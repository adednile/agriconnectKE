package com.ecommerce.agriconnectke.dto;

public class CreateOrderRequest {
    private Long listingId;
    private Long buyerId;
    private Long farmerId;
    private Integer quantity;
    private Double unitPrice;
    private Double deliverFee;

    // Constructors
    public CreateOrderRequest() {}

    // Getters and setters
    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Double getDeliverFee() { return deliverFee; }
    public void setDeliverFee(Double deliverFee) { this.deliverFee = deliverFee; }
}
