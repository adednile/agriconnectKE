package com.ecommerce.agriconnectke.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ecommerce.agriconnectke.models.DriverLocation;
import com.ecommerce.agriconnectke.services.DriverLocationService;

@Controller
public class DriverLocationWebSocketController {

    private final DriverLocationService driverLocationService;
    private final SimpMessagingTemplate messagingTemplate;

    public DriverLocationWebSocketController(DriverLocationService driverLocationService,
                                           SimpMessagingTemplate messagingTemplate) {
        this.driverLocationService = driverLocationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/update-location")
    @SendTo("/topic/driver-locations")
    public DriverLocation updateDriverLocation(LocationUpdateMessage message) {
        DriverLocation location = driverLocationService.updateDriverLocation(
            message.getDriverId(), message.getLatitude(), message.getLongitude());
        
        // Broadcast to specific delivery topic if needed
        if (message.getDeliveryId() != null) {
            messagingTemplate.convertAndSend("/topic/delivery-" + message.getDeliveryId(), location);
        }
        
        return location;
    }

    public static class LocationUpdateMessage {
        private Long driverId;
        private Long deliveryId;
        private Double latitude;
        private Double longitude;

        // Getters and setters
        public Long getDriverId() { return driverId; }
        public void setDriverId(Long driverId) { this.driverId = driverId; }
        
        public Long getDeliveryId() { return deliveryId; }
        public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}