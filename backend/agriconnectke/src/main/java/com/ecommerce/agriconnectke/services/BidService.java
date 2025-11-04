package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.exceptions.BidNotFoundException;
import com.ecommerce.agriconnectke.exceptions.InvalidBidException;
import com.ecommerce.agriconnectke.exceptions.ListingNotFoundException;
import com.ecommerce.agriconnectke.models.Bid;
import com.ecommerce.agriconnectke.models.Listing;
import com.ecommerce.agriconnectke.repositories.BidRepository;
import com.ecommerce.agriconnectke.repositories.ListingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final ListingRepository listingRepository;

    public BidService(BidRepository bidRepository, ListingRepository listingRepository) {
        this.bidRepository = bidRepository;
        this.listingRepository = listingRepository;
    }

    public Bid placeBid(Bid bid) {
        // Verify listing exists
        Listing listing = listingRepository.findById(bid.getListingId())
                .orElseThrow(() -> new ListingNotFoundException(bid.getListingId()));
        
        // Validate listing status
        if (!"OPEN".equals(listing.getStatus())) {
            throw new InvalidBidException("Cannot bid on closed listing");
        }

        // Validate bid price - use BigDecimal comparison for offerPrice
        if (bid.getOfferPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBidException("Bid price must be positive");
        }

        // Check if listing has sufficient quantity - use Integer comparison for quantity
        if (listing.getQuantity() <= 0) {
            throw new InvalidBidException("Listing has no available quantity");
        }

        // Check for duplicate pending bid from same buyer
        List<Bid> existingBids = bidRepository.findByBuyerIdAndListingIdAndStatus(
            bid.getBuyerId(), bid.getListingId(), "PENDING");
        if (!existingBids.isEmpty()) {
            throw new InvalidBidException("You already have a pending bid for this listing");
        }

        return bidRepository.save(bid);
    }

    public List<Bid> getUserBids(Long buyerId) {
        return bidRepository.findByBuyerId(buyerId);
    }

    public List<Bid> getListingBids(Long listingId) {
        return bidRepository.findByListingId(listingId);
    }

    public Bid updateBidStatus(Long bidId, String status) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new BidNotFoundException(bidId));
        
        // Validate status
        if (!isValidStatus(status)) {
            throw new InvalidBidException("Invalid bid status: " + status);
        }

        bid.setStatus(status);
        return bidRepository.save(bid);
    }

    public Optional<Bid> getBidById(Long bidId) {
        return bidRepository.findById(bidId);
    }

    public List<Bid> getUserBidsByStatus(Long buyerId, String status) {
        return bidRepository.findByBuyerIdAndStatus(buyerId, status);
    }

    public List<Bid> getListingBidsByStatus(Long listingId, String status) {
        return bidRepository.findByListingIdAndStatus(listingId, status);
    }

    public void deleteBid(Long bidId) {
        if (!bidRepository.existsById(bidId)) {
            throw new BidNotFoundException(bidId);
        }
        bidRepository.deleteById(bidId);
    }

    private boolean isValidStatus(String status) {
        return status != null && 
               (status.equals("PENDING") || status.equals("ACCEPTED") || status.equals("REJECTED"));
    }
}