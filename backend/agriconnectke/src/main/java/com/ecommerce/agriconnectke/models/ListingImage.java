package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "ListingImages")
public class ListingImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @NotNull
    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @NotNull
    @Size(max = 500)
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

        @Column(name = "is_primary")
    private Boolean isPrimary = false; // Add this field

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    // Constructors
    public ListingImage() {}
    public ListingImage(Long listingId, String imageUrl, Boolean isPrimary) {
        this.listingId = listingId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.uploadedAt = LocalDateTime.now();
    }
    // Getters and Setters
    public Long getImageId() { return imageId; }
    public void setImageId(Long imageId) { this.imageId = imageId; }

    // Add these missing methods
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    @Override
    public String toString() {
        return "ListingImage{" +
                "imageId=" + imageId +
                ", listingId=" + listingId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}