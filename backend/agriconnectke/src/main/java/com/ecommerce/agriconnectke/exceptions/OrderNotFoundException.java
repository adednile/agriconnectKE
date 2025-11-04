package com.ecommerce.agriconnectke.exceptions;

public class OrderNotFoundException extends AgriconnectException {
    public OrderNotFoundException(Long orderId) {
        super("ORDER_NOT_FOUND", "Order not found with ID: " + orderId);
    }
}