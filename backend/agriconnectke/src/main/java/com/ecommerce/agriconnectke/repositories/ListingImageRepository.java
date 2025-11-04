package com.ecommerce.agriconnectke.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.ListingImage;

@Repository
public interface ListingImageRepository extends JpaRepository<ListingImage, Long> {
    
    List<ListingImage> findByListingId(Long listingId);
    
    @Query("SELECT li FROM ListingImage li WHERE li.listingId = :listingId AND li.isPrimary = true")
    Optional<ListingImage> findPrimaryImageByListingId(@Param("listingId") Long listingId);
    
    @Query("SELECT li FROM ListingImage li WHERE li.listingId = :listingId ORDER BY li.isPrimary DESC, li.uploadedAt DESC")
    List<ListingImage> findByListingIdOrderByPrimaryAndUploadDate(@Param("listingId") Long listingId);
    
    @Modifying
    @Query("UPDATE ListingImage li SET li.isPrimary = false WHERE li.listingId = :listingId")
    void clearPrimaryImages(@Param("listingId") Long listingId);
    
    @Modifying
    @Query("UPDATE ListingImage li SET li.isPrimary = true WHERE li.imageId = :imageId")
    void setAsPrimaryImage(@Param("imageId") Long imageId);
    
    Long countByListingId(Long listingId);
    
    @Modifying
    @Query("DELETE FROM ListingImage li WHERE li.listingId = :listingId")
    void deleteByListingId(@Param("listingId") Long listingId);
    
    boolean existsByListingIdAndIsPrimaryTrue(Long listingId);
}