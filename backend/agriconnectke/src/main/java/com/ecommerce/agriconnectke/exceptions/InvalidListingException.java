package com.ecommerce.agriconnectke.exceptions;


public class InvalidListingException extends AgriconnectException {
    public InvalidListingException(String message) {
        super("INVALID_LISTING", message);
    }
    
    public InvalidListingException(Long listingId, String message) {
        super("INVALID_LISTING", "Listing ID " + listingId + ": " + message);
    }
    
    public InvalidListingException(String cropName, String message) {
        super("INVALID_LISTING", "Crop '" + cropName + "': " + message);
    }
}