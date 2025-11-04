package com.ecommerce.agriconnectke.exceptions;

public class InsufficientQuantityException extends AgriconnectException {
    public InsufficientQuantityException(String cropName, int available, int requested) {
        super("INSUFFICIENT_QUANTITY", 
            String.format("Insufficient quantity for %s. Available: %d, Requested: %d", 
                cropName, available, requested));
    }
}