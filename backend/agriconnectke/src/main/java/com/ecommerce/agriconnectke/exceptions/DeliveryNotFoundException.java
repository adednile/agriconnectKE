package com.ecommerce.agriconnectke.exceptions;

public class DeliveryNotFoundException extends AgriconnectException {
    public DeliveryNotFoundException(Long deliveryId) {
        super("DELIVERY_NOT_FOUND", "Delivery not found with ID: " + deliveryId);
    }
}