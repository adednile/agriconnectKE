package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ListingRequest {
    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotBlank(message = "Crop name is required")
    private String cropName;

    private String grade;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;

    // Constructors
    public ListingRequest() {}

    public ListingRequest(Long farmerId, String cropName, BigDecimal price, Integer quantity) {
        this.farmerId = farmerId;
        this.cropName = cropName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getAvailabilityStart() { return availabilityStart; }
    public void setAvailabilityStart(LocalDate availabilityStart) { this.availabilityStart = availabilityStart; }

    public LocalDate getAvailabilityEnd() { return availabilityEnd; }
    public void setAvailabilityEnd(LocalDate availabilityEnd) { this.availabilityEnd = availabilityEnd; }
}