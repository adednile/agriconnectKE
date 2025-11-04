package com.ecommerce.agriconnectke.exceptions;

/**
 * Simple runtime exception for resources not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
