package com.ecommerce.agriconnectke.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
}