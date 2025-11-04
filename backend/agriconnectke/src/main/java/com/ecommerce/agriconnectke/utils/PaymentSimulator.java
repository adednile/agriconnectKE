package com.ecommerce.agriconnectke.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class PaymentSimulator {
    
    private final Random random = new Random();
    
    /**
     * Simulate MPESA payment processing
     */
    public PaymentResult simulateMpesaPayment(String phoneNumber, Double amount) {
        try {
            // Simulate API call delay
            Thread.sleep(2000);
            
            // Simulate success (90% success rate for demo)
            boolean isSuccess = random.nextDouble() < 0.9;
            
            if (isSuccess) {
                return new PaymentResult(
                    true,
                    "MPESA_" + generateTransactionId(),
                    "Payment processed successfully",
                    amount
                );
            } else {
                return new PaymentResult(
                    false,
                    null,
                    "Payment failed: Insufficient funds",
                    amount
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new PaymentResult(false, null, "Payment processing interrupted", amount);
        }
    }
    
    private String generateTransactionId() {
        return String.format("AG%08d", random.nextInt(100000000));
    }
    
    public static class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String message;
        private final Double amount;
        
        public PaymentResult(boolean success, String transactionId, String message, Double amount) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
            this.amount = amount;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getTransactionId() { return transactionId; }
        public String getMessage() { return message; }
        public Double getAmount() { return amount; }
    }
}