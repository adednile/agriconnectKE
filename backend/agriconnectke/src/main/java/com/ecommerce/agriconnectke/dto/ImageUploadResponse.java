package com.ecommerce.agriconnectke.dto;
public class ImageUploadResponse {
    private boolean success;
    private String message;
    private ListingImageResponse image;

    public ImageUploadResponse(boolean success, String message, ListingImageResponse image) {
        this.success = success;
        this.message = message;
        this.image = image;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ListingImageResponse getImage() { return image; }
    public void setImage(ListingImageResponse image) { this.image = image; }
}