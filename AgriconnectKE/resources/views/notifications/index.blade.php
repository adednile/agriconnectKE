@extends('layouts.app')

@section('content')
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">Notifications</h1>
    <div>
        <form action="{{ route('notifications.read-all') }}" method="POST" class="d-inline">
            @csrf
            <button type="submit" class="btn btn-success btn-sm">Mark All as Read</button>
        </form>
        <form action="{{ route('notifications.clear-all') }}" method="POST" class="d-inline">
            @csrf
            <button type="submit" class="btn btn-danger btn-sm">Clear All</button>
        </form>
    </div>
</div>

<div class="row">
    <div class="col-12">
        @if($notifications->count() > 0)
            <div class="list-group">
                @foreach($notifications as $notification)
                <div class="list-group-item {{ $notification->is_read ? '' : 'bg-light' }}">
                    <div class="d-flex w-100 justify-content-between">
                        <h6 class="mb-1">{{ $notification->title }}</h6>
                        <small>{{ $notification->created_at->diffForHumans() }}</small>
                    </div>
                    <p class="mb-1">{{ $notification->message }}</p>
                    <div class="mt-2">
                        @if(!$notification->is_read)
                        <form action="{{ route('notifications.read', $notification) }}" method="POST" class="d-inline">
                            @csrf
                            <button type="submit" class="btn btn-sm btn-outline-success">Mark as Read</button>
                        </form>
                        @endif
                        <form action="{{ route('notifications.destroy', $notification) }}" method="POST" class="d-inline">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                        </form>
                    </div>
                </div>
                @endforeach
            </div>
        @else
            <div class="alert alert-info">
                <p>You have no notifications.</p>
            </div>
        @endif
    </div>
</div>
@endsection