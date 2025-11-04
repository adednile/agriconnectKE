package com.ecommerce.agriconnectke.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByDriverId(Long driverId);
    List<Delivery> findByStatus(String status);
    Optional<Delivery> findByOrderId(Long orderId);
    
    List<Delivery> findByDriverIdAndStatus(Long driverId, String status);
    
    // Find deliveries that need driver assignment
    @Query("SELECT d FROM Delivery d WHERE d.status = 'PENDING' AND d.driverId IS NULL")
    List<Delivery> findUnassignedDeliveries();
    
    // Find active deliveries for a driver (not completed or cancelled)
    @Query("SELECT d FROM Delivery d WHERE d.driverId = ?1 AND d.status NOT IN ('DELIVERED', 'CANCELLED')")
    List<Delivery> findActiveDeliveriesByDriver(Long driverId);
    
    // Count deliveries by status
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = ?1")
    Long countByStatus(String status);
    
    // Find deliveries within a date range
    @Query("SELECT d FROM Delivery d WHERE d.createdAt BETWEEN ?1 AND ?2")
    List<Delivery> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}