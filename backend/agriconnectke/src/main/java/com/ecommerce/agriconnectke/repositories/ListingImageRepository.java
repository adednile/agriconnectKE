package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.ListingImage;
public interface ListingImageRepository extends JpaRepository<ListingImage, Long> {
    
}
