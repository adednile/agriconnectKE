package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.Listing;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    
}
