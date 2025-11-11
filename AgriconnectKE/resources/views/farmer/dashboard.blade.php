<!-- resources/views/layouts/app.blade.php -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm Market</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
    <style>
        .navbar-brand { font-weight: bold; }
        .sidebar { min-height: 100vh; }
        .notification-badge { position: absolute; top: 0; right: 0; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-success">
        <div class="container">
            <a class="navbar-brand" href="/">Farm Market</a>
            <div class="navbar-nav ms-auto">
                @auth
                    <span class="navbar-text me-3">Welcome, {{ Auth::user()->name }}</span>
                    <a class="nav-link" href="{{ route('logout') }}">Logout</a>
                @else
                    <a class="nav-link" href="{{ route('login') }}">Login</a>
                    <a class="nav-link" href="{{ route('register') }}">Register</a>
                @endauth
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            @auth
            <nav class="col-md-3 col-lg-2 d-md-block bg-light sidebar">
                <div class="position-sticky pt-3">
                    @if(Auth::user()->isFarmer())
                        @include('partials.farmer-sidebar')
                    @elseif(Auth::user()->isBuyer())
                        @include('partials.buyer-sidebar')
                    @elseif(Auth::user()->isDriver())
                        @include('partials.driver-sidebar')
                    @elseif(Auth::user()->isAdmin())
                        @include('partials.admin-sidebar')
                    @endif
                </div>
            </nav>
            @endauth

            <main class="@auth col-md-9 ms-sm-auto col-lg-10 px-md-4 @else col-12 @endauth">
                @if(session('success'))
                    <div class="alert alert-success mt-3">{{ session('success') }}</div>
                @endif
                @if(session('error'))
                    <div class="alert alert-danger mt-3">{{ session('error') }}</div>
                @endif
                
                @yield('content')
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
    @stack('scripts')
</body>
</html>