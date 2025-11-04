package com.ecommerce.agriconnectke.dto;


public class DeliveryAssignmentRequest {
    private Long driverId;

    public DeliveryAssignmentRequest() {}
    
    public DeliveryAssignmentRequest(Long driverId) {
        this.driverId = driverId;
    }

    // Getters and Setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
}