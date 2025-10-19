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
@Table(name="Orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long orderId;

    @Column(name="listing_id", nullable=false)
    private Long listingId;

    @Column(name="buyer_id", nullable=false)
    private Long buyerId;

    @Column(name="farmer_id", nullable=false)
    private Long farmerId;

    @Column(name="quantity", nullable=false)
    private Integer quantity;

    @Column(name="unit_price", nullable=false)
    private Double unitPrice;

    // total_price is computed in DB; do not map as insertable/updatable
    @Column(name="total_price", insertable=false, updatable=false)
    private Double totalPrice;

    @Column(name="deliver_fee")
    private Double deliverFee;

    @Column(name="order_status")
    private String orderStatus;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){ createdAt=LocalDateTime.now(); if (orderStatus==null) orderStatus="PENDING"; }

    // getters & setters
}

