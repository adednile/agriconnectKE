<?php
// app/Http/Controllers/DriverController.php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Order;
use App\Models\DriverLocation;
use App\Models\Notification;
use Illuminate\Support\Facades\Auth;

class DriverController extends Controller
{
    public function dashboard()
    {
        $driver = Auth::user();
        $assignedOrders = Order::where('driver_id', $driver->id)
            ->with('product', 'buyer', 'farmer')
            ->get();

        $completedOrders = $assignedOrders->where('status', 'delivered');
        $pendingOrders = $assignedOrders->whereIn('status', ['paid', 'shipped']);

        $earnings = $completedOrders->sum('delivery_cost');

        return view('driver.dashboard', compact('assignedOrders', 'completedOrders', 'pendingOrders', 'earnings'));
    }

    public function deliveries()
    {
        $orders = Order::where('driver_id', Auth::id())
            ->with('product', 'buyer', 'farmer')
            ->latest()
            ->get();

        return view('driver.deliveries', compact('orders'));
    }

    public function updateDeliveryStatus(Request $request, Order $order)
    {
        if ($order->driver_id !== Auth::id()) {
            abort(403);
        }

        $request->validate([
            'status' => 'required|in:shipped,delivered'
        ]);

        $order->update(['status' => $request->status]);

        // Notify buyer
        Notification::create([
            'user_id' => $order->buyer_id,
            'title' => 'Delivery Status Updated',
            'message' => "Your order #{$order->id} has been {$request->status}",
            'type' => 'info'
        ]);

        return back()->with('success', 'Delivery status updated successfully!');
    }

    public function updateLocation(Request $request)
    {
        $request->validate([
            'latitude' => 'required|numeric',
            'longitude' => 'required|numeric'
        ]);

        DriverLocation::updateOrCreate(
            ['driver_id' => Auth::id()],
            [
                'latitude' => $request->latitude,
                'longitude' => $request->longitude,
                'location_updated_at' => now()
            ]
        );

        return response()->json(['success' => true, 'message' => 'Location updated successfully']);
    }

    public function getCurrentLocation()
    {
        $location = DriverLocation::where('driver_id', Auth::id())->first();
        
        return response()->json([
            'latitude' => $location->latitude ?? 0,
            'longitude' => $location->longitude ?? 0
        ]);
    }