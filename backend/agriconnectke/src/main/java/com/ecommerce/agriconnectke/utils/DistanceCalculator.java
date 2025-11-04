package com.ecommerce.agriconnectke.utils;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculate distance between two points using Haversine formula
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Calculate distance with different units
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double distanceKm = calculateDistance(lat1, lon1, lat2, lon2);
        
        return switch (unit.toUpperCase()) {
            case "MILES" -> distanceKm * 0.621371;
            case "METERS" -> distanceKm * 1000;
            default -> distanceKm; // KM
        };
    }

    /**
     * Calculate estimated travel time in minutes
     */
    public double calculateTravelTime(double distanceKm, double averageSpeedKmph) {
        if (averageSpeedKmph <= 0) {
            averageSpeedKmph = 40.0; // Default average speed in km/h
        }
        return (distanceKm / averageSpeedKmph) * 60; // Convert to minutes
    }

    /**
     * Calculate delivery fee based on distance and rates
     */
    public double calculateDeliveryFee(double distanceKm, double baseFee, double ratePerKm) {
        return baseFee + (distanceKm * ratePerKm);
    }

    /**
     * Check if two points are within a certain radius
     */
    public boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        return calculateDistance(lat1, lon1, lat2, lon2) <= radiusKm;
    }
}