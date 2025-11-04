package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bids")
public class Bid {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long bidId;

    @NotNull
    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @NotNull
    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @NotNull
    @Positive
    @Column(name = "offer_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal offerPrice;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum BidStatus {
        PENDING, ACCEPTED, REJECTED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = BidStatus.PENDING.name();
        }
    }

    // Constructors
    public Bid() {}

    public Bid(Long buyerId, Long listingId, BigDecimal offerPrice) {
        this.buyerId = buyerId;
        this.listingId = listingId;
        this.offerPrice = offerPrice;
    }

    // Getters and Setters
    public Long getBidId() { return bidId; }
    public void setBidId(Long bidId) { this.bidId = bidId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Bid{" +
                "bidId=" + bidId +
                ", buyerId=" + buyerId +
                ", listingId=" + listingId +
                ", offerPrice=" + offerPrice +
                ", status='" + status + '\'' +
                '}';
    }
}