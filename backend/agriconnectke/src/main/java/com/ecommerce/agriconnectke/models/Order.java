package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @NotNull
    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @NotNull
    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @NotNull
    @Column(name = "farmer_id", nullable = false)
    private Long farmerId;

    @NotNull
    @Positive
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Positive
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "deliver_fee", precision = 10, scale = 2)
    private BigDecimal deliverFee;

    @Column(name = "order_status", length = 20)
    private String orderStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // ADD THIS FIELD

    public enum OrderStatus {
        PENDING, PAID, ASSIGNED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now(); // ADD THIS
        if (orderStatus == null) {
            orderStatus = OrderStatus.PENDING.name();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // ADD THIS
    }

    // Constructors
    public Order() {}

    public Order(Long listingId, Long buyerId, Long farmerId, Integer quantity, BigDecimal unitPrice) {
        this.listingId = listingId;
        this.buyerId = buyerId;
        this.farmerId = farmerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

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

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BigDecimal getDeliverFee() { return deliverFee; }
    public void setDeliverFee(BigDecimal deliverFee) { this.deliverFee = deliverFee; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; } // ADD THIS SETTER

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", listingId=" + listingId +
                ", buyerId=" + buyerId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}