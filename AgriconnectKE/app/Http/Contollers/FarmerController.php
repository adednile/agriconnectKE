<?php
namespace App\Http\Controllers;

use App\Models\Bid;
use App\Models\Order;
use App\Models\Product;
use App\Models\Notification;
use App\Services\DeliveryService; // ADD THIS IMPORT
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Storage;

class FarmerController extends Controller
{
    public function dashboard()
    {
        $farmerId = Auth::id();
        
        $recentProducts = Product::where('farmer_id', $farmerId)
            ->latest()
            ->take(5)
            ->get();
            
        $recentBids = Bid::whereIn('product_id', function($query) use ($farmerId) {
                $query->select('id')
                    ->from('products')
                    ->where('farmer_id', $farmerId);
            })
            ->with(['product', 'buyer'])
            ->latest()
            ->take(5)
            ->get();
            
        $recentSales = Order::where('farmer_id', $farmerId)
            ->with(['product', 'buyer'])
            ->latest()
            ->take(5)
            ->get();
            
        $stats = [
            'total_products' => Product::where('farmer_id', $farmerId)->count(),
            'active_bids' => Bid::whereIn('product_id', function($query) use ($farmerId) {
                $query->select('id')
                    ->from('products')
                    ->where('farmer_id', $farmerId);
            })->where('status', 'pending')->count(),
            'total_sales' => Order::where('farmer_id', $farmerId)->count(),
            'revenue' => Order::where('farmer_id', $farmerId)->sum('amount'),
        ];

        return view('farmer.dashboard', compact('recentProducts', 'recentBids', 'recentSales', 'stats'));
    }

    public function products()
    {
        $products = Product::where('farmer_id', Auth::id())
            ->withCount(['bids' => function($query) {
                $query->where('status', 'pending');
            }])
            ->latest()
            ->paginate(10);

        return view('farmer.products.index', compact('products'));
    }

    public function createProduct()
    {
        return view('farmer.products.create');
    }

