package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.DriverLocation;
public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
        DriverLocation findTopByDriverIdOrderByTimestampDesc(Long driverId);

}
