package com.ecommerce.agriconnectke.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.agriconnectke.models.User;

public interface  UserRepository extends JpaRepository<User, Long> {
    
    
}
