package com.ecommerce.agriconnectke.config;

import com.ecommerce.agriconnectke.models.DeliveryRate;
import com.ecommerce.agriconnectke.models.User;
import com.ecommerce.agriconnectke.repositories.DeliveryRateRepository;
import com.ecommerce.agriconnectke.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, 
                                    DeliveryRateRepository deliveryRateRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (userRepository.findByPhone("254700000000").isEmpty()) {
                User admin = new User();
                admin.setFullName("System Administrator");
                admin.setPhone("254700000000");
                admin.setEmail("admin@agriconnectke.com");
                admin.setRole("ADMIN");
                admin.setCounty("Nairobi");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setStatus("ACTIVE");
                
                userRepository.save(admin);
                System.out.println("Admin user created successfully");
            }

            // Create default delivery rates if not exists
            if (deliveryRateRepository.count() == 0) {
                DeliveryRate rate = new DeliveryRate();
                rate.setBaseFee(BigDecimal.valueOf(100.00));
                rate.setRatePerKm(BigDecimal.valueOf(15.00));
                rate.setEffectiveFrom(LocalDate.now());
                
                deliveryRateRepository.save(rate);
                System.out.println("Default delivery rate created successfully");
            }
        };
    }
}