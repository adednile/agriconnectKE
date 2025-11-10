<!-- resources/views/admin/dashboard.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Admin Dashboard</h1>
</div>

<div class="row">
    <div class="col-md-3 mb-4">
        <div class="card text-white bg-primary">
            <div class="card-body">
                <h5 class="card-title">Total Users</h5>
                <h2 class="card-text">{{ $stats['total_users'] }}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card text-white bg-success">
            <div class="card-body">
                <h5 class="card-title">Farmers</h5>
                <h2 class="card-text">{{ $stats['total_farmers'] }}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card text-white bg-info">
            <div class="card-body">
                <h5 class="card-title">Buyers</h5>
                <h2 class="card-text">{{ $stats['total_buyers'] }}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card text-white bg-warning">
            <div class="card-body">
                <h5 class="card-title">Products</h5>
                <h2 class="card-text">{{ $stats['total_products'] }}</h2>
            </div>
        </div>
    </div>
</div>

<div class="row mt-4">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5>Recent Orders</h5>
            </div>
            <div class="card-body">
                @if($recentOrders->count() > 0)
                    <div class="table-responsive">
                        <table class="table table-sm">
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Product</th>
                                    <th>Buyer</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                @foreach($recentOrders as $order)
                                <tr>
                                    <td>#{{ $order->id }}</td>
                                    <td>{{ $order->product->name }}</td>
                                    <td>{{ $order->buyer->name }}</td>
                                    <td>Ksh {{ number_format($order->amount, 2) }}</td>
                                    <td>
                                        <span class="badge bg-{{ $order->status == 'paid' ? 'success' : 'warning' }}">
                                            {{ ucfirst($order->status) }}
                                        </span>
                                    </td>
                                </tr>
                                @endforeach
                            </tbody>
                        </table>
                    </div>
                @else
                    <p>No orders yet.</p>
                @endif
            </div>
        </div>
    </div>
       <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5>Quick Stats</h5>
            </div>
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item d-flex justify-content-between">
                        <span>Total Orders:</span>
                        <strong>{{ $stats['total_orders'] }}</strong>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>Pending Orders:</span>
                        <strong>{{ $stats['pending_orders'] }}</strong>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>Total Sales:</span>
                        <strong>Ksh {{ number_format($stats['total_sales'], 2) }}</strong>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>Drivers:</span>
                        <strong>{{ $stats['total_drivers'] }}</strong>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
@endsection

 
