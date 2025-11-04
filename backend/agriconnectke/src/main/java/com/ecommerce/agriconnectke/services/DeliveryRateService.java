package com.ecommerce.agriconnectke.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.exceptions.DuplicateResourceException;
import com.ecommerce.agriconnectke.exceptions.InvalidDeliveryRateException;
import com.ecommerce.agriconnectke.models.DeliveryRate;
import com.ecommerce.agriconnectke.repositories.DeliveryRateRepository;

@Service
public class DeliveryRateService {
    private final DeliveryRateRepository deliveryRateRepository;

    public DeliveryRateService(DeliveryRateRepository deliveryRateRepository) {
        this.deliveryRateRepository = deliveryRateRepository;
    }

    public DeliveryRate createDeliveryRate(DeliveryRate deliveryRate) {
        // Validate delivery rate
        validateDeliveryRate(deliveryRate);
        
        // Check for duplicate effective date
        Optional<DeliveryRate> existingRate = deliveryRateRepository.findByEffectiveFrom(deliveryRate.getEffectiveFrom());
        if (existingRate.isPresent()) {
            throw new DuplicateResourceException("Delivery rate already exists for date: " + deliveryRate.getEffectiveFrom());
        }
        
        return deliveryRateRepository.save(deliveryRate);
    }

    public List<DeliveryRate> getAllDeliveryRates() {
        return deliveryRateRepository.findAll();
    }

    public DeliveryRate getLatestRate() {
        return deliveryRateRepository.findLatestRate()
                .orElseThrow(() -> new InvalidDeliveryRateException("No active delivery rates configured"));
    }

    public List<DeliveryRate> getCurrentRates() {
        return deliveryRateRepository.findCurrentRates();
    }

    public List<DeliveryRate> getActiveRates() {
        return deliveryRateRepository.findByIsActiveTrue();
    }

    public List<DeliveryRate> getInactiveRates() {
        return deliveryRateRepository.findByIsActiveFalse();
    }

    public List<DeliveryRate> getFutureRates() {
        return deliveryRateRepository.findFutureRates();
    }

    public DeliveryRate getDeliveryRateById(Long rateId) {
        return deliveryRateRepository.findById(rateId)
                .orElseThrow(() -> new InvalidDeliveryRateException("Delivery rate not found with ID: " + rateId));
    }

    public DeliveryRate updateDeliveryRate(Long rateId, DeliveryRate deliveryRateDetails) {
        DeliveryRate deliveryRate = deliveryRateRepository.findById(rateId)
                .orElseThrow(() -> new InvalidDeliveryRateException("Delivery rate not found with ID: " + rateId));
        
        validateDeliveryRate(deliveryRateDetails);
        
        // Check for duplicate effective date (excluding current rate)
        Optional<DeliveryRate> existingRate = deliveryRateRepository.findByEffectiveFrom(deliveryRateDetails.getEffectiveFrom());
        if (existingRate.isPresent() && !existingRate.get().getRateId().equals(rateId)) {
            throw new DuplicateResourceException("Delivery rate already exists for date: " + deliveryRateDetails.getEffectiveFrom());
        }
        
        deliveryRate.setBaseFee(deliveryRateDetails.getBaseFee());
        deliveryRate.setRatePerKm(deliveryRateDetails.getRatePerKm());
        deliveryRate.setEffectiveFrom(deliveryRateDetails.getEffectiveFrom());
        deliveryRate.setIsActive(deliveryRateDetails.getIsActive());
        
        return deliveryRateRepository.save(deliveryRate);
    }

    public DeliveryRate deactivateRate(Long rateId) {
        DeliveryRate deliveryRate = deliveryRateRepository.findById(rateId)
                .orElseThrow(() -> new InvalidDeliveryRateException("Delivery rate not found with ID: " + rateId));
        
        deliveryRate.setIsActive(false);
        return deliveryRateRepository.save(deliveryRate);
    }

    public DeliveryRate activateRate(Long rateId) {
        DeliveryRate deliveryRate = deliveryRateRepository.findById(rateId)
                .orElseThrow(() -> new InvalidDeliveryRateException("Delivery rate not found with ID: " + rateId));
        
        deliveryRate.setIsActive(true);
        return deliveryRateRepository.save(deliveryRate);
    }

    public void deleteDeliveryRate(Long rateId) {
        if (!deliveryRateRepository.existsById(rateId)) {
            throw new InvalidDeliveryRateException("Delivery rate not found with ID: " + rateId);
        }
        deliveryRateRepository.deleteById(rateId);
    }

    public Double calculateDeliveryCost(Double distanceKm) {
        DeliveryRate latestRate = getLatestRate();
        // Convert Double to BigDecimal for the entity method
        BigDecimal distanceBigDecimal = BigDecimal.valueOf(distanceKm);
        return latestRate.calculateDeliveryCost(distanceBigDecimal).doubleValue();
    }

    public DeliveryRate getRateForDate(LocalDate date) {
        return deliveryRateRepository.findByEffectiveFrom(date)
                .orElseThrow(() -> new InvalidDeliveryRateException("No delivery rate found for date: " + date));
    }

    private void validateDeliveryRate(DeliveryRate deliveryRate) {
        // Use BigDecimal comparison methods
        if (deliveryRate.getBaseFee() == null || deliveryRate.getBaseFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDeliveryRateException("Base fee must be non-negative");
        }
        if (deliveryRate.getRatePerKm() == null || deliveryRate.getRatePerKm().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDeliveryRateException("Rate per km must be non-negative");
        }
        if (deliveryRate.getEffectiveFrom() == null) {
            throw new InvalidDeliveryRateException("Effective from date is required");
        }
        if (deliveryRate.getEffectiveFrom().isBefore(LocalDate.now().minusYears(1))) {
            throw new InvalidDeliveryRateException("Effective date cannot be more than 1 year in the past");
        }
    }
}