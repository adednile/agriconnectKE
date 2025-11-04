package com.ecommerce.agriconnectke.exceptions;

public class UserNotFoundException extends AgriconnectException {
    public UserNotFoundException(Long userId) {
        super("USER_NOT_FOUND", "User not found with ID: " + userId);
    }
    
    public UserNotFoundException(String phone) {
        super("USER_NOT_FOUND", "User not found with phone: " + phone);
    }
}