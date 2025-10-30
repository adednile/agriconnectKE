package com.ecommerce.agriconnectke.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.Delivery;
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
        List<Delivery> findByDriverId(Long driverId);
    List<Delivery> findByOrderId(Long orderId);
}
