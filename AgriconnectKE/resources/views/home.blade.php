@extends('layouts.app')

@section('content')
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-12 text-center">
            <div class="jumbotron bg-light p-5 rounded mt-4">
                <h1 class="display-4 text-success">Welcome to Farm Market</h1>
                <p class="lead">Connect farmers directly with buyers in our agricultural marketplace.</p>
                <hr class="my-4">
                <p>Buy fresh produce directly from farmers or sell your agricultural products.</p>
                <div class="mt-4">
                    <a class="btn btn-success btn-lg me-3" href="{{ route('login') }}">Login</a>
                    <a class="btn btn-outline-success btn-lg" href="{{ route('register') }}">Register</a>
                </div>
            </div>

            <div class="row mt-5">
                <div class="col-md-4 mb-4">
                    <div class="card border-success h-100">
                        <div class="card-body text-center">
                            <h5 class="card-title">For Farmers</h5>
                            <p class="card-text">List your products, receive bids, and sell directly to buyers.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="card border-success h-100">
                        <div class="card-body text-center">
                            <h5 class="card-title">For Buyers</h5>
                            <p class="card-text">Buy fresh produce, place bids, and track your orders.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="card border-success h-100">
                        <div class="card-body text-center">
                            <h5 class="card-title">For Drivers</h5>
                            <p class="card-text">Deliver products and earn money with our logistics system.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection