package com.ecommerce.agriconnectke.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    
    // Find all listings by farmer ID
    List<Listing> findByFarmerId(Long farmerId);
    
    // Find listings by status
    List<Listing> findByStatus(String status);
    
    // Search listings by crop name (case-insensitive)
    List<Listing> findByCropNameContainingIgnoreCase(String cropName);
    
    // Find available listings (OPEN status and quantity > 0)
    @Query("SELECT l FROM Listing l WHERE l.status = 'OPEN' AND l.quantity > 0")
    List<Listing> findAvailableListings();
    
    // Find listings by farmer and status
    List<Listing> findByFarmerIdAndStatus(Long farmerId, String status);
    
    // Find active listings for a farmer
    @Query("SELECT l FROM Listing l WHERE l.farmerId = :farmerId AND l.status = 'OPEN' AND l.quantity > 0")
    List<Listing> findActiveListingsByFarmer(@Param("farmerId") Long farmerId);
    
    // Find listings that are expiring soon
    @Query("SELECT l FROM Listing l WHERE l.availabilityEnd BETWEEN :startDate AND :endDate AND l.status = 'OPEN'")
    List<Listing> findListingsExpiringBetween(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    // Find listings by price range
    @Query("SELECT l FROM Listing l WHERE l.price BETWEEN :minPrice AND :maxPrice AND l.status = 'OPEN' AND l.quantity > 0")
    List<Listing> findByPriceBetween(@Param("minPrice") Double minPrice, 
                                   @Param("maxPrice") Double maxPrice);
    
    // Count listings by farmer and status
    @Query("SELECT COUNT(l) FROM Listing l WHERE l.farmerId = :farmerId AND l.status = :status")
    Long countByFarmerIdAndStatus(@Param("farmerId") Long farmerId, 
                                @Param("status") String status);
}