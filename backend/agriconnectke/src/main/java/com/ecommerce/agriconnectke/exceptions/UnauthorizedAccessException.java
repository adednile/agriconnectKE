package com.ecommerce.agriconnectke.exceptions;

public class UnauthorizedAccessException extends AgriconnectException {
    public UnauthorizedAccessException(String message) {
        super("UNAUTHORIZED_ACCESS", message);
    }
}