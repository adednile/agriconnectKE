package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Deliveries")
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "driver_id")
    private Long driverId;

    @NotNull
    @Column(name = "pickup_lat", nullable = false, precision = 9, scale = 6)
    private BigDecimal pickupLat;

    @NotNull
    @Column(name = "pickup_lon", nullable = false, precision = 9, scale = 6)
    private BigDecimal pickupLon;

    @NotNull
    @Column(name = "dropoff_lat", nullable = false, precision = 9, scale = 6)
    private BigDecimal dropoffLat;

    @NotNull
    @Column(name = "dropoff_lon", nullable = false, precision = 9, scale = 6)
    private BigDecimal dropoffLon;

    @NotNull
    @Positive
    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public enum DeliveryStatus {
        PENDING, ASSIGNED, PICKED_UP, IN_TRANSIT, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = DeliveryStatus.PENDING.name();
        }
    }

    // Constructors
    public Delivery() {}

    public Delivery(Long orderId, BigDecimal pickupLat, BigDecimal pickupLon, 
                   BigDecimal dropoffLat, BigDecimal dropoffLon, BigDecimal distanceKm) {
        this.orderId = orderId;
        this.pickupLat = pickupLat;
        this.pickupLon = pickupLon;
        this.dropoffLat = dropoffLat;
        this.dropoffLon = dropoffLon;
        this.distanceKm = distanceKm;
    }

    // Getters and Setters
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

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

    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", orderId=" + orderId +
                ", driverId=" + driverId +
                ", distanceKm=" + distanceKm +
                ", deliveryFee=" + deliveryFee +
                ", status='" + status + '\'' +
                '}';
    }
}