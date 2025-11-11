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