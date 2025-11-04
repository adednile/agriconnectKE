package com.ecommerce.agriconnectke.exceptions;

public class ListingImageNotFoundException extends AgriconnectException {
    public ListingImageNotFoundException(Long imageId) {
        super("LISTING_IMAGE_NOT_FOUND", "Listing image not found with ID: " + imageId);
    }
}