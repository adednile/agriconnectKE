package com.ecommerce.agriconnectke.dto;

import java.time.LocalDate;

public class CreateListingRequest {
    private Long farmerId;
    private String cropName;
    private String grade;
    private Double price;
    private Integer quantity;
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;

    // Constructors
    public CreateListingRequest() {}

    // Getters and setters
    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getAvailabilityStart() { return availabilityStart; }
    public void setAvailabilityStart(LocalDate availabilityStart) { this.availabilityStart = availabilityStart; }

    public LocalDate getAvailabilityEnd() { return availabilityEnd; }
    public void setAvailabilityEnd(LocalDate availabilityEnd) { this.availabilityEnd = availabilityEnd; }
}

