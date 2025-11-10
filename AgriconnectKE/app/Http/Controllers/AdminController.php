<?php
// app/Http/Controllers/AdminController.php

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

    public function users()
    {
        $users = User::withCount(['products', 'orders'])->latest()->get();
        return view('admin.users', compact('users'));
    }

    public function createUser()
    {
        return view('admin.create-user');
    }

    public function storeUser(Request $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|email|unique:users',
            'password' => 'required|min:8|confirmed',
            'role' => 'required|in:farmer,buyer,driver,admin',
            'phone' => 'required|string',
            'address' => 'required|string'
        ]);

        User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'role' => $request->role,
            'phone' => $request->phone,
            'address' => $request->address
        ]);

        return redirect()->route('admin.users')->with('success', 'User created successfully!');
    }

    public function editUser(User $user)
    {
        return view('admin.edit-user', compact('user'));
    }

    public function updateUser(Request $request, User $user)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|email|unique:users,email,' . $user->id,
            'role' => 'required|in:farmer,buyer,driver,admin',
            'phone' => 'required|string',
            'address' => 'required|string'
        ]);

        $user->update($request->only(['name', 'email', 'role', 'phone', 'address']));

        if ($request->filled('password')) {
            $user->update(['password' => Hash::make($request->password)]);
        }

        return redirect()->route('admin.users')->with('success', 'User updated successfully!');
    }

    public function deleteUser(User $user)
    {
        if ($user->id === Auth::id()) {
            return back()->with('error', 'You cannot delete your own account.');
        }

        $user->delete();
        return back()->with('success', 'User deleted successfully!');
    }

    public function products()
    {
        $products = Product::with('farmer')->latest()->get();
        return view('admin.products', compact('products'));
    }

    public function editProduct(Product $product)
    {
        return view('admin.edit-product', compact('product'));
    }

    public function updateProduct(Request $request, Product $product)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'description' => 'required|string',
            'price' => 'required|numeric|min:0',
            'quantity' => 'required|integer|min:0',
            'category' => 'required|string',
            'is_available' => 'boolean',
            'accepts_bids' => 'boolean'
        ]);

        $product->update([
            'name' => $request->name,
            'description' => $request->description,
            'price' => $request->price,
            'quantity' => $request->quantity,
            'category' => $request->category,
            'is_available' => $request->has('is_available'),
            'accepts_bids' => $request->has('accepts_bids')
        ]);

        return redirect()->route('admin.products')->with('success', 'Product updated successfully!');
    }

    public function deleteProduct(Product $product)
    {
        $product->delete();
        return back()->with('success', 'Product deleted successfully!');
    }

    public function orders()
    {
        $orders = Order::with(['buyer', 'farmer', 'product', 'driver'])->latest()->get();
        return view('admin.orders', compact('orders'));
    }

    public function updateOrderStatus(Request $request, Order $order)
    {
        $request->validate([
            'status' => 'required|in:pending,paid,shipped,delivered,cancelled'
        ]);

        $order->update(['status' => $request->status]);

        // Notify buyer about status change
        \App\Models\Notification::create([
            'user_id' => $order->buyer_id,
            'title' => 'Order Status Updated',
            'message' => "Your order #{$order->id} status has been updated to: {$request->status}",
            'type' => 'info'
        ]);

        return back()->with('success', 'Order status updated successfully!');
    }

    public function trackDrivers()
    {
        $drivers = User::drivers()->with('driverLocation')->get();
        return view('admin.track-drivers', compact('drivers'));
    }

    public function updateDriverLocation(Request $request, User $driver)
    {
        if (!$driver->isDriver()) {
            return back()->with('error', 'User is not a driver.');
        }

        $request->validate([
            'latitude' => 'required|numeric',
            'longitude' => 'required|numeric'
        ]);

        DriverLocation::updateOrCreate(
            ['driver_id' => $driver->id],
            [
                'latitude' => $request->latitude,
                'longitude' => $request->longitude,
                'location_updated_at' => now()
            ]
        );

        return back()->with('success', 'Driver location updated successfully!');
    }

    public function systemStats()
    {
        $monthlySales = Order::where('status', 'paid')
            ->whereYear('created_at', now()->year)
            ->whereMonth('created_at', now()->month)
            ->sum('amount');

        $weeklyOrders = Order::where('created_at', '>=', now()->subWeek())->count();

        $popularProducts = Product::withCount('orders')
            ->orderBy('orders_count', 'desc')
            ->take(5)
            ->get();

        return view('admin.system-stats', compact('monthlySales', 'weeklyOrders', 'popularProducts'));
    }
}