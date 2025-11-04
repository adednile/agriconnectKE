package com.ecommerce.agriconnectke.dto;

import java.time.LocalDateTime;

public class BidResponse {
    private Long bidId;
    private Long buyerId;
    private String buyerName;
    private Long listingId;
    private String cropName;
    private Double offerPrice;
    private String status;
    private LocalDateTime createdAt;

    // Constructors, Getters, and Setters
    public BidResponse() {}

    // Getters and Setters
    public Long getBidId() { return bidId; }
    public void setBidId(Long bidId) { this.bidId = bidId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public Double getOfferPrice() { return offerPrice; }
    public void setOfferPrice(Double offerPrice) { this.offerPrice = offerPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}