<!-- resources/views/buyer/market.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Marketplace</h1>
</div>

<div class="row mb-4">
    <div class="col-12">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Browse Fresh Products</h5>
                <p class="card-text">Find the best agricultural products from local farmers.</p>
            </div>
        </div>
    </div>
</div>

<div class="row">
    @forelse($products as $product)
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body">
                <h5 class="card-title">{{ $product->name }}</h5>
                <p class="card-text">{{ Str::limit($product->description, 100) }}</p>
                <p class="card-text">
                    <strong>Price:</strong> Ksh {{ number_format($product->price, 2) }}<br>
                    <strong>Quantity:</strong> {{ $product->quantity }}<br>
                    <strong>Farmer:</strong> {{ $product->farmer->name }}<br>
                    <strong>Category:</strong> {{ ucfirst($product->category) }}
                </p>
                
                @if($product->accepts_bids)
                <button class="btn btn-warning btn-sm mb-2" data-bs-toggle="modal" data-bs-target="#bidModal{{ $product->id }}">
                    Place Bid
                </button>
                @endif
                
                <button class="btn btn-success btn-sm mb-2" data-bs-toggle="modal" data-bs-target="#purchaseModal{{ $product->id }}">
                    Buy Now
                </button>
            </div>
        </div>
    </div>

    <!-- Bid Modal -->
    <div class="modal fade" id="bidModal{{ $product->id }}" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="{{ route('buyer.place-bid', $product) }}" method="POST">
                    @csrf
                    <div class="modal-header">
                        <h5 class="modal-title">Place Bid - {{ $product->name }}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p>Current Price: Ksh {{ number_format($product->price, 2) }}</p>
                        <div class="mb-3">
                            <label for="amount" class="form-label">Your Bid Amount (Ksh)</label>
                            <input type="number" class="form-control" id="amount" name="amount" 
                                   step="0.01" min="0" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-warning">Place Bid</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Purchase Modal -->
    <div class="modal fade" id="purchaseModal{{ $product->id }}" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="{{ route('buyer.purchase', $product) }}" method="POST">
                    @csrf
                    <div class="modal-header">
                        <h5 class="modal-title">Purchase - {{ $product->name }}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p>Price: Ksh {{ number_format($product->price, 2) }} per unit</p>
                        <div class="mb-3">
                            <label for="quantity" class="form-label">Quantity</label>
                            <input type="number" class="form-control" id="quantity" name="quantity" 
                                   min="1" max="{{ $product->quantity }}" value="1" required>
                        </div>
                        <p class="total-price">Total: Ksh <span id="totalPrice{{ $product->id }}">{{ number_format($product->price, 2) }}</span></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">Proceed to Checkout</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    @empty
    <div class="col-12">
        <div class="alert alert-info">
            <p>No products available in the marketplace at the moment.</p>
        </div>
    </div>
    @endforelse
</div>
@endsection

@push('scripts')
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Calculate total price for purchase modals
        @foreach($products as $product)
        const quantityInput{{ $product->id }} = document.getElementById('quantity');
        const totalPrice{{ $product->id }} = document.getElementById('totalPrice{{ $product->id }}');
        
        if (quantityInput{{ $product->id }} && totalPrice{{ $product->id }}) {
            quantityInput{{ $product->id }}.addEventListener('input', function() {
                const quantity = parseInt(this.value) || 0;
                const price = {{ $product->price }};
                totalPrice{{ $product->id }}.textContent = (quantity * price).toFixed(2);
            });
        }
        @endforeach
    });
</script>
@endpush
