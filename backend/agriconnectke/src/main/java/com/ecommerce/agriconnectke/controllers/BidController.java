package com.ecommerce.agriconnectke.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.agriconnectke.dto.BidRequest;
import com.ecommerce.agriconnectke.models.Bid;
import com.ecommerce.agriconnectke.services.BidService;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin(origins = "*")
public class BidController {
    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<?> placeBid(@RequestBody Bid bid) {
        try {
            Bid createdBid = bidService.placeBid(bid);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Bid>> getUserBids(@PathVariable Long buyerId) {
        List<Bid> bids = bidService.getUserBids(buyerId);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<Bid>> getListingBids(@PathVariable Long listingId) {
        List<Bid> bids = bidService.getListingBids(listingId);
        return ResponseEntity.ok(bids);
    }

    @PutMapping("/{bidId}/status")
    public ResponseEntity<?> updateBidStatus(@PathVariable Long bidId, @RequestParam String status) {
        try {
            Bid updatedBid = bidService.updateBidStatus(bidId, status);
            return ResponseEntity.ok(updatedBid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<Bid> getBidById(@PathVariable Long bidId) {
        return bidService.getBidById(bidId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<Bid>> getUserBidsByStatus(
            @PathVariable Long buyerId, 
            @PathVariable String status) {
        List<Bid> bids = bidService.getUserBidsByStatus(buyerId, status);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/listing/{listingId}/status/{status}")
    public ResponseEntity<List<Bid>> getListingBidsByStatus(
            @PathVariable Long listingId, 
            @PathVariable String status) {
        List<Bid> bids = bidService.getListingBidsByStatus(listingId, status);
        return ResponseEntity.ok(bids);
    }

    @DeleteMapping("/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long bidId) {
        try {
            bidService.deleteBid(bidId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Additional method in BidController using DTOs
@PostMapping("/v2")
public ResponseEntity<?> placeBidV2(@RequestBody BidRequest bidRequest) {
    try {
        Bid bid = new Bid();
        bid.setBuyerId(bidRequest.getBuyerId());
        bid.setListingId(bidRequest.getListingId());
        bid.setOfferPrice(bidRequest.getOfferPrice());
        
        Bid createdBid = bidService.placeBid(bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBid);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}