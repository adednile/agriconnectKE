package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class DeliveryRequest {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Pickup latitude is required")
    @DecimalMin(value = "-90.0", message = "Pickup latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Pickup latitude must be between -90 and 90")
    private BigDecimal pickupLat;

    @NotNull(message = "Pickup longitude is required")
    @DecimalMin(value = "-180.0", message = "Pickup longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Pickup longitude must be between -180 and 180")
    private BigDecimal pickupLon;

    @NotNull(message = "Dropoff latitude is required")
    @DecimalMin(value = "-90.0", message = "Dropoff latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Dropoff latitude must be between -90 and 90")
    private BigDecimal dropoffLat;

    @NotNull(message = "Dropoff longitude is required")
    @DecimalMin(value = "-180.0", message = "Dropoff longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Dropoff longitude must be between -180 and 180")
    private BigDecimal dropoffLon;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be positive")
    private BigDecimal distanceKm;

    // Constructors
    public DeliveryRequest() {}

    public DeliveryRequest(Long orderId, BigDecimal pickupLat, BigDecimal pickupLon, 
                         BigDecimal dropoffLat, BigDecimal dropoffLon, BigDecimal distanceKm) {
        this.orderId = orderId;
        this.pickupLat = pickupLat;
        this.pickupLon = pickupLon;
        this.dropoffLat = dropoffLat;
        this.dropoffLon = dropoffLon;
        this.distanceKm = distanceKm;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getPickupLat() { return pickupLat; }
    public void setPickupLat(BigDecimal pickupLat) { this.pickupLat = pickupLat; }

    public BigDecimal getPickupLon() { return pickupLon; }
    public void setPickupLon(BigDecimal pickupLon) { this.pickupLon = pickupLon; }

    public BigDecimal getDropoffLat() { return dropoffLat; }
    public void setDropoffLat(BigDecimal dropoffLat) { this.dropoffLat = dropoffLat; }

    public BigDecimal getDropoffLon() { return dropoffLon; }
    public void setDropoffLon(BigDecimal dropoffLon) { this.dropoffLon = dropoffLon; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
}