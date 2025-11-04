package com.ecommerce.agriconnectke.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.DeliveryRate;

@Repository
public interface DeliveryRateRepository extends JpaRepository<DeliveryRate, Long> {
    
    // Find current active rates (effective today or earlier, ordered by most recent)
    @Query("SELECT dr FROM DeliveryRate dr WHERE dr.effectiveFrom <= CURRENT_DATE AND dr.isActive = true ORDER BY dr.effectiveFrom DESC")
    List<DeliveryRate> findCurrentRates();
    
    // Find the latest active rate
    @Query("SELECT dr FROM DeliveryRate dr WHERE dr.effectiveFrom <= CURRENT_DATE AND dr.isActive = true ORDER BY dr.effectiveFrom DESC LIMIT 1")
    Optional<DeliveryRate> findLatestRate();
    
    // Find rates effective from a specific date
    @Query("SELECT dr FROM DeliveryRate dr WHERE dr.effectiveFrom = :date AND dr.isActive = true")
    Optional<DeliveryRate> findByEffectiveFrom(@Param("date") java.time.LocalDate date);
    
    // Find all active rates
    List<DeliveryRate> findByIsActiveTrue();
    
    // Find all inactive rates
    List<DeliveryRate> findByIsActiveFalse();
    
    // Find rates that will become effective in the future
    @Query("SELECT dr FROM DeliveryRate dr WHERE dr.effectiveFrom > CURRENT_DATE AND dr.isActive = true ORDER BY dr.effectiveFrom ASC")
    List<DeliveryRate> findFutureRates();
}