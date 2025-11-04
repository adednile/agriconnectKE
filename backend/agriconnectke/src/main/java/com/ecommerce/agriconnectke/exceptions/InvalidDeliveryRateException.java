package com.ecommerce.agriconnectke.exceptions;

public class InvalidDeliveryRateException extends AgriconnectException {
    public InvalidDeliveryRateException(String message) {
        super("INVALID_DELIVERY_RATE", message);
    }
}