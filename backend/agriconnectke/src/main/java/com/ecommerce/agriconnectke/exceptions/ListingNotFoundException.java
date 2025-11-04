package com.ecommerce.agriconnectke.exceptions;

public class ListingNotFoundException extends AgriconnectException {
    public ListingNotFoundException(Long listingId) {
        super("LISTING_NOT_FOUND", "Listing not found with ID: " + listingId);
    }
}