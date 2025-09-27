package com.ecommerce.agriconnectke.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    private Long farmer_id;
    private Long buyer_id;
    private Double deliver_fee; // from database
    private String status;
    
    // getters & setters
    public Long getOrder_id() { return order_id; }
    public void setOrder_id(Long order_id) { this.order_id = order_id; }

    public Long getFarmer_id() { return farmer_id; }
    public void setFarmer_id(Long farmer_id) { this.farmer_id = farmer_id; }

    public Long getBuyer_id() { return buyer_id; }
    public void setBuyer_id(Long buyer_id) { this.buyer_id = buyer_id; }

    public Double getDeliver_fee() { return deliver_fee; }
    public void setDeliver_fee(Double deliver_fee) { this.deliver_fee = deliver_fee; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
