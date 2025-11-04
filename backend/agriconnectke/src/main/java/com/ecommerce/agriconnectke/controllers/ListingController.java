package com.ecommerce.agriconnectke.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.models.Listing;
import com.ecommerce.agriconnectke.services.ListingService;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "*")
public class ListingController {
    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    // Get all listings
    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    // Get listing by ID
    @GetMapping("/{listingId}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long listingId) {
        Optional<Listing> listing = listingService.getListingById(listingId);
        return listing.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Create new listing
    @PostMapping
    public ResponseEntity<?> createListing(@RequestBody Listing listing) {
        try {
            Listing createdListing = listingService.createListing(listing);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdListing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get farmer's listings
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Listing>> getFarmerListings(@PathVariable Long farmerId) {
        List<Listing> listings = listingService.getFarmerListings(farmerId);
        return ResponseEntity.ok(listings);
    }

    // Get available listings for market
    @GetMapping("/market")
    public ResponseEntity<List<Listing>> getAvailableListings() {
        List<Listing> listings = listingService.getAvailableListings();
        return ResponseEntity.ok(listings);
    }

    // Search listings by crop name
    @GetMapping("/search")
    public ResponseEntity<List<Listing>> searchListings(@RequestParam String cropName) {
        List<Listing> listings = listingService.searchListings(cropName);
        return ResponseEntity.ok(listings);
    }

    // Update listing status
    @PutMapping("/{listingId}/status")
    public ResponseEntity<?> updateListingStatus(@PathVariable Long listingId, @RequestParam String status) {
        try {
            Listing updatedListing = listingService.updateListingStatus(listingId, status);
            return ResponseEntity.ok(updatedListing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update entire listing
    @PutMapping("/{listingId}")
    public ResponseEntity<?> updateListing(@PathVariable Long listingId, @RequestBody Listing listingDetails) {
        try {
            Listing updatedListing = listingService.updateListing(listingId, listingDetails);
            return ResponseEntity.ok(updatedListing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update listing quantity
    @PatchMapping("/{listingId}/quantity")
    public ResponseEntity<?> updateListingQuantity(@PathVariable Long listingId, @RequestParam Integer quantity) {
        try {
            Listing updatedListing = listingService.updateListingQuantity(listingId, quantity);
            return ResponseEntity.ok(updatedListing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete listing
    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long listingId) {
        try {
            listingService.deleteListing(listingId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get listings by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Listing>> getListingsByStatus(@PathVariable String status) {
        List<Listing> listings = listingService.getListingsByStatus(status);
        return ResponseEntity.ok(listings);
    }

    // Get active listings for a farmer
    @GetMapping("/farmer/{farmerId}/active")
    public ResponseEntity<List<Listing>> getActiveListingsByFarmer(@PathVariable Long farmerId) {
        List<Listing> listings = listingService.getActiveListingsByFarmer(farmerId);
        return ResponseEntity.ok(listings);
    }

    // Get expired listings
    @GetMapping("/expired")
    public ResponseEntity<List<Listing>> getExpiredListings() {
        List<Listing> listings = listingService.getExpiredListings();
        return ResponseEntity.ok(listings);
    }

    // Close expired listings (admin endpoint)
    @PostMapping("/close-expired")
    public ResponseEntity<String> closeExpiredListings() {
        try {
            listingService.closeExpiredListings();
            return ResponseEntity.ok("Expired listings have been closed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error closing expired listings: " + e.getMessage());
        }
    }
}