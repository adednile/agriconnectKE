<!-- resources/views/buyer/bids.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">My Bids</h1>
</div>

<div class="row">
    <div class="col-12">
        @if($bids->count() > 0)
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Farmer</th>
                            <th>My Bid Amount</th>
                            <th>Product Price</th>
                            <th>Status</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($bids as $bid)
                        <tr>
                            <td>{{ $bid->product->name }}</td>
                            <td>{{ $bid->product->farmer->name }}</td>
                            <td>Ksh {{ number_format($bid->amount, 2) }}</td>
                            <td>Ksh {{ number_format($bid->product->price, 2) }}</td>
                            <td>
                                <span class="badge bg-{{ $bid->status == 'pending' ? 'warning' : ($bid->status == 'accepted' ? 'success' : 'danger') }}">
                                    {{ ucfirst($bid->status) }}
                                </span>
                            </td>
                            <td>{{ $bid->created_at->format('M d, Y H:i') }}</td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
        @else
            <div class="alert alert-info">
                <p>You haven't placed any bids yet. <a href="{{ route('buyer.market') }}">Browse the marketplace</a> to place your first bid.</p>
            </div>
        @endif
    </div>
</div>
@endsection
