package com.ecommerce.agriconnectke.exceptions;

public class InvalidOrderException extends AgriconnectException {
    public InvalidOrderException(String message) {
        super("INVALID_ORDER", message);
    }
}