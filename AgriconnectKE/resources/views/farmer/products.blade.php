<!-- resources/views/farmer/products.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">My Products</h1>
    <a href="{{ route('farmer.products.create') }}" class="btn btn-success">Add New Product</a>
</div>

<div class="row">
    <div class="col-12">
        @if($products->count() > 0)
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Category</th>
                            <th>Status</th>
                            <th>Accepts Bids</th>
                            <th>Date Added</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($products as $product)
                        <tr>
                            <td>{{ $product->name }}</td>
                            <td>Ksh {{ number_format($product->price, 2) }}</td>
                            <td>{{ $product->quantity }}</td>
                            <td>{{ ucfirst($product->category) }}</td>
                            <td>
                                <span class="badge bg-{{ $product->is_available ? 'success' : 'danger' }}">
                                    {{ $product->is_available ? 'Available' : 'Sold Out' }}
                                </span>
                            </td>
                            <td>
                                <span class="badge bg-{{ $product->accepts_bids ? 'warning' : 'secondary' }}">
                                    {{ $product->accepts_bids ? 'Yes' : 'No' }}
                                </span>
                            </td>
                            <td>{{ $product->created_at->format('M d, Y') }}</td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
        @else
            <div class="alert alert-info">
                <p>You haven't listed any products yet. <a href="{{ route('farmer.products.create') }}">Add your first product</a> to start selling.</p>
            </div>
        @endif
    </div>
</div>
@endsection