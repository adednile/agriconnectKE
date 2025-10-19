package com.ecommerce.agriconnectke.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Deliveries")
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_id")
    private Long deliveryId;

    @Column(name="order_id", nullable=false)
    private Long orderId;

    @Column(name="driver_id")
    private Long driverId;

    @Column(name="pickup_lat", nullable=false)
    private Double pickupLat;

    @Column(name="pickup_lon", nullable=false)
    private Double pickupLon;

    @Column(name="dropoff_lat", nullable=false)
    private Double dropoffLat;

    @Column(name="dropoff_lon", nullable=false)
    private Double dropoffLon;

    @Column(name="distance_km", nullable=false)
    private Double distanceKm;

    @Column(name="delivery_fee")
    private Double deliveryFee;

    @Column(name="status")
    private String status;

    @Column(name="assigned_at")
    private LocalDateTime assignedAt;

    @Column(name="completed_at")
    private LocalDateTime completedAt;

    // getters & setters
}