    public function storeProduct(Request $request)
    {
        $validated = $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'required|string',
            'price' => 'required|numeric|min:0',
            'quantity' => 'required|integer|min:1',
            'category' => 'required|string',
            'image' => 'required|image|mimes:jpeg,png,jpg,gif|max:2048',
        ]);

        $imagePath = $request->file('image')->store('products', 'public');
    
        // Handle checkbox properly - if checked, value is "on", if unchecked, nothing is submitted
        $acceptsBids = $request->has('accepts_bids');
        
        $product = Product::create([
            'name' => $validated['name'],
            'description' => $validated['description'],
            'price' => $validated['price'],
            'quantity' => $validated['quantity'],
            'category' => $validated['category'],
            'image' => $imagePath,
            'farmer_id' => Auth::id(),
            'accepts_bids' => $acceptsBids,
            'is_available' => true,
        ]);

        return redirect()->route('farmer.products')
            ->with('success', 'Product created successfully.');
    }

        public function editProduct(Product $product)
    {
        if ($product->farmer_id !== Auth::id()) {
            abort(403, 'Unauthorized action.');
        }

        return view('farmer.products.edit', compact('product'));
    }

    public function updateProduct(Request $request, Product $product)
    {
        if ($product->farmer_id !== Auth::id()) {
            abort(403, 'Unauthorized action.');
        }

        $validated = $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'required|string',
            'price' => 'required|numeric|min:0',
            'quantity' => 'required|integer|min:1',
            'category' => 'required|string',
            'image' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048',
        ]);

        if ($request->hasFile('image')) {
            // Delete old image
            if ($product->image) {
                Storage::disk('public')->delete($product->image);
            }
            
            // Store new image
            $imagePath = $request->file('image')->store('products', 'public');
            $validated['image'] = $imagePath;
        }

        // Handle checkbox properly
        $acceptsBids = $request->has('accepts_bids');

        $product->update([
            'name' => $validated['name'],
            'description' => $validated['description'],
            'price' => $validated['price'],
            'quantity' => $validated['quantity'],
            'category' => $validated['category'],
            'image' => $validated['image'] ?? $product->image,
            'accepts_bids' => $acceptsBids,
        ]);

        return redirect()->route('farmer.products')
            ->with('success', 'Product updated successfully.');
    }

    public function destroyProduct(Product $product)
    {
        if ($product->farmer_id !== Auth::id()) {
            abort(403, 'Unauthorized action.');
        }

        // Delete product image
        if ($product->image) {
            Storage::disk('public')->delete($product->image);
        }

        $product->delete();

        return redirect()->route('farmer.products')
            ->with('success', 'Product deleted successfully.');
    }

    public function bids()
    {
        $bids = Bid::whereIn('product_id', function($query) {
            $query->select('id')
                ->from('products')
                ->where('farmer_id', Auth::id());
        })
        ->with(['product', 'buyer'])
        ->latest()
        ->paginate(10);

        return view('farmer.bids.index', compact('bids'));
    }

    // FIXED ACCEPT BID METHOD - COMPLETE VERSION
     public function acceptBid(Bid $bid)
    {
        if ($bid->product->farmer_id !== Auth::id()) {
            abort(403, 'Unauthorized action.');
        }

        // Check if product is still available
        if ($bid->product->quantity < 1) {
            return redirect()->route('farmer.bids')
                ->with('error', 'Product is out of stock. Cannot accept bid.');
        }

        // Calculate delivery cost
        $deliveryService = new DeliveryService();
        $buyer = $bid->buyer;
        $farmer = Auth::user();
        $deliveryCost = $deliveryService->calculateDeliveryCost($buyer, $farmer);
        
        $totalAmount = $bid->amount + $deliveryCost;

        // Create order from accepted bid
        $order = Order::create([
            'product_id' => $bid->product_id,
            'buyer_id' => $bid->buyer_id,
            'farmer_id' => Auth::id(),
            'bid_id' => $bid->id,
            'amount' => $totalAmount, // Include delivery cost
            'quantity' => 1, // Default quantity for bids
            'status' => 'pending', // Order is created but payment is pending
            'delivery_cost' => $deliveryCost,
            'delivery_address' => $bid->buyer->address,
            'delivery_lat' => $bid->buyer->latitude ?? -1.2921, // Default Nairobi coordinates
            'delivery_lng' => $bid->buyer->longitude ?? 36.8219,
        ]);

        // Update bid status
        $bid->update(['status' => 'accepted']);

        // Reject other bids for the same product
        Bid::where('product_id', $bid->product_id)
            ->where('id', '!=', $bid->id)
            ->where('status', 'pending')
            ->update(['status' => 'rejected']);

        // Update product quantity
        $bid->product->decrement('quantity', 1);

        // Mark product as unavailable if quantity reaches 0
        if ($bid->product->quantity <= 0) {
            $bid->product->update(['is_available' => false]);
        }

        // Notify buyer with checkout link
        Notification::create([
            'user_id' => $bid->buyer_id,
            'title' => 'Bid Accepted! 🎉',
            'message' => "Your bid of Ksh " . number_format($bid->amount, 2) . " for {$bid->product->name} has been accepted! Total amount: Ksh " . number_format($totalAmount, 2) . " (including delivery).",
            'type' => 'success',
            'data' => [
                'order_id' => $order->id,
                'action_url' => route('buyer.checkout.bid', $order),
                'action_text' => 'Complete Purchase'
            ]
        ]);

        // Also notify the farmer
        Notification::create([
            'user_id' => Auth::id(),
            'title' => 'Bid Accepted',
            'message' => "You accepted a bid for {$bid->product->name}. Waiting for buyer payment.",
            'type' => 'info'
        ]);

        return redirect()->route('farmer.bids')
            ->with('success', 'Bid accepted successfully. Order created and buyer notified to complete payment.');
    }

    // FIXED REJECT BID METHOD
    public function rejectBid(Bid $bid)
    {
        if ($bid->product->farmer_id !== Auth::id()) {
            abort(403, 'Unauthorized action.');
        }

        $bid->update(['status' => 'rejected']);

        // Notify buyer
        Notification::create([
            'user_id' => $bid->buyer_id,
            'title' => 'Bid Rejected',
            'message' => "Your bid of Ksh " . number_format($bid->amount, 2) . " for {$bid->product->name} has been rejected by the farmer.",
            'type' => 'warning'
        ]);

        return redirect()->route('farmer.bids')
            ->with('success', 'Bid rejected successfully.');
    }

    public function sales()
    {
        $sales = Order::where('farmer_id', Auth::id())
            ->with(['product', 'buyer'])
            ->latest()
            ->paginate(10);

        $totalSales = Order::where('farmer_id', Auth::id())->sum('amount');

        return view('farmer.sales.index', compact('sales', 'totalSales'));
    }

    public function showSale(Order $order)
    {
        if ($order->farmer_id !== Auth::id()) {
            abort(403, 'Unauthorized action.');
        }

        $order->load(['product', 'buyer', 'driver']);
        return view('farmer.sales.show', compact('order'));
    }
}