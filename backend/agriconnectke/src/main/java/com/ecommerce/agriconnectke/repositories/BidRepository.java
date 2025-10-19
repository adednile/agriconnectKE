package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.Bid;
public interface BidRepository extends JpaRepository<Bid, Long> {
    
}
