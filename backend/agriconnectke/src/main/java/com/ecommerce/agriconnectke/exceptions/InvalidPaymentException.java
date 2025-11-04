package com.ecommerce.agriconnectke.exceptions;
public class InvalidPaymentException extends AgriconnectException {
    public InvalidPaymentException(String message) {
        super("INVALID_PAYMENT", message);
    }
}