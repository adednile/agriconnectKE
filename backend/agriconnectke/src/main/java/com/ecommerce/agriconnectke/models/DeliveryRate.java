package com.ecommerce.agriconnectke.models;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "DeliveryRates")
public class DeliveryRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @Column(name = "base_fee", nullable = false)
    private Double baseFee;

    @Column(name = "rate_per_km", nullable = false)
    private Double ratePerKm;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @PrePersist
    public void prePersist() {
        if (this.effectiveFrom == null) this.effectiveFrom = LocalDate.now();
    }

    // --- Getters & Setters ---
    public Long getRateId() { return rateId; }
    public void setRateId(Long rateId) { this.rateId = rateId; }

    public Double getBaseFee() { return baseFee; }
    public void setBaseFee(Double baseFee) { this.baseFee = baseFee; }

    public Double getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(Double ratePerKm) { this.ratePerKm = ratePerKm; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
}
