package com.ecommerce.agriconnectke.utils;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public void validateCropPrice(Double price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Crop price must be positive");
        }
    }
    
    public void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}