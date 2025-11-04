package com.ecommerce.agriconnectke.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.models.Payment;
import com.ecommerce.agriconnectke.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> processPayment(@RequestBody Payment payment) {
        try {
            Payment processedPayment = paymentService.processPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(processedPayment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/mpesa/simulate")
    public ResponseEntity<?> simulateMpesaPayment(
            @RequestParam String phoneNumber,
            @RequestParam Double amount,
            @RequestParam Long orderId,
            @RequestParam(required = false) String description) {
        try {
            Payment payment = paymentService.simulateMpesaPayment(phoneNumber, amount, orderId, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getOrderPayments(@PathVariable Long orderId) {
        List<Payment> payments = paymentService.getOrderPayments(orderId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{orderId}/completed")
    public ResponseEntity<?> getCompletedPaymentByOrderId(@PathVariable Long orderId) {
        try {
            Payment payment = paymentService.getCompletedPaymentByOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("No completed payment found for order"));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam String status) {
        try {
            Payment payment = paymentService.updatePaymentStatus(paymentId, status);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable Long paymentId) {
        try {
            Payment refundedPayment = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(refundedPayment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<Payment>> getPaymentsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Payment> payments = paymentService.getPaymentsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Double revenue = paymentService.getTotalRevenue(startDate, endDate);
            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<?> getPaymentStatusSummary() {
        try {
            PaymentService.PaymentStatusSummary summary = paymentService.getPaymentStatusSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cleanup/expired")
    public ResponseEntity<String> expireOldPendingPayments() {
        try {
            paymentService.expireOldPendingPayments();
            return ResponseEntity.ok("Expired payments cleaned up successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error cleaning up expired payments: " + e.getMessage());
        }
    }
}