<!-- resources/views/buyer/dashboard.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Buyer Dashboard</h1>
</div>

<div class="row">
    <div class="col-md-4">
        <div class="card text-white bg-success mb-3">
            <div class="card-body">
                <h5 class="card-title">Total Orders</h5>
                <h2 class="card-text">{{ $orders->count() }}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-primary mb-3">
            <div class="card-body">
                <h5 class="card-title">Active Bids</h5>
                <h2 class="card-text">{{ $bids->where('status', 'pending')->count() }}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-info mb-3">
            <div class="card-body">
                <h5 class="card-title">Available Products</h5>
                <h2 class="card-text">{{ $products->count() }}</h2>
            </div>
        </div>
    </div>
</div>

<div class="row mt-4">
    <div class="col-md-6">
        <h4>Recent Orders</h4>
        <div class="list-group">
            @forelse($orders->take(5) as $order)
            <div class="list-group-item">
                <h6>{{ $order->product->name }}</h6>
                <small>Amount: Ksh {{ number_format($order->amount, 2) }}</small>
                <span class="badge bg-{{ $order->status == 'paid' ? 'success' : 'warning' }} float-end">
                    {{ ucfirst($order->status) }}
                </span>
            </div>
            @empty
            <div class="list-group-item">
                <p class="text-muted">No orders yet.</p>
            </div>
            @endforelse
        </div>
    </div>
    
    <div class="col-md-6">
        <h4>Recent Bids</h4>
        <div class="list-group">
            @forelse($bids->take(5) as $bid)
            <div class="list-group-item">
                <h6>{{ $bid->product->name }}</h6>
                <small>Your Bid: Ksh {{ number_format($bid->amount, 2) }}</small>
                <span class="badge bg-{{ $bid->status == 'pending' ? 'warning' : ($bid->status == 'accepted' ? 'success' : 'danger') }} float-end">
                    {{ ucfirst($bid->status) }}
                </span>
            </div>
            @empty
            <div class="list-group-item">
                <p class="text-muted">No bids yet.</p>
            </div>
            @endforelse
        </div>
    </div>
</div>

<div class="row mt-4">
    <div class="col-12">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Quick Actions</h5>
                <a href="{{ route('buyer.market') }}" class="btn btn-success">Browse Marketplace</a>
                <a href="{{ route('buyer.orders') }}" class="btn btn-primary">View My Orders</a>
                <a href="{{ route('buyer.bids') }}" class="btn btn-warning">View My Bids</a>
            </div>
        </div>
    </div>
</div>
@endsection
