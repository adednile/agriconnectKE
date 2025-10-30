package com.ecommerce.agriconnectke.services;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.models.Payment;
import com.ecommerce.agriconnectke.repositories.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    public Payment create(Payment p) { return repo.save(p); }
    public List<Payment> findByOrder(Long orderId) { return repo.findByOrderId(orderId); }
    public List<Payment> findAll() { return repo.findAll(); }
}
