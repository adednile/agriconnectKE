<!-- resources/views/buyer/track-order.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Track Order #{{ $order->id }}</h1>
</div>

<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5>Order Details</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <strong>Product:</strong> {{ $order->product->name }}<br>
                        <strong>Farmer:</strong> {{ $order->farmer->name }}<br>
                        <strong>Quantity:</strong> {{ $order->quantity }}
                    </div>
                    <div class="col-md-6">
                        <strong>Total Amount:</strong> Ksh {{ number_format($order->amount, 2) }}<br>
                        <strong>Status:</strong> 
                        <span class="badge bg-{{ $order->status == 'paid' ? 'success' : ($order->status == 'shipped' ? 'primary' : 'info') }}">
                            {{ ucfirst($order->status) }}
                        </span><br>
                        <strong>Delivery Address:</strong> {{ $order->delivery_address }}
                    </div>
                </div>
            </div>
        </div>

        <div class="card mt-4">
            <div class="card-header">
                <h5>Delivery Tracking</h5>
            </div>
            <div class="card-body">
                @if($order->driver)
                    <div class="alert alert-info">
                        <p><strong>Driver:</strong> {{ $order->driver->name }}</p>
                        <p><strong>Driver Phone:</strong> {{ $order->driver->phone }}</p>
                        @if($driverLocation)
                            <p><strong>Last Location Update:</strong> {{ $driverLocation->location_updated_at->diffForHumans() }}</p>
                        @endif
                    </div>
                    
                    <div id="map" style="height: 400px; width: 100%;"></div>
                @else
                    <div class="alert alert-warning">
                        <p>No driver has been assigned to your order yet. Please check back later.</p>
                    </div>
                @endif
            </div>
        </div>
    </div>
</div>
@endsection

@push('scripts')
@if($order->driver && $driverLocation)
<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Initialize map
        var map = L.map('map').setView([{{ $driverLocation->latitude }}, {{ $driverLocation->longitude }}], 13);
        
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(map);
        
        // Add driver location marker
        L.marker([{{ $driverLocation->latitude }}, {{ $driverLocation->longitude }}])
            .addTo(map)
            .bindPopup('Driver Location')
            .openPopup();
            
        // Add delivery location marker
        L.marker([{{ $order->delivery_lat ?? $driverLocation->latitude }}, {{ $order->delivery_lng ?? $driverLocation->longitude }}])
            .addTo(map)
            .bindPopup('Delivery Location')
            .openPopup();
    });
</script>
@endif
@endpush
