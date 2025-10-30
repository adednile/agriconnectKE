package com.ecommerce.agriconnectke.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.Listing;


public interface ListingRepository extends JpaRepository<Listing, Long> {
        List<Listing> findByFarmerId(Long farmerId);

    
}
