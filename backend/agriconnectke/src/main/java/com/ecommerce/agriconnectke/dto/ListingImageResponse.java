package com.ecommerce.agriconnectke.dto;
import java.time.LocalDateTime;

public class ListingImageResponse {
    private Long imageId;
    private Long listingId;
    private String imageUrl;
    private Boolean isPrimary;
    private LocalDateTime uploadedAt;

    public ListingImageResponse() {}

    // Constructor from entity
    public ListingImageResponse(com.ecommerce.agriconnectke.models.ListingImage image) {
        this.imageId = image.getImageId();
        this.listingId = image.getListingId();
        this.imageUrl = image.getImageUrl();
        this.isPrimary = image.getIsPrimary();
        this.uploadedAt = image.getUploadedAt();
    }

    // Getters and setters
    public Long getImageId() { return imageId; }
    public void setImageId(Long imageId) { this.imageId = imageId; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
