package com.ecommerce.agriconnectke.exceptions;

public class DuplicateResourceException extends AgriconnectException {
    public DuplicateResourceException(String message) {
        super("DUPLICATE_RESOURCE", message);
    }
    
    public DuplicateResourceException(String resource, String value) {
        super("DUPLICATE_RESOURCE", resource + " already exists with value: " + value);
    }

    public DuplicateResourceException(String resource, String field, String value) {
        super("DUPLICATE_RESOURCE", resource + " " + field + " already exists with value: " + value);
    }
}