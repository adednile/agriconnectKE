package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.exceptions.InvalidLocationException;
import com.ecommerce.agriconnectke.models.DriverLocation;
import com.ecommerce.agriconnectke.repositories.DriverLocationRepository;
import com.ecommerce.agriconnectke.utils.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DriverLocationService {
    private final DriverLocationRepository driverLocationRepository;
    private final DistanceCalculator distanceCalculator;

    public DriverLocationService(DriverLocationRepository driverLocationRepository, 
                               DistanceCalculator distanceCalculator) {
        this.driverLocationRepository = driverLocationRepository;
        this.distanceCalculator = distanceCalculator;
    }

    public DriverLocation updateDriverLocation(Long driverId, Double latitude, Double longitude) {
        // Validate coordinates
        validateCoordinates(latitude, longitude);
        
        Optional<DriverLocation> existingLocation = driverLocationRepository.findByDriverId(driverId);
        
        DriverLocation location;
        if (existingLocation.isPresent()) {
            location = existingLocation.get();
            // Convert Double to BigDecimal for entity setters
            location.setLatitude(BigDecimal.valueOf(latitude));
            location.setLongitude(BigDecimal.valueOf(longitude));
        } else {
            location = new DriverLocation();
            location.setDriverId(driverId);
            // Convert Double to BigDecimal for entity setters
            location.setLatitude(BigDecimal.valueOf(latitude));
            location.setLongitude(BigDecimal.valueOf(longitude));
        }
        
        // Timestamp will be automatically set by @PreUpdate
        return driverLocationRepository.save(location);
    }

    public Optional<DriverLocation> getDriverLocation(Long driverId) {
        return driverLocationRepository.findByDriverId(driverId);
    }

    public List<DriverLocation> getActiveDrivers() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10); // Drivers active in last 10 minutes
        return driverLocationRepository.findActiveDrivers(cutoffTime);
    }

    public List<DriverLocation> getDriverLocations(List<Long> driverIds) {
        return driverLocationRepository.findByDriverIdIn(driverIds);
    }

    public List<DriverLocation> getAllDriverLocations() {
        return driverLocationRepository.findAll();
    }

    public void deleteDriverLocation(Long driverId) {
        Optional<DriverLocation> location = driverLocationRepository.findByDriverId(driverId);
        location.ifPresent(driverLocation -> driverLocationRepository.delete(driverLocation));
    }

    public List<DriverLocation> findNearbyDrivers(Double latitude, Double longitude, Double radiusKm) {
        validateCoordinates(latitude, longitude);
        
        if (radiusKm == null || radiusKm <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        List<DriverLocation> activeDrivers = getActiveDrivers();
        
        // Filter drivers within the specified radius
        return activeDrivers.stream()
                .filter(driver -> {
                    // Convert BigDecimal to double for distance calculation
                    double driverLat = driver.getLatitude().doubleValue();
                    double driverLon = driver.getLongitude().doubleValue();
                    double distance = distanceCalculator.calculateDistance(
                        latitude, longitude, driverLat, driverLon);
                    return distance <= radiusKm;
                })
                .toList();
    }

    public Double calculateDistanceToDriver(Long driverId, Double latitude, Double longitude) {
        validateCoordinates(latitude, longitude);
        
        Optional<DriverLocation> driverLocation = driverLocationRepository.findByDriverId(driverId);
        if (driverLocation.isEmpty()) {
            throw new IllegalArgumentException("Driver location not found");
        }

        DriverLocation location = driverLocation.get();
        // Convert BigDecimal to double for distance calculation
        double driverLat = location.getLatitude().doubleValue();
        double driverLon = location.getLongitude().doubleValue();
        return distanceCalculator.calculateDistance(
            latitude, longitude, driverLat, driverLon);
    }

    public Long findNearestDriver(Double latitude, Double longitude) {
        validateCoordinates(latitude, longitude);
        
        List<DriverLocation> activeDrivers = getActiveDrivers();
        if (activeDrivers.isEmpty()) {
            return null;
        }

        Long nearestDriverId = null;
        double minDistance = Double.MAX_VALUE;

        for (DriverLocation driver : activeDrivers) {
            // Convert BigDecimal to double for distance calculation
            double driverLat = driver.getLatitude().doubleValue();
            double driverLon = driver.getLongitude().doubleValue();
            double distance = distanceCalculator.calculateDistance(
                latitude, longitude, driverLat, driverLon);
            
            if (distance < minDistance) {
                minDistance = distance;
                nearestDriverId = driver.getDriverId();
            }
        }

        return nearestDriverId;
    }

    public boolean isDriverActive(Long driverId) {
        Optional<DriverLocation> location = driverLocationRepository.findByDriverId(driverId);
        if (location.isEmpty()) {
            return false;
        }
        
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        return location.get().getLastUpdated().isAfter(cutoffTime);
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new InvalidLocationException("Latitude and longitude cannot be null");
        }
        if (latitude < -90 || latitude > 90) {
            throw new InvalidLocationException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new InvalidLocationException("Longitude must be between -180 and 180");
        }
    }
}