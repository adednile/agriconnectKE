package com.ecommerce.agriconnectke.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.dto.LocationUpdateRequest;
import com.ecommerce.agriconnectke.models.DriverLocation;
import com.ecommerce.agriconnectke.services.DriverLocationService;

@RestController
@RequestMapping("/api/driver-locations")
@CrossOrigin(origins = "*")
public class DriverLocationController {
    private final DriverLocationService driverLocationService;

    public DriverLocationController(DriverLocationService driverLocationService) {
        this.driverLocationService = driverLocationService;
    }

    @PostMapping("/{driverId}")
    public ResponseEntity<?> updateLocation(@PathVariable Long driverId, 
                                          @RequestBody LocationUpdateRequest request) {
        try {
            DriverLocation location = driverLocationService.updateDriverLocation(
                driverId, request.getLatitude(), request.getLongitude());
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverLocation> getDriverLocation(@PathVariable Long driverId) {
        return driverLocationService.getDriverLocation(driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DriverLocation>> getActiveDrivers() {
        List<DriverLocation> activeDrivers = driverLocationService.getActiveDrivers();
        return ResponseEntity.ok(activeDrivers);
    }

    @GetMapping
    public ResponseEntity<List<DriverLocation>> getAllDriverLocations() {
        List<DriverLocation> locations = driverLocationService.getAllDriverLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> findNearbyDrivers(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {
        try {
            List<DriverLocation> nearbyDrivers = driverLocationService.findNearbyDrivers(
                latitude, longitude, radiusKm);
            return ResponseEntity.ok(nearbyDrivers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/nearest")
    public ResponseEntity<?> findNearestDriver(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        try {
            Long nearestDriverId = driverLocationService.findNearestDriver(latitude, longitude);
            if (nearestDriverId != null) {
                return ResponseEntity.ok(nearestDriverId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No active drivers found nearby");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{driverId}/distance")
    public ResponseEntity<?> calculateDistanceToDriver(
            @PathVariable Long driverId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        try {
            Double distance = driverLocationService.calculateDistanceToDriver(
                driverId, latitude, longitude);
            return ResponseEntity.ok(distance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{driverId}/active")
    public ResponseEntity<Boolean> isDriverActive(@PathVariable Long driverId) {
        boolean isActive = driverLocationService.isDriverActive(driverId);
        return ResponseEntity.ok(isActive);
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriverLocation(@PathVariable Long driverId) {
        driverLocationService.deleteDriverLocation(driverId);
        return ResponseEntity.noContent().build();
    }
}