package com.ecommerce.agriconnectke.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="DriverLocations")
public class DriverLocation {
    @Id
    @Column(name="driver_id")
    private Long driverId;

    @Column(name="latitude", nullable=false)
    private Double latitude;

    @Column(name="longitude", nullable=false)
    private Double longitude;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist @PreUpdate
    public void touch(){ lastUpdated = LocalDateTime.now(); }

    // getters & setters
}