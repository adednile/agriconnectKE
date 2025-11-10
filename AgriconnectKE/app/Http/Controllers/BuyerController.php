<?php
// app/Http/Controllers/BuyerController.php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Product;
use App\Models\Bid;
use App\Models\Order;
use App\Models\Notification;
use App\Models\User;
use Illuminate\Support\Facades\Auth;

class BuyerController extends Controller
{
    public function dashboard()
    {
        $user = Auth::user();
        $products = Product::available()->with('farmer')->get();
        $bids = Bid::where('buyer_id', $user->id)->with('product')->get();
        $orders = Order::where('buyer_id', $user->id)->with('product', 'farmer')->get();

        return view('buyer.dashboard', compact('products', 'bids', 'orders'));
    }

    public function market()
    {
        $products = Product::available()->with('farmer')->get();
        return view('buyer.market', compact('products'));
    }

    public function placeBid(Request $request, Product $product)
    {
        $request->validate([
            'amount' => 'required|numeric|min:0'
        ]);

        if (!$product->accepts_bids) {
            return back()->with('error', 'This product does not accept bids.');
        }

        if ($product->farmer_id == Auth::id()) {
            return back()->with('error', 'You cannot bid on your own product.');
        }

        Bid::create([
            'product_id' => $product->id,
            'buyer_id' => Auth::id(),
            'amount' => $request->amount
        ]);

        // Notify farmer
        Notification::create([
            'user_id' => $product->farmer_id,
            'title' => 'New Bid Received',
            'message' => "A new bid of Ksh " . number_format($request->amount, 2) . " has been placed on your product: {$product->name}",
            'type' => 'info'
        ]);

        return back()->with('success', 'Bid placed successfully!');
    }

    public function purchaseProduct(Request $request, Product $product)
    {
        $request->validate([
            'quantity' => 'required|integer|min:1|max:' . $product->quantity
        ]);

        if ($product->farmer_id == Auth::id()) {
            return back()->with('error', 'You cannot purchase your own product.');
        }

        // Calculate delivery cost (simplified - in real app, calculate based on distance)
        $deliveryCost = $this->calculateDeliveryCost(Auth::user());

        // Create order
        $order = Order::create([
            'product_id' => $product->id,
            'buyer_id' => Auth::id(),
            'farmer_id' => $product->farmer_id,
            'amount' => ($product->price * $request->quantity) + $deliveryCost,
            'quantity' => $request->quantity,
            'delivery_cost' => $deliveryCost,
            'delivery_address' => Auth::user()->address,
            'delivery_lat' => Auth::user()->latitude,
            'delivery_lng' => Auth::user()->longitude
        ]);

        // Update product quantity
        $product->decrement('quantity', $request->quantity);

        if ($product->quantity <= 0) {
            $product->update(['is_available' => false]);
        }

        return redirect()->route('buyer.checkout', $order);
    }

    public function checkout(Order $order)
    {
        if ($order->buyer_id !== Auth::id()) {
            abort(403);
        }

        return view('buyer.checkout', compact('order'));
    }

    public function processPayment(Request $request, Order $order)
    {
        if ($order->buyer_id !== Auth::id()) {
            abort(403);
        }

        $request->validate([
            'phone' => 'required|string'
        ]);

        // Simulate M-Pesa payment
        $mpesaReceipt = 'MPESA' . time() . rand(1000, 9999);

        $order->update([
            'status' => 'paid',
            'mpesa_receipt' => $mpesaReceipt
        ]);

        // Assign driver (simplified - in real app, find nearest available driver)
        $this->assignDriver($order);

        // Create receipt notification for buyer
        Notification::create([
            'user_id' => Auth::id(),
            'title' => 'Payment Successful',
            'message' => "Your payment for order #{$order->id} has been processed. Receipt: {$mpesaReceipt}",
            'type' => 'success',
            'data' => ['order_id' => $order->id, 'receipt' => $mpesaReceipt]
        ]);

        // Notify farmer
        Notification::create([
            'user_id' => $order->farmer_id,
            'title' => 'Order Paid',
            'message' => "Order #{$order->id} has been paid. Please prepare the products for delivery.",
            'type' => 'info'
        ]);

        return redirect()->route('buyer.orders')->with('success', 'Payment processed successfully!');
    }

    public function orders()
    {
        $orders = Order::where('buyer_id', Auth::id())
            ->with('product', 'farmer', 'driver')
            ->latest()
            ->get();

        return view('buyer.orders', compact('orders'));
    }

    public function trackOrder(Order $order)
    {
        if ($order->buyer_id !== Auth::id()) {
            abort(403);
        }

        $driverLocation = $order->driver ? $order->driver->driverLocation : null;

        return view('buyer.track-order', compact('order', 'driverLocation'));
    }

    public function bids()
    {
        $bids = Bid::where('buyer_id', Auth::id())
            ->with('product.farmer')
            ->latest()
            ->get();

        return view('buyer.bids', compact('bids'));
    }

    private function calculateDeliveryCost($buyer)
    {
        // Simplified delivery cost calculation
        // In real application, this would calculate based on distance from farmer to buyer
        return 200; // Fixed cost for demo
    }

    private function assignDriver(Order $order)
    {
        // Find nearest available driver (simplified)
        $driver = User::drivers()->first();
        
        if ($driver) {
            $order->update(['driver_id' => $driver->id]);
            
            // Notify driver
            Notification::create([
                'user_id' => $driver->id,
                'title' => 'New Delivery Assignment',
                'message' => "You have been assigned to deliver order #{$order->id}",
                'type' => 'info'
            ]);
        }
    }
}