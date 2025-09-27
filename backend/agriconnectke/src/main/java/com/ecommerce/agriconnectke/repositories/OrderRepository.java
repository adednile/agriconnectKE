package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.Order;
public interface OrderRepository extends JpaRepository<Order, Long>  {
    
}
