package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.NotNull;

public class LocationUpdateRequest {
    @NotNull
    private Double latitude;
    
    @NotNull
    private Double longitude;

    // Constructors
    public LocationUpdateRequest() {}

    public LocationUpdateRequest(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}