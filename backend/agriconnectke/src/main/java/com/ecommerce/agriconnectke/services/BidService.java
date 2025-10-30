package com.ecommerce.agriconnectke.services;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.agriconnectke.models.Bid;
import com.ecommerce.agriconnectke.repositories.BidRepository;

@Service
public class BidService {
    private final BidRepository bidRepository;

    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public List<Bid> getBidsByListing(Long listingId) {
        return bidRepository.findByListingId(listingId);
    }

    public Bid placeBid(Bid bid) {
        return bidRepository.save(bid);
    }
}