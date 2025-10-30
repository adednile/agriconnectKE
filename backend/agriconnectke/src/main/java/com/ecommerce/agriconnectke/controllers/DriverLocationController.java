package com.ecommerce.agriconnectke.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public DriverLocation updateLocation(@RequestBody DriverLocation location) {
        return driverLocationService.updateLocation(location);
    }

    @GetMapping("/{driverId}")
    public DriverLocation getLatestLocation(@PathVariable Long driverId) {
        return driverLocationService.getLatestLocation(driverId);
    }
}
