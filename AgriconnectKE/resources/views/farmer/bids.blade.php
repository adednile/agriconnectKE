<!-- resources/views/farmer/bids.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Product Bids</h1>
</div>

<div class="row">
    <div class="col-12">
        @if($bids->count() > 0)
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Buyer</th>
                            <th>Buyer Phone</th>
                            <th>Bid Amount</th>
                            <th>Product Price</th>
                            <th>Status</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($bids as $bid)
                        <tr>
                            <td>{{ $bid->product->name }}</td>
                            <td>{{ $bid->buyer->name }}</td>
                            <td>{{ $bid->buyer->phone }}</td>
                            <td>Ksh {{ number_format($bid->amount, 2) }}</td>
                            <td>Ksh {{ number_format($bid->product->price, 2) }}</td>
                            <td>
                                <span class="badge bg-{{ $bid->status == 'pending' ? 'warning' : ($bid->status == 'accepted' ? 'success' : 'danger') }}">
                                    {{ ucfirst($bid->status) }}
                                </span>
                            </td>
                            <td>{{ $bid->created_at->format('M d, Y H:i') }}</td>
                            <td>
                                @if($bid->status == 'pending')
                                <div class="btn-group btn-group-sm">
                                    <form action="{{ route('farmer.bids.status', $bid) }}" method="POST" class="d-inline">
                                        @csrf
                                        <input type="hidden" name="status" value="accepted">
                                        <button type="submit" class="btn btn-success" onclick="return confirm('Accept this bid?')">Accept</button>
                                    </form>
                                    <form action="{{ route('farmer.bids.status', $bid) }}" method="POST" class="d-inline">
                                        @csrf
                                        <input type="hidden" name="status" value="rejected">
                                        <button type="submit" class="btn btn-danger" onclick="return confirm('Reject this bid?')">Reject</button>
                                    </form>
                                </div>
                                @else
                                <span class="text-muted">Action taken</span>
                                @endif
                            </td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
        @else
            <div class="alert alert-info">
                <p>No bids have been placed on your products yet.</p>
            </div>
        @endif
    </div>
</div>
@endsection