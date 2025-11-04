package com.ecommerce.agriconnectke.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findByFarmerId(Long farmerId);
    List<Order> findByOrderStatus(String orderStatus);
    
    @Query("SELECT o FROM Order o WHERE o.farmerId = :farmerId AND o.orderStatus = 'PAID'")
    List<Order> findCompletedOrdersByFarmer(@Param("farmerId") Long farmerId);
    
    @Query("SELECT o FROM Order o WHERE o.buyerId = :buyerId AND o.orderStatus = :status")
    List<Order> findByBuyerIdAndOrderStatus(@Param("buyerId") Long buyerId, @Param("status") String status);
    
    @Query("SELECT o FROM Order o WHERE o.farmerId = :farmerId AND o.orderStatus = :status")
    List<Order> findByFarmerIdAndOrderStatus(@Param("farmerId") Long farmerId, @Param("status") String status);
    
    @Query("SELECT o FROM Order o WHERE o.listingId = :listingId")
    List<Order> findByListingId(@Param("listingId") Long listingId);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.farmerId = :farmerId AND o.orderStatus = 'PAID'")
    Double getTotalSalesByFarmer(@Param("farmerId") Long farmerId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.farmerId = :farmerId AND o.orderStatus = 'PAID'")
    Long countCompletedOrdersByFarmer(@Param("farmerId") Long farmerId);
    
    @Query("SELECT o FROM Order o WHERE o.orderStatus IN ('PENDING', 'PAID') AND o.createdAt < :cutoffTime")
    List<Order> findAbandonedOrders(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    Optional<Order> findByOrderIdAndBuyerId(Long orderId, Long buyerId);
    Optional<Order> findByOrderIdAndFarmerId(Long orderId, Long farmerId);
}