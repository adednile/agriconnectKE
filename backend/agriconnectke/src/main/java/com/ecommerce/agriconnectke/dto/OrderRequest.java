package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class OrderRequest {
    @NotNull(message = "Listing ID is required")
    private Long listingId;

    @NotNull(message = "Buyer ID is required")
    private Long buyerId;

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;

    // Constructors
    public OrderRequest() {}

    public OrderRequest(Long listingId, Long buyerId, Long farmerId, Integer quantity, BigDecimal unitPrice) {
        this.listingId = listingId;
        this.buyerId = buyerId;
        this.farmerId = farmerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}