package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.exceptions.InvalidPaymentException;
import com.ecommerce.agriconnectke.exceptions.PaymentNotFoundException;
import com.ecommerce.agriconnectke.models.Payment;
import com.ecommerce.agriconnectke.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final Random random = new Random();

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Transactional
    public Payment processPayment(Payment payment) {
        // Validate payment
        validatePayment(payment);
        
        // Check if order already has a completed payment
        if (paymentRepository.existsByOrderIdAndStatus(payment.getOrderId(), "COMPLETED")) {
            throw new InvalidPaymentException("Order already has a completed payment");
        }

        // Generate transaction reference
        payment.setTransactionRef(generateTransactionRef());
        
        // Note: Removed setMpesaCode call since field doesn't exist

        // Simulate payment processing
        return simulatePaymentProcessing(payment);
    }

    public List<Payment> getOrderPayments(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public Optional<Payment> getCompletedPaymentByOrderId(Long orderId) {
        return paymentRepository.findCompletedPaymentByOrderId(orderId);
    }

    @Transactional
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        
        validateStatusTransition(payment.getStatus(), status);
        
        payment.setStatus(status);
        
        // If payment is completed, update order status
        if ("COMPLETED".equals(status)) {
            orderService.updateOrderStatus(payment.getOrderId(), "PAID");
        }
        
        // If payment is refunded, update order status
        if ("REFUNDED".equals(status)) {
            orderService.updateOrderStatus(payment.getOrderId(), "REFUNDED");
        }
        
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new InvalidPaymentException("Only completed payments can be refunded");
        }

        // Simulate refund processing
        try {
            Thread.sleep(500); // Simulate API call
            payment.setStatus("REFUNDED");
            
            // Update order status
            orderService.updateOrderStatus(payment.getOrderId(), "REFUNDED");
            
            return paymentRepository.save(payment);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvalidPaymentException("Refund processing interrupted");
        }
    }

    public Payment simulateMpesaPayment(String phoneNumber, Double amount, Long orderId, String description) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        // Convert Double to BigDecimal for amount
        payment.setAmount(BigDecimal.valueOf(amount));
        payment.setPaymentMethod("MPESA_SIMULATION");
        // Note: Removed setPhoneNumber and setDescription calls since fields don't exist
        
        return processPayment(payment);
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getPaymentsByPhoneNumber(String phoneNumber) {
        return paymentRepository.findByPhoneNumber(phoneNumber);
    }

    public List<Payment> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public Double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = paymentRepository.getTotalRevenueBetween(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    public Long getCompletedPaymentCount(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.countCompletedPaymentsBetween(startDate, endDate);
    }

    public void expireOldPendingPayments() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(30); // 30 minutes expiry
        List<Payment> expiredPayments = paymentRepository.findExpiredPayments(expiryTime);
        
        for (Payment payment : expiredPayments) {
            payment.setStatus("EXPIRED");
            paymentRepository.save(payment);
        }
    }

    public PaymentStatusSummary getPaymentStatusSummary() {
        List<Payment> allPayments = paymentRepository.findAll();
        
        long initiated = allPayments.stream().filter(p -> "INITIATED".equals(p.getStatus())).count();
        long pending = allPayments.stream().filter(p -> "PENDING".equals(p.getStatus())).count();
        long completed = allPayments.stream().filter(p -> "COMPLETED".equals(p.getStatus())).count();
        long failed = allPayments.stream().filter(p -> "FAILED".equals(p.getStatus())).count();
        long refunded = allPayments.stream().filter(p -> "REFUNDED".equals(p.getStatus())).count();
        
        return new PaymentStatusSummary(initiated, pending, completed, failed, refunded);
    }

    private Payment simulatePaymentProcessing(Payment payment) {
        try {
            // Simulate API call delay
            Thread.sleep(2000);
            
            // Simulate success (85% success rate for demo)
            boolean isSuccess = random.nextDouble() < 0.85;
            
            if (isSuccess) {
                payment.setStatus("COMPLETED");
                // Update order status to PAID
                orderService.updateOrderStatus(payment.getOrderId(), "PAID");
            } else {
                payment.setStatus("FAILED");
            }
            
            return paymentRepository.save(payment);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            payment.setStatus("FAILED");
            return paymentRepository.save(payment);
        }
    }

    private void validatePayment(Payment payment) {
        if (payment.getOrderId() == null) {
            throw new InvalidPaymentException("Order ID is required");
        }
        // Use BigDecimal comparison for amount validation
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Amount must be positive");
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            throw new InvalidPaymentException("Payment method is required");
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "COMPLETED":
                if (!"REFUNDED".equals(newStatus)) {
                    throw new InvalidPaymentException("Completed payments can only be refunded");
                }
                break;
            case "FAILED":
            case "EXPIRED":
            case "CANCELLED":
            case "REFUNDED":
                throw new InvalidPaymentException("Cannot change status of terminal payment state");
        }
    }

    private String generateTransactionRef() {
        return "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateMpesaCode() {
        return "MPE" + String.format("%07d", random.nextInt(10000000));
    }

    // DTO for payment status summary
    public static class PaymentStatusSummary {
        private final long initiated;
        private final long pending;
        private final long completed;
        private final long failed;
        private final long refunded;

        public PaymentStatusSummary(long initiated, long pending, long completed, long failed, long refunded) {
            this.initiated = initiated;
            this.pending = pending;
            this.completed = completed;
            this.failed = failed;
            this.refunded = refunded;
        }

        // Getters
        public long getInitiated() { return initiated; }
        public long getPending() { return pending; }
        public long getCompleted() { return completed; }
        public long getFailed() { return failed; }
        public long getRefunded() { return refunded; }
        public long getTotal() { return initiated + pending + completed + failed + refunded; }
    }
}