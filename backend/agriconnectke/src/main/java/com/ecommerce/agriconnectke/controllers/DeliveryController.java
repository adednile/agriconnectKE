package com.ecommerce.agriconnectke.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.models.Delivery;
import com.ecommerce.agriconnectke.models.DriverLocation;
import com.ecommerce.agriconnectke.services.DeliveryService;
import com.ecommerce.agriconnectke.services.DriverLocationService;
import com.ecommerce.agriconnectke.services.RouteService;

@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(origins = "*")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final RouteService routeService;
    private final DriverLocationService driverLocationService;

    public DeliveryController(DeliveryService deliveryService, RouteService routeService, DriverLocationService driverLocationService) {
        this.deliveryService = deliveryService;
        this.routeService = routeService;
        this.driverLocationService = driverLocationService;
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long deliveryId) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(deliveryId);
            return ResponseEntity.ok(delivery);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody Delivery delivery) {
        try {
            Delivery createdDelivery = deliveryService.createDelivery(delivery);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDelivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Delivery>> getDriverDeliveries(@PathVariable Long driverId) {
        List<Delivery> deliveries = deliveryService.getDriverDeliveries(driverId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Delivery> getDeliveryByOrder(@PathVariable Long orderId) {
        return deliveryService.getDeliveryByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{deliveryId}/assign")
    public ResponseEntity<?> assignDriver(@PathVariable Long deliveryId, @RequestParam Long driverId) {
        try {
            Delivery delivery = deliveryService.assignDriver(deliveryId, driverId);
            return ResponseEntity.ok(delivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable Long deliveryId, @RequestParam String status) {
        try {
            Delivery delivery = deliveryService.updateDeliveryStatus(deliveryId, status);
            return ResponseEntity.ok(delivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Delivery>> getDeliveriesByStatus(@PathVariable String status) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByStatus(status);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/driver/{driverId}/status/{status}")
    public ResponseEntity<List<Delivery>> getDriverDeliveriesByStatus(
            @PathVariable Long driverId, @PathVariable String status) {
        List<Delivery> deliveries = deliveryService.getDriverDeliveriesByStatus(driverId, status);
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/{deliveryId}/coordinates")
    public ResponseEntity<?> updateDeliveryCoordinates(
            @PathVariable Long deliveryId,
            @RequestParam Double pickupLat,
            @RequestParam Double pickupLon,
            @RequestParam Double dropoffLat,
            @RequestParam Double dropoffLon) {
        try {
            Delivery delivery = deliveryService.updateDeliveryCoordinates(
                deliveryId, pickupLat, pickupLon, dropoffLat, dropoffLon);
            return ResponseEntity.ok(delivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long deliveryId) {
        try {
            deliveryService.deleteDelivery(deliveryId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Add these methods to your existing DeliveryController

    @GetMapping("/{deliveryId}/route")
    public ResponseEntity<?> getDeliveryRoute(@PathVariable Long deliveryId) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(deliveryId);
            
            // Convert BigDecimal to Double for route service
            Double pickupLat = convertToDouble(delivery.getPickupLat());
            Double pickupLon = convertToDouble(delivery.getPickupLon());
            Double dropoffLat = convertToDouble(delivery.getDropoffLat());
            Double dropoffLon = convertToDouble(delivery.getDropoffLon());
            
            List<double[]> route = routeService.calculateRoute(
                pickupLat, pickupLon, dropoffLat, dropoffLon
            );
            
            RouteService.RouteInfo routeInfo = routeService.calculateRouteInfo(
                pickupLat, pickupLon, dropoffLat, dropoffLon
            );
            
            // Convert to double arrays for response
            double[] pickupLocationArray = new double[]{pickupLat, pickupLon};
            double[] dropoffLocationArray = new double[]{dropoffLat, dropoffLon};
            
            Map<String, Object> response = new HashMap<>();
            response.put("deliveryId", deliveryId);
            response.put("route", route);
            response.put("distanceKm", routeInfo.getDistanceKm());
            response.put("estimatedTimeMinutes", routeInfo.getEstimatedTimeMinutes());
            response.put("pickupLocation", pickupLocationArray);
            response.put("dropoffLocation", dropoffLocationArray);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{deliveryId}/driver-progress")
    public ResponseEntity<?> getDriverProgress(@PathVariable Long deliveryId) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(deliveryId);
            
            if (delivery.getDriverId() == null) {
                return ResponseEntity.badRequest().body("No driver assigned to this delivery");
            }
            
            Optional<DriverLocation> driverLocation = driverLocationService.getDriverLocation(delivery.getDriverId());
            if (driverLocation.isEmpty()) {
                return ResponseEntity.badRequest().body("Driver location not available");
            }
            
            // Convert BigDecimal to Double for calculations
            Double startLat = convertToDouble(delivery.getPickupLat());
            Double startLon = convertToDouble(delivery.getPickupLon());
            Double endLat = convertToDouble(delivery.getDropoffLat());
            Double endLon = convertToDouble(delivery.getDropoffLon());
            Double currentLat = convertToDouble(driverLocation.get().getLatitude());
            Double currentLon = convertToDouble(driverLocation.get().getLongitude());
            
            // Calculate progress along the route
            Double progress = calculateRouteProgress(
                startLat, startLon, endLat, endLon, currentLat, currentLon
            );
            
            // Convert to double array for response
            double[] currentLocationArray = new double[]{currentLat, currentLon};
            
            Map<String, Object> response = new HashMap<>();
            response.put("deliveryId", deliveryId);
            response.put("driverId", delivery.getDriverId());
            response.put("currentLocation", currentLocationArray);
            response.put("progressPercentage", progress);
            response.put("lastUpdated", driverLocation.get().getLastUpdated());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Double calculateRouteProgress(Double startLat, Double startLon, 
                                        Double endLat, Double endLon,
                                        Double currentLat, Double currentLon) {
        // Simple progress calculation based on distance
        double totalDistance = calculateHaversineDistance(startLat, startLon, endLat, endLon);
        double completedDistance = calculateHaversineDistance(startLat, startLon, currentLat, currentLon);
        
        double progress = (completedDistance / totalDistance) * 100;
        return Math.min(Math.max(progress, 0), 100); // Clamp between 0-100
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    // Utility method to safely convert BigDecimal to Double
    private Double convertToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}