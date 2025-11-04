package com.ecommerce.agriconnectke.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @NotNull
    @Positive
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 50)
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Size(max = 100)
    @Column(name = "transaction_ref", length = 100)
    private String transactionRef;

    // ADD THESE MISSING FIELDS
    @Size(max = 20)
    @Column(name = "mpesa_code", length = 20)
    private String mpesaCode;

    @Size(max = 15)
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum PaymentStatus {
        INITIATED, COMPLETED, FAILED, REFUNDED
    }

    public enum PaymentMethod {
        MPESA_SIMULATION, MPESA_EXPRESS, CARD, BANK_TRANSFER, CASH_ON_DELIVERY
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.MPESA_SIMULATION.name();
        }
        if (status == null) {
            status = PaymentStatus.INITIATED.name();
        }
    }

    // Constructors
    public Payment() {}

    public Payment(Long orderId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    // ADD THESE MISSING GETTERS AND SETTERS
    public String getMpesaCode() { return mpesaCode; }
    public void setMpesaCode(String mpesaCode) { this.mpesaCode = mpesaCode; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}