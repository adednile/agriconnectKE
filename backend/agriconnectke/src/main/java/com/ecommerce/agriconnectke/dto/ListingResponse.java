package com.ecommerce.agriconnectke.dto;

import java.time.LocalDate;

public class ListingResponse {
    private Long listingId;
    private Long farmerId;
    private String farmerName;
    private String cropName;
    private String grade;
    private Double price;
    private Integer quantity;
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;
    private String status;
    private LocalDate createdAt;

    // Constructors, getters, and setters
    public ListingResponse() {}

    // Getters and setters for all fields
    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}