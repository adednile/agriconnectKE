package com.ecommerce.agriconnectke.exceptions;

public class InvalidImageException extends AgriconnectException {
    public InvalidImageException(String message) {
        super("INVALID_IMAGE", message);
    }
}