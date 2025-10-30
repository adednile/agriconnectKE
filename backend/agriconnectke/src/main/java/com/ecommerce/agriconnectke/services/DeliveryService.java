package com.ecommerce.agriconnectke.services;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.models.Delivery;
import com.ecommerce.agriconnectke.repositories.DeliveryRepository;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> getDeliveriesByDriver(Long driverId) {
        return deliveryRepository.findByDriverId(driverId);
    }

    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
}