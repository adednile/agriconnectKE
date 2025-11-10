<!-- resources/views/driver/dashboard.blade.php -->
@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Driver Dashboard</h1>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Welcome, Driver!</h5>
                <p class="card-text">This is your driver dashboard. Here you can:</p>
                <ul>
                    <li>View assigned deliveries</li>
                    <li>Update your current location</li>
                    <li>Track delivery progress</li>
                    <li>View delivery history</li>
                </ul>
                <p>Driver functionality will be implemented in the next phase.</p>
            </div>
        </div>
    </div>
</div>
@endsection