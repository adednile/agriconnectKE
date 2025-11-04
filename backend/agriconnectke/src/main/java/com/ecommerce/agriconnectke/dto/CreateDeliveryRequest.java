package com.ecommerce.agriconnectke.dto;

public class CreateDeliveryRequest {
    private Long orderId;
    private Double pickupLat;
    private Double pickupLon;
    private Double dropoffLat;
    private Double dropoffLon;
    private Double distanceKm;

    // Getters and setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Double getPickupLat() { return pickupLat; }
    public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }

    public Double getPickupLon() { return pickupLon; }
    public void setPickupLon(Double pickupLon) { this.pickupLon = pickupLon; }

    public Double getDropoffLat() { return dropoffLat; }
    public void setDropoffLat(Double dropoffLat) { this.dropoffLat = dropoffLat; }

    public Double getDropoffLon() { return dropoffLon; }
    public void setDropoffLon(Double dropoffLon) { this.dropoffLon = dropoffLon; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
}