package com.ecommerce.agriconnectke.controllers;

import java.util.List;

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

import com.ecommerce.agriconnectke.models.DeliveryRate;
import com.ecommerce.agriconnectke.services.DeliveryRateService;

@RestController
@RequestMapping("/api/delivery-rates")
@CrossOrigin(origins = "*")
public class DeliveryRateController {
    private final DeliveryRateService deliveryRateService;

    public DeliveryRateController(DeliveryRateService deliveryRateService) {
        this.deliveryRateService = deliveryRateService;
    }

    @PostMapping
    public ResponseEntity<?> createDeliveryRate(@RequestBody DeliveryRate deliveryRate) {
        try {
            DeliveryRate createdRate = deliveryRateService.createDeliveryRate(deliveryRate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DeliveryRate>> getAllDeliveryRates() {
        List<DeliveryRate> rates = deliveryRateService.getAllDeliveryRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestRate() {
        try {
            DeliveryRate latestRate = deliveryRateService.getLatestRate();
            return ResponseEntity.ok(latestRate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<DeliveryRate>> getCurrentRates() {
        List<DeliveryRate> rates = deliveryRateService.getCurrentRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DeliveryRate>> getActiveRates() {
        List<DeliveryRate> rates = deliveryRateService.getActiveRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<DeliveryRate>> getInactiveRates() {
        List<DeliveryRate> rates = deliveryRateService.getInactiveRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/future")
    public ResponseEntity<List<DeliveryRate>> getFutureRates() {
        List<DeliveryRate> rates = deliveryRateService.getFutureRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/{rateId}")
    public ResponseEntity<?> getDeliveryRateById(@PathVariable Long rateId) {
        try {
            DeliveryRate rate = deliveryRateService.getDeliveryRateById(rateId);
            return ResponseEntity.ok(rate);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{rateId}")
    public ResponseEntity<?> updateDeliveryRate(@PathVariable Long rateId, @RequestBody DeliveryRate deliveryRate) {
        try {
            DeliveryRate updatedRate = deliveryRateService.updateDeliveryRate(rateId, deliveryRate);
            return ResponseEntity.ok(updatedRate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{rateId}/deactivate")
    public ResponseEntity<?> deactivateRate(@PathVariable Long rateId) {
        try {
            DeliveryRate rate = deliveryRateService.deactivateRate(rateId);
            return ResponseEntity.ok(rate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{rateId}/activate")
    public ResponseEntity<?> activateRate(@PathVariable Long rateId) {
        try {
            DeliveryRate rate = deliveryRateService.activateRate(rateId);
            return ResponseEntity.ok(rate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{rateId}")
    public ResponseEntity<Void> deleteDeliveryRate(@PathVariable Long rateId) {
        try {
            deliveryRateService.deleteDeliveryRate(rateId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/calculate-cost")
    public ResponseEntity<?> calculateDeliveryCost(@RequestParam Double distanceKm) {
        try {
            Double cost = deliveryRateService.calculateDeliveryCost(distanceKm);
            return ResponseEntity.ok(cost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}