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
@Table(name="Bids")
public class Bid {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bid_id")
    private Long bidId;

    @Column(name="buyer_id", nullable=false)
    private Long buyerId;

    @Column(name="listing_id", nullable=false)
    private Long listingId;

    @Column(name="offer_price", nullable=false)
    private Double offerPrice;

    @Column(name="status")
    private String status;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){ createdAt=LocalDateTime.now(); if (status==null) status="PENDING"; }

    // getters & setters
}