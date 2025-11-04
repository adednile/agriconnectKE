package com.ecommerce.agriconnectke.dto;

public class DeliveryCostResponse {
    private Double distanceKm;
    private Double baseFee;
    private Double ratePerKm;
    private Double totalCost;

    public DeliveryCostResponse(Double distanceKm, Double baseFee, Double ratePerKm, Double totalCost) {
        this.distanceKm = distanceKm;
        this.baseFee = baseFee;
        this.ratePerKm = ratePerKm;
        this.totalCost = totalCost;
    }

    // Getters and setters
    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Double getBaseFee() { return baseFee; }
    public void setBaseFee(Double baseFee) { this.baseFee = baseFee; }

    public Double getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(Double ratePerKm) { this.ratePerKm = ratePerKm; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
}