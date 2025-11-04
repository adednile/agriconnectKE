package com.ecommerce.agriconnectke.exceptions;

public class PaymentNotFoundException extends AgriconnectException {
    public PaymentNotFoundException(Long paymentId) {
        super("PAYMENT_NOT_FOUND", "Payment not found with ID: " + paymentId);
    }
}