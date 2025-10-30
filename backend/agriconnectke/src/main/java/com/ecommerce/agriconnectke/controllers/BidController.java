package com.ecommerce.agriconnectke.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<Bid> getAllBids() {
        return bidService.getAllBids();
    }

    @GetMapping("/listing/{listingId}")
    public List<Bid> getBidsByListing(@PathVariable Long listingId) {
        return bidService.getBidsByListing(listingId);
    }

    @PostMapping
    public Bid placeBid(@RequestBody Bid bid) {
        return bidService.placeBid(bid);
    }
}