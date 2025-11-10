<?php
// app/Http/Controllers/FarmerController.php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Product;
use App\Models\Bid;
use App\Models\Order;
use App\Models\Notification;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Log;

class FarmerController extends Controller
{
    public function dashboard()
    {
        $user = Auth::user();
        $products = Product::where('farmer_id', $user->id)->latest()->get();
        $bids = Bid::whereHas('product', function($query) use ($user) {
            $query->where('farmer_id', $user->id);
        })->with('product', 'buyer')->latest()->get();
        
        $totalSales = Order::where('farmer_id', $user->id)
            ->where('status', 'paid')
            ->sum('amount');

        return view('farmer.dashboard', compact('products', 'bids', 'totalSales'));
    }

    public function products()
    {
        $products = Product::where('farmer_id', Auth::id())->latest()->get();
        return view('farmer.products', compact('products'));
    }

    public function createProduct()
    {
        Log::info('Create product form accessed by user: ' . Auth::id());
        return view('farmer.create-product');
    }


      public function storeProduct(Request $request)
    {
        Log::info('Store product method called by user: ' . Auth::id(), $request->all());
        
        $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'required|string',
            'price' => 'required|numeric|min:0',
            'quantity' => 'required|integer|min:1',
            'category' => 'required|string',
            'image' => 'nullable|image|mimes:jpeg,png,jpg,gif|max:2048',
            'accepts_bids' => 'sometimes|boolean'
        ]);

        Log::info('Validation passed');

        try {
            $imagePath = null;
            
            // Handle image upload
            if ($request->hasFile('image')) {
                Log::info('Image file detected');
                $imagePath = $request->file('image')->store('product-images', 'public');
                Log::info('Image stored at: ' . $imagePath);
            }

            Log::info('Creating product in database');
            $product = Product::create([
                'farmer_id' => Auth::id(),
                'name' => $request->name,
                'description' => $request->description,
                'price' => $request->price,
                'quantity' => $request->quantity,
                'category' => $request->category,
                'image' => $imagePath,
                'accepts_bids' => $request->has('accepts_bids')
            ]);

            Log::info('Product created successfully with ID: ' . $product->id);

            return redirect()->route('farmer.products')->with('success', 'Product listed successfully!');
            
        } catch (\Exception $e) {
            Log::error('Product creation failed: ' . $e->getMessage());
            Log::error('Stack trace: ' . $e->getTraceAsString());
            return back()->with('error', 'Failed to create product: ' . $e->getMessage())->withInput();
        }
    }
    public function bids()
    {
        $bids = Bid::whereHas('product', function($query) {
            $query->where('farmer_id', Auth::id());
        })->with('product', 'buyer')->latest()->get();

        return view('farmer.bids', compact('bids'));
    }

    public function updateBidStatus(Request $request, Bid $bid)
    {
        $request->validate([
            'status' => 'required|in:accepted,rejected'
        ]);

        // Check if the bid belongs to farmer's product
        if ($bid->product->farmer_id !== Auth::id()) {
            return back()->with('error', 'Unauthorized action.');
        }

        $bid->update(['status' => $request->status]);

        // Create notification for buyer
        Notification::create([
            'user_id' => $bid->buyer_id,
            'title' => 'Bid ' . ucfirst($request->status),
            'message' => "Your bid for {$bid->product->name} has been {$request->status}",
            'type' => $request->status === 'accepted' ? 'success' : 'warning'
        ]);

        // If bid is accepted, create an order
        if ($request->status === 'accepted') {
            $order = Order::create([
                'product_id' => $bid->product_id,
                'buyer_id' => $bid->buyer_id,
                'farmer_id' => Auth::id(),
                'amount' => $bid->amount,
                'quantity' => 1, // Default quantity for bid acceptance
                'delivery_address' => $bid->buyer->address,
                'delivery_lat' => $bid->buyer->latitude,
                'delivery_lng' => $bid->buyer->longitude,
                'status' => 'pending'
            ]);

            // Update product quantity
            $bid->product->decrement('quantity', 1);

            if ($bid->product->quantity <= 0) {
                $bid->product->update(['is_available' => false]);
            }

            // Notify buyer about order creation
            Notification::create([
                'user_id' => $bid->buyer_id,
                'title' => 'Order Created',
                'message' => "An order has been created for your accepted bid on {$bid->product->name}. Please proceed to checkout.",
                'type' => 'info'
            ]);
        }

        return back()->with('success', 'Bid status updated successfully!');
    }

    public function sales()
    {
        $sales = Order::where('farmer_id', Auth::id())
            ->with('product', 'buyer')
            ->latest()
            ->get();

        $totalSales = $sales->where('status', 'paid')->sum('amount');

        return view('farmer.sales', compact('sales', 'totalSales'));
    }
}