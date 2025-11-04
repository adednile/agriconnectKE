package com.ecommerce.agriconnectke.exceptions;

// Base custom exception
public class AgriconnectException extends RuntimeException {
    private String errorCode;
    
    public AgriconnectException(String message) {
        super(message);
    }
    
    public AgriconnectException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() { return errorCode; }
    
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
}