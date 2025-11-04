package com.ecommerce.agriconnectke.exceptions;

public class InvalidStatusException extends AgriconnectException {
    public InvalidStatusException(String message) {
        super("INVALID_STATUS", message);
    }
}