package com.ecommerce.agriconnectke.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecommerce.agriconnectke.models.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByBuyerId(Long buyerId);
    List<Bid> findByListingId(Long listingId);
    List<Bid> findByBuyerIdAndStatus(Long buyerId, String status);
    List<Bid> findByListingIdAndStatus(Long listingId, String status);
    
    // New method to check for duplicate pending bids
    @Query("SELECT b FROM Bid b WHERE b.buyerId = ?1 AND b.listingId = ?2 AND b.status = 'PENDING'")
    List<Bid> findByBuyerIdAndListingIdAndStatus(Long buyerId, Long listingId, String status);
    
    // Find bids by multiple statuses
    @Query("SELECT b FROM Bid b WHERE b.listingId = ?1 AND b.status IN ?2")
    List<Bid> findByListingIdAndStatusIn(Long listingId, List<String> statuses);
    
    // Count pending bids for a listing
    @Query("SELECT COUNT(b) FROM Bid b WHERE b.listingId = ?1 AND b.status = 'PENDING'")
    Long countPendingBidsByListingId(Long listingId);
}