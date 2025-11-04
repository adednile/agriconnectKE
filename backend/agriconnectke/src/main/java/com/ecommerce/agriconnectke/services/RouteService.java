package com.ecommerce.agriconnectke.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RouteService {
    
    @Value("${routing.service.url:https://router.project-osrm.org/route/v1/driving/}")
    private String routingServiceUrl;
    
    private final RestTemplate restTemplate;
    
    public RouteService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Calculate route between two points using OSRM (Open Source Routing Machine)
     */
    public List<double[]> calculateRoute(Double startLat, Double startLon, Double endLat, Double endLon) {
        try {
            String url = String.format("%s%s,%s;%s,%s?overview=full&geometries=geojson",
                routingServiceUrl, startLon, startLat, endLon, endLat);
            
            Map response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && "Ok".equals(response.get("code"))) {
                List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
                if (!routes.isEmpty()) {
                    Map<String, Object> geometry = (Map<String, Object>) routes.get(0).get("geometry");
                    List<List<Double>> coordinates = (List<List<Double>>) geometry.get("coordinates");
                    
                    // Convert to [lat, lon] format for Leaflet
                    List<double[]> routePoints = new ArrayList<>();
                    for (List<Double> coord : coordinates) {
                        // OSRM returns [lon, lat], Leaflet needs [lat, lon]
                        routePoints.add(new double[]{coord.get(1), coord.get(0)});
                    }
                    return routePoints;
                }
            }
        } catch (Exception e) {
            // Fallback to straight line if routing service fails
            System.err.println("Routing service error: " + e.getMessage());
        }
        
        // Fallback: return straight line between points
        List<double[]> fallbackRoute = new ArrayList<>();
        fallbackRoute.add(new double[]{startLat, startLon});
        fallbackRoute.add(new double[]{endLat, endLon});
        return fallbackRoute;
    }
    
    /**
     * Calculate distance and estimated time for a route
     */
    public RouteInfo calculateRouteInfo(Double startLat, Double startLon, Double endLat, Double endLon) {
        try {
            String url = String.format("%s%s,%s;%s,%s",
                routingServiceUrl, startLon, startLat, endLon, endLat);
            
            Map response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && "Ok".equals(response.get("code"))) {
                List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
                if (!routes.isEmpty()) {
                    Map<String, Object> route = routes.get(0);
                    Double distance = (Double) route.get("distance"); // in meters
                    Double duration = (Double) route.get("duration"); // in seconds
                    
                    return new RouteInfo(
                        distance != null ? distance / 1000.0 : null, // convert to km
                        duration != null ? duration / 60.0 : null    // convert to minutes
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Route info calculation error: " + e.getMessage());
        }
        
        // Fallback calculation using Haversine formula
        double distance = calculateHaversineDistance(startLat, startLon, endLat, endLon);
        double estimatedTime = distance * 2.5; // Rough estimate: 2.5 minutes per km
        
        return new RouteInfo(distance, estimatedTime);
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
    
    public static class RouteInfo {
        private final Double distanceKm;
        private final Double estimatedTimeMinutes;
        
        public RouteInfo(Double distanceKm, Double estimatedTimeMinutes) {
            this.distanceKm = distanceKm;
            this.estimatedTimeMinutes = estimatedTimeMinutes;
        }
        
        // Getters
        public Double getDistanceKm() { return distanceKm; }
        public Double getEstimatedTimeMinutes() { return estimatedTimeMinutes; }
    }
}