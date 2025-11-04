package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.exceptions.InsufficientQuantityException;
import com.ecommerce.agriconnectke.exceptions.InvalidListingException;
import com.ecommerce.agriconnectke.exceptions.ListingNotFoundException;
import com.ecommerce.agriconnectke.models.Listing;
import com.ecommerce.agriconnectke.repositories.ListingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {
    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> getListingsByFarmer(Long farmerId) {
        return listingRepository.findByFarmerId(farmerId);
    }

    // Add this method to fix the error
    public List<Listing> getFarmerListings(Long farmerId) {
        return listingRepository.findByFarmerId(farmerId);
    }

    // Add this method to fix the error
    public List<Listing> getAvailableListings() {
        return listingRepository.findAvailableListings();
    }

    // Add this method to fix the error
    public List<Listing> searchListings(String cropName) {
        return listingRepository.findByCropNameContainingIgnoreCase(cropName);
    }

    // Add this method to fix the error
    public Listing updateListingStatus(Long listingId, String status) {
        Optional<Listing> optionalListing = listingRepository.findById(listingId);
        if (optionalListing.isPresent()) {
            Listing listing = optionalListing.get();
            
            // Validate status transition
            validateStatusTransition(listing.getStatus(), status);
            
            listing.setStatus(status);
            return listingRepository.save(listing);
        } else {
            throw new ListingNotFoundException(listingId);
        }
    }

    public Listing createListing(Listing listing) {
        // Validate listing data
        validateListing(listing);
        
        // Set default status if not provided
        if (listing.getStatus() == null) {
            listing.setStatus("OPEN");
        }
        
        return listingRepository.save(listing);
    }

    public void deleteListing(Long id) {
        if (!listingRepository.existsById(id)) {
            throw new ListingNotFoundException(id);
        }
        listingRepository.deleteById(id);
    }

    // Additional useful methods
    public Optional<Listing> getListingById(Long listingId) {
        return listingRepository.findById(listingId);
    }

    public List<Listing> getListingsByStatus(String status) {
        return listingRepository.findByStatus(status);
    }

    public Listing updateListing(Long listingId, Listing listingDetails) {
        Optional<Listing> optionalListing = listingRepository.findById(listingId);
        if (optionalListing.isPresent()) {
            Listing listing = optionalListing.get();
            
            // Validate updated data
            validateListing(listingDetails);
            
            listing.setCropName(listingDetails.getCropName());
            listing.setGrade(listingDetails.getGrade());
            listing.setPrice(listingDetails.getPrice());
            listing.setQuantity(listingDetails.getQuantity());
            listing.setAvailabilityStart(listingDetails.getAvailabilityStart());
            listing.setAvailabilityEnd(listingDetails.getAvailabilityEnd());
            listing.setStatus(listingDetails.getStatus());
            
            return listingRepository.save(listing);
        } else {
            throw new ListingNotFoundException(listingId);
        }
    }

    public Listing updateListingQuantity(Long listingId, Integer newQuantity) {
        Optional<Listing> optionalListing = listingRepository.findById(listingId);
        if (optionalListing.isPresent()) {
            Listing listing = optionalListing.get();
            
            if (newQuantity < 0) {
                throw new InvalidListingException("Quantity cannot be negative");
            }
            
            listing.setQuantity(newQuantity);
            
            // If quantity becomes 0, automatically close the listing
            if (newQuantity == 0 && "OPEN".equals(listing.getStatus())) {
                listing.setStatus("CLOSED");
            }
            
            return listingRepository.save(listing);
        } else {
            throw new ListingNotFoundException(listingId);
        }
    }

    public void reduceListingQuantity(Long listingId, Integer quantityToReduce) {
        Optional<Listing> optionalListing = listingRepository.findById(listingId);
        if (optionalListing.isPresent()) {
            Listing listing = optionalListing.get();
            
            if (quantityToReduce > listing.getQuantity()) {
                throw new InsufficientQuantityException(
                    listing.getCropName(), listing.getQuantity(), quantityToReduce);
            }
            
            int newQuantity = listing.getQuantity() - quantityToReduce;
            listing.setQuantity(newQuantity);
            
            // If quantity becomes 0, automatically close the listing
            if (newQuantity == 0 && "OPEN".equals(listing.getStatus())) {
                listing.setStatus("CLOSED");
            }
            
            listingRepository.save(listing);
        } else {
            throw new ListingNotFoundException(listingId);
        }
    }

    public List<Listing> getActiveListingsByFarmer(Long farmerId) {
        return listingRepository.findActiveListingsByFarmer(farmerId);
    }

    public List<Listing> getExpiredListings() {
        LocalDate today = LocalDate.now();
        return listingRepository.findAll().stream()
                .filter(listing -> listing.getAvailabilityEnd() != null && 
                                 listing.getAvailabilityEnd().isBefore(today) &&
                                 "OPEN".equals(listing.getStatus()))
                .toList();
    }

    public void closeExpiredListings() {
        List<Listing> expiredListings = getExpiredListings();
        for (Listing listing : expiredListings) {
            listing.setStatus("EXPIRED");
            listingRepository.save(listing);
        }
    }

    private void validateListing(Listing listing) {
        if (listing.getCropName() == null || listing.getCropName().trim().isEmpty()) {
            throw new InvalidListingException("Crop name is required");
        }
        // Use BigDecimal comparison for price validation
        if (listing.getPrice() == null || listing.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidListingException("Price must be positive");
        }
        if (listing.getQuantity() == null || listing.getQuantity() < 0) {
            throw new InvalidListingException("Quantity cannot be negative");
        }
        if (listing.getAvailabilityStart() != null && listing.getAvailabilityEnd() != null) {
            if (listing.getAvailabilityEnd().isBefore(listing.getAvailabilityStart())) {
                throw new InvalidListingException("Availability end date cannot be before start date");
            }
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        // Define valid status transitions
        switch (currentStatus) {
            case "OPEN":
                if (!"CLOSED".equals(newStatus) && !"EXPIRED".equals(newStatus)) {
                    throw new InvalidListingException("Open listing can only be closed or expired");
                }
                break;
            case "CLOSED":
                if (!"OPEN".equals(newStatus)) {
                    throw new InvalidListingException("Closed listing can only be reopened");
                }
                break;
            case "EXPIRED":
                throw new InvalidListingException("Expired listings cannot be modified");
            default:
                throw new InvalidListingException("Invalid current status: " + currentStatus);
        }
    }
}