package com.ecommerce.agriconnectke.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="listing_id")
    private Long listingId;

    @Column(name="farmer_id", nullable=false)
    private Long farmerId;

    @Column(name="crop_name", nullable=false)
    private String cropName;

    @Column(name="grade")
    private String grade;

    @Column(name="price", nullable=false)
    private Double price;

    @Column(name="quantity", nullable=false)
    private Integer quantity;

    @Column(name="availability_start")
    private LocalDate availabilityStart;

    @Column(name="availability_end")
    private LocalDate availabilityEnd;

    @Column(name="status")
    private String status;

    @Column(name="created_at")
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDate.now(); if (status==null) status="OPEN"; }

    // getters & setters
}