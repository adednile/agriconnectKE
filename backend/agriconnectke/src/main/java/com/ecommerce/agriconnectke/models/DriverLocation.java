package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DriverLocations")
public class DriverLocation {
    
    @Id
    @Column(name = "driver_id")
    private Long driverId;

    @NotNull
    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @NotNull
    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public DriverLocation() {}

    public DriverLocation(Long driverId, BigDecimal latitude, BigDecimal longitude) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    @Override
    public String toString() {
        return "DriverLocation{" +
                "driverId=" + driverId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}