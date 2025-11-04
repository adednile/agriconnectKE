package com.ecommerce.agriconnectke.dto;

import java.time.LocalDate;

public class CreateDeliveryRateRequest {
    private Double baseFee;
    private Double ratePerKm;
    private LocalDate effectiveFrom;
    private Boolean isActive;

    // Getters and setters
    public Double getBaseFee() { return baseFee; }
    public void setBaseFee(Double baseFee) { this.baseFee = baseFee; }

    public Double getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(Double ratePerKm) { this.ratePerKm = ratePerKm; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
