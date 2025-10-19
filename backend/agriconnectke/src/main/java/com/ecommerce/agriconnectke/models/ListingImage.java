package com.ecommerce.agriconnectke.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="ListingImages")
public class ListingImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long imageId;

    @Column(name="listing_id", nullable=false)
    private Long listingId;

    @Column(name="image_url", nullable=false, length=500)
    private String imageUrl;

    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist(){ uploadedAt = LocalDateTime.now(); }

    // getters & setters
}