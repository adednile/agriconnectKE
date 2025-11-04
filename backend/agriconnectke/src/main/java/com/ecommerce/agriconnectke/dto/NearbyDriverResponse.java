package com.ecommerce.agriconnectke.dto;

import java.time.LocalDateTime;

public class NearbyDriverResponse {
    private Long driverId;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
    private LocalDateTime lastUpdated;

    public NearbyDriverResponse(Long driverId, Double latitude, Double longitude, 
                              Double distanceKm, LocalDateTime lastUpdated) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
        this.lastUpdated = lastUpdated;
    }

    // Getters and setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}