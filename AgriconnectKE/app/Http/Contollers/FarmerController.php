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