package com.ecommerce.agriconnectke.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.exceptions.DeliveryNotFoundException;
import com.ecommerce.agriconnectke.exceptions.InvalidStatusException;
import com.ecommerce.agriconnectke.models.Delivery;
import com.ecommerce.agriconnectke.models.DeliveryRate;
import com.ecommerce.agriconnectke.repositories.DeliveryRateRepository;
import com.ecommerce.agriconnectke.repositories.DeliveryRepository;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRateRepository deliveryRateRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, DeliveryRateRepository deliveryRateRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryRateRepository = deliveryRateRepository;
    }

    public Delivery createDelivery(Delivery delivery) {
        // Validate coordinates
        validateCoordinates(delivery.getPickupLat(), delivery.getPickupLon(), "pickup");
        validateCoordinates(delivery.getDropoffLat(), delivery.getDropoffLon(), "dropoff");
        
        // Validate distance - use BigDecimal comparison
        if (delivery.getDistanceKm().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Distance must be positive");
        }

        // Calculate delivery fee
        DeliveryRate currentRate = deliveryRateRepository.findLatestRate()
            .orElseThrow(() -> new RuntimeException("No delivery rates configured"));
        
        // Use BigDecimal arithmetic for calculation
        BigDecimal deliveryFee = currentRate.getBaseFee().add(
            currentRate.getRatePerKm().multiply(delivery.getDistanceKm())
        );
        delivery.setDeliveryFee(deliveryFee);
        
        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> getDriverDeliveries(Long driverId) {
        return deliveryRepository.findByDriverId(driverId);
    }

    public Delivery assignDriver(Long deliveryId, Long driverId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
        
        if (!"PENDING".equals(delivery.getStatus())) {
            throw new InvalidStatusException("Only pending deliveries can be assigned");
        }

        delivery.setDriverId(driverId);
        delivery.setStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());
        
        return deliveryRepository.save(delivery);
    }

    public Delivery updateDeliveryStatus(Long deliveryId, String status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
        
        validateStatusTransition(delivery.getStatus(), status);
        
        delivery.setStatus(status);
        
        // Set timestamps based on status
        if ("PICKED_UP".equals(status) && delivery.getAssignedAt() == null) {
            throw new InvalidStatusException("Delivery must be assigned before being picked up");
        }
        
        if ("DELIVERED".equals(status)) {
            delivery.setCompletedAt(LocalDateTime.now());
        }
        
        return deliveryRepository.save(delivery);
    }

    public Optional<Delivery> getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    public List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findByStatus("PENDING");
    }

    public List<Delivery> getDeliveriesByStatus(String status) {
        return deliveryRepository.findByStatus(status);
    }

    public List<Delivery> getDriverDeliveriesByStatus(Long driverId, String status) {
        return deliveryRepository.findByDriverIdAndStatus(driverId, status);
    }

    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }

    public Delivery updateDeliveryCoordinates(Long deliveryId, Double pickupLat, Double pickupLon, 
                                           Double dropoffLat, Double dropoffLon) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
        
        // Convert Double to BigDecimal for entity
        BigDecimal pickupLatBD = BigDecimal.valueOf(pickupLat);
        BigDecimal pickupLonBD = BigDecimal.valueOf(pickupLon);
        BigDecimal dropoffLatBD = BigDecimal.valueOf(dropoffLat);
        BigDecimal dropoffLonBD = BigDecimal.valueOf(dropoffLon);
        
        validateCoordinates(pickupLatBD, pickupLonBD, "pickup");
        validateCoordinates(dropoffLatBD, dropoffLonBD, "dropoff");
        
        delivery.setPickupLat(pickupLatBD);
        delivery.setPickupLon(pickupLonBD);
        delivery.setDropoffLat(dropoffLatBD);
        delivery.setDropoffLon(dropoffLonBD);
        
        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(Long deliveryId) {
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new DeliveryNotFoundException(deliveryId);
        }
        deliveryRepository.deleteById(deliveryId);
    }

    private void validateCoordinates(BigDecimal lat, BigDecimal lon, String type) {
        if (lat == null || lon == null) {
            throw new IllegalArgumentException(type + " coordinates cannot be null");
        }
        // Use BigDecimal comparison
        if (lat.compareTo(new BigDecimal("-90")) < 0 || lat.compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException(type + " latitude must be between -90 and 90");
        }
        if (lon.compareTo(new BigDecimal("-180")) < 0 || lon.compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException(type + " longitude must be between -180 and 180");
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        // Define valid status transitions
        switch (currentStatus) {
            case "PENDING":
                if (!"ASSIGNED".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidStatusException("Pending delivery can only be assigned or cancelled");
                }
                break;
            case "ASSIGNED":
                if (!"PICKED_UP".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidStatusException("Assigned delivery can only be picked up or cancelled");
                }
                break;
            case "PICKED_UP":
                if (!"IN_TRANSIT".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidStatusException("Picked up delivery can only be in transit or cancelled");
                }
                break;
            case "IN_TRANSIT":
                if (!"DELIVERED".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidStatusException("Delivery in transit can only be delivered or cancelled");
                }
                break;
            case "DELIVERED":
            case "CANCELLED":
                throw new InvalidStatusException("Cannot change status of completed or cancelled delivery");
            default:
                throw new InvalidStatusException("Invalid current status: " + currentStatus);
        }
    }
}