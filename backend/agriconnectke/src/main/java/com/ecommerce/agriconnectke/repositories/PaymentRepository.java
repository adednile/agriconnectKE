package com.ecommerce.agriconnectke.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByTransactionRef(String transactionRef);
    List<Payment> findByStatus(String status);
    
    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId AND p.status = 'COMPLETED'")
    Optional<Payment> findCompletedPaymentByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT p FROM Payment p WHERE p.phoneNumber = :phoneNumber ORDER BY p.createdAt DESC")
    List<Payment> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' AND p.createdAt BETWEEN :startDate AND :endDate")
    Double getTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'COMPLETED' AND p.createdAt BETWEEN :startDate AND :endDate")
    Long countCompletedPaymentsBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.createdAt < :expiryTime")
    List<Payment> findExpiredPayments(@Param("expiryTime") LocalDateTime expiryTime);
    
    boolean existsByOrderIdAndStatus(Long orderId, String status);
}