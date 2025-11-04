package com.ecommerce.agriconnectke.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.DriverLocation;

@Repository
public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
    Optional<DriverLocation> findByDriverId(Long driverId);
    
    @Query("SELECT dl FROM DriverLocation dl WHERE dl.lastUpdated >= :cutoffTime")
    List<DriverLocation> findActiveDrivers(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);
    
    List<DriverLocation> findByDriverIdIn(List<Long> driverIds);
    
    // Find drivers who updated their location recently (within minutes)
    @Query("SELECT dl FROM DriverLocation dl WHERE dl.lastUpdated >= :cutoffTime ORDER BY dl.lastUpdated DESC")
    List<DriverLocation> findRecentlyUpdatedDrivers(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);
    
    // Count active drivers
    @Query("SELECT COUNT(dl) FROM DriverLocation dl WHERE dl.lastUpdated >= :cutoffTime")
    Long countActiveDrivers(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);
    
    // Delete locations older than specified time
    @Query("DELETE FROM DriverLocation dl WHERE dl.lastUpdated < :cutoffTime")
    void deleteStaleLocations(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);
}