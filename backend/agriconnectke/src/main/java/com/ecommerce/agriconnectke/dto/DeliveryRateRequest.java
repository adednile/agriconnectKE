package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DeliveryRateRequest {
    @NotNull(message = "Base fee is required")
    @PositiveOrZero(message = "Base fee must be non-negative")
    private BigDecimal baseFee;

    @NotNull(message = "Rate per km is required")
    @PositiveOrZero(message = "Rate per km must be non-negative")
    private BigDecimal ratePerKm;

    private LocalDate effectiveFrom;
    private Boolean isActive;

    // Constructors
    public DeliveryRateRequest() {}

    public DeliveryRateRequest(BigDecimal baseFee, BigDecimal ratePerKm) {
        this.baseFee = baseFee;
        this.ratePerKm = ratePerKm;
    }

    // Getters and Setters
    public BigDecimal getBaseFee() { return baseFee; }
    public void setBaseFee(BigDecimal baseFee) { this.baseFee = baseFee; }

    public BigDecimal getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(BigDecimal ratePerKm) { this.ratePerKm = ratePerKm; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}