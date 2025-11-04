package com.ecommerce.agriconnectke.exceptions;

public class PaymentProcessingException extends AgriconnectException {
    public PaymentProcessingException(String message) {
        super("PAYMENT_ERROR", message);
    }
}