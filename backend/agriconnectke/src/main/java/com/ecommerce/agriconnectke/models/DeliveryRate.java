package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DeliveryRates")
public class DeliveryRate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @Column(name = "base_fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal baseFee;

    @Column(name = "rate_per_km", precision = 10, scale = 2, nullable = false)
    private BigDecimal ratePerKm;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public DeliveryRate() {}

    public DeliveryRate(BigDecimal baseFee, BigDecimal ratePerKm, LocalDate effectiveFrom) {
        this.baseFee = baseFee;
        this.ratePerKm = ratePerKm;
        this.effectiveFrom = effectiveFrom;
    }

    // Getters and Setters
    public Long getRateId() { return rateId; }
    public void setRateId(Long rateId) { this.rateId = rateId; }

    public BigDecimal getBaseFee() { return baseFee; }
    public void setBaseFee(BigDecimal baseFee) { this.baseFee = baseFee; }

    public BigDecimal getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(BigDecimal ratePerKm) { this.ratePerKm = ratePerKm; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    // Business method to calculate delivery cost
    public BigDecimal calculateDeliveryCost(BigDecimal distanceKm) {
        if (distanceKm == null || distanceKm.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Distance must be non-negative");
        }
        // Formula: baseFee + (ratePerKm * distanceKm)
        return baseFee.add(ratePerKm.multiply(distanceKm));
    }

    @Override
    public String toString() {
        return "DeliveryRate{" +
                "rateId=" + rateId +
                ", baseFee=" + baseFee +
                ", ratePerKm=" + ratePerKm +
                ", effectiveFrom=" + effectiveFrom +
                ", isActive=" + isActive +
                '}';
    }
}