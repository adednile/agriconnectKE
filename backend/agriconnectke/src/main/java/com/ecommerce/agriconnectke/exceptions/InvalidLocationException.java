package com.ecommerce.agriconnectke.exceptions;

public class InvalidLocationException extends AgriconnectException {
    public InvalidLocationException(String message) {
        super("INVALID_LOCATION", message);
    }
}