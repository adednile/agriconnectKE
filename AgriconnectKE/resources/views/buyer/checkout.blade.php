<!-- resources/views/buyer/checkout.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Checkout</h1>
</div>

<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5>Order Summary</h5>
            </div>
            <div class="card-body">
                <div class="row mb-3">
                    <div class="col-md-6">
                        <strong>Product:</strong> {{ $order->product->name }}<br>
                        <strong>Farmer:</strong> {{ $order->farmer->name }}<br>
                        <strong>Quantity:</strong> {{ $order->quantity }}
                    </div>
                    <div class="col-md-6">
                        <strong>Unit Price:</strong> Ksh {{ number_format($order->product->price, 2) }}<br>
                        <strong>Subtotal:</strong> Ksh {{ number_format($order->product->price * $order->quantity, 2) }}<br>
                        <strong>Delivery Cost:</strong> Ksh {{ number_format($order->delivery_cost, 2) }}<br>
                        <strong>Total Amount:</strong> Ksh {{ number_format($order->amount, 2) }}<br>
                        <strong>Delivery Address:</strong> {{ $order->delivery_address }}
                    </div>
                </div>
            </div>
        </div>

        <div class="card mt-4">
            <div class="card-header">
                <h5>Payment Method</h5>
            </div>
            <div class="card-body">
                <div class="alert alert-info">
                    <h6>M-Pesa Payment Simulation</h6>
                    <p>This is a simulation of M-Pesa payment. In a real application, this would integrate with the M-Pesa API.</p>
                </div>
                
                <form method="POST" action="{{ route('buyer.payment', $order) }}">
                    @csrf
                    <div class="mb-3">
                        <label for="phone" class="form-label">M-Pesa Phone Number</label>
                        <input type="text" class="form-control" id="phone" name="phone" 
                               value="{{ Auth::user()->phone }}" required placeholder="e.g., 254712345678">
                        <div class="form-text">Enter your M-Pesa registered phone number</div>
                    </div>
                    
                    <div class="d-grid">
                        <button type="submit" class="btn btn-success btn-lg">
                            Pay Ksh {{ number_format($order->amount, 2) }} via M-Pesa
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
@endsection
