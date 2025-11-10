<!-- resources/views/buyer/orders.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">My Orders</h1>
</div>

<div class="row">
    <div class="col-12">
        @if($orders->count() > 0)
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Product</th>
                            <th>Farmer</th>
                            <th>Quantity</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($orders as $order)
                        <tr>
                            <td>#{{ $order->id }}</td>
                            <td>{{ $order->product->name }}</td>
                            <td>{{ $order->farmer->name }}</td>
                            <td>{{ $order->quantity }}</td>
                            <td>Ksh {{ number_format($order->amount, 2) }}</td>
                            <td>
                                <span class="badge bg-{{ 
                                    $order->status == 'paid' ? 'success' : 
                                    ($order->status == 'delivered' ? 'info' : 
                                    ($order->status == 'shipped' ? 'primary' : 'warning')) 
                                }}">
                                    {{ ucfirst($order->status) }}
                                </span>
                            </td>
                            <td>{{ $order->created_at->format('M d, Y H:i') }}</td>
                            <td>
                                @if($order->status == 'paid' || $order->status == 'shipped')
                                <a href="{{ route('buyer.track-order', $order) }}" class="btn btn-sm btn-info">Track</a>
                                @endif
                            </td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
        @else
            <div class="alert alert-info">
                <p>You haven't placed any orders yet. <a href="{{ route('buyer.market') }}">Browse the marketplace</a> to make your first purchase.</p>
            </div>
        @endif
    </div>
</div>
@endsection
