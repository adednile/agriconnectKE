package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class BidRequest {
    @NotNull(message = "Buyer ID is required")
    private Long buyerId;

    @NotNull(message = "Listing ID is required")
    private Long listingId;

    @NotNull(message = "Offer price is required")
    @Positive(message = "Offer price must be positive")
    private BigDecimal offerPrice;

    // Constructors
    public BidRequest() {}

    public BidRequest(Long buyerId, Long listingId, BigDecimal offerPrice) {
        this.buyerId = buyerId;
        this.listingId = listingId;
        this.offerPrice = offerPrice;
    }

    // Getters and Setters
    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }
}