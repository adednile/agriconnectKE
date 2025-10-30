package com.ecommerce.agriconnectke.services;
import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.models.DriverLocation;
import com.ecommerce.agriconnectke.repositories.DriverLocationRepository;

@Service
public class DriverLocationService {
    private final DriverLocationRepository driverLocationRepository;

    public DriverLocationService(DriverLocationRepository driverLocationRepository) {
        this.driverLocationRepository = driverLocationRepository;
    }

    public DriverLocation updateLocation(DriverLocation location) {
        return driverLocationRepository.save(location);
    }

    public DriverLocation getLatestLocation(Long driverId) {
        return driverLocationRepository.findTopByDriverIdOrderByTimestampDesc(driverId);
    }
}
