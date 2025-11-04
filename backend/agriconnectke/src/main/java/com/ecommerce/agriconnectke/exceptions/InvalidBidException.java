package com.ecommerce.agriconnectke.exceptions;

public class InvalidBidException extends AgriconnectException {
    public InvalidBidException(String message) {
        super("INVALID_BID", message);
    }
}