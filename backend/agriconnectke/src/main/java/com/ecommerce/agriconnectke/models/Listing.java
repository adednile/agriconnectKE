package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Listings")
public class Listing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id")
    private Long listingId;

    @NotNull
    @Column(name = "farmer_id", nullable = false)
    private Long farmerId;

    @NotNull
    @Size(max = 100)
    @Column(name = "crop_name", nullable = false, length = 100)
    private String cropName;

    @Size(max = 50)
    @Column(name = "grade", length = 50)
    private String grade;

    @NotNull
    @Positive
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Positive
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "availability_start")
    private LocalDate availabilityStart;

    @Column(name = "availability_end")
    private LocalDate availabilityEnd;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum ListingStatus {
        OPEN, BOOKED, DELIVERED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = ListingStatus.OPEN.name();
        }
    }

    // Constructors
    public Listing() {}

    public Listing(Long farmerId, String cropName, BigDecimal price, Integer quantity) {
        this.farmerId = farmerId;
        this.cropName = cropName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Listing{" +
                "listingId=" + listingId +
                ", cropName='" + cropName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}