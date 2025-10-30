package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.agriconnectke.models.DeliveryRate;

public interface DeliveryRateRepository extends JpaRepository<DeliveryRate, Long> {
    // Find the most recent rate by effectiveFrom (descending)
    @Query(value = "SELECT TOP 1 dr FROM DeliveryRate dr ORDER BY dr.effectiveFrom DESC")
    DeliveryRate findLatestRate();
}