<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use App\Models\Product;
use App\Models\Order;
use App\Models\Bid;
use App\Models\DriverLocation;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;

class AdminController extends Controller
{
    public function dashboard()
    {
        $stats = [
            'total_users' => User::count(),
            'total_farmers' => User::farmers()->count(),
            'total_buyers' => User::buyers()->count(),
            'total_drivers' => User::drivers()->count(),
            'total_products' => Product::count(),
            'total_orders' => Order::count(),
            'pending_orders' => Order::where('status', 'pending')->count(),
            'total_sales' => Order::where('status', 'paid')->sum('amount')
        ];

        $recentOrders = Order::with('buyer', 'product')->latest()->take(10)->get();
        $recentUsers = User::withCount(['products', 'orders'])->latest()->take(5)->get();

        return view('admin.dashboard', compact('stats', 'recentOrders', 'recentUsers'));
    }
}