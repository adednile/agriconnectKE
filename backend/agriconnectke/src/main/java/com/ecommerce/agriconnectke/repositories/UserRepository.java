package com.ecommerce.agriconnectke.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.agriconnectke.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    List<User> findByRole(String role);
    boolean existsByPhone(String phone);
    
    @Query("SELECT u FROM User u WHERE u.role = 'DRIVER'")
    List<User> findAllDrivers();
    
    @Query("SELECT u FROM User u WHERE u.role = 'FARMER'")
    List<User> findAllFarmers();
    
    @Query("SELECT u FROM User u WHERE u.role = 'BUYER'")
    List<User> findAllBuyers();
    
    List<User> findByCountyContainingIgnoreCase(String county);
    
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> findByFullNameContaining(String name);
    
    Optional<User> findByUserIdAndRole(Long userId, String role);
}