package com.ecommerce.agriconnectke.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.models.Payment;
import com.ecommerce.agriconnectke.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public Payment createPayment(@RequestBody Payment p) { return paymentService.create(p); }

    @GetMapping("/order/{orderId}")
    public List<Payment> getByOrder(@PathVariable Long orderId) { return paymentService.findByOrder(orderId); }

    @GetMapping
    public List<Payment> all() { return paymentService.findAll(); }
}
