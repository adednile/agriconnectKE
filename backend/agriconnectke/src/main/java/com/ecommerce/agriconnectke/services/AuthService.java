package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.exceptions.InvalidCredentialsException;
import com.ecommerce.agriconnectke.exceptions.UserNotFoundException;
import com.ecommerce.agriconnectke.models.User;
import com.ecommerce.agriconnectke.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String authenticate(String phone, String password) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException("User not found with phone: " + phone));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        // Check if user is active
        if ("INACTIVE".equals(user.getStatus()) || "SUSPENDED".equals(user.getStatus())) {
            throw new InvalidCredentialsException("Account is " + user.getStatus().toLowerCase());
        }

        return jwtService.generateToken(user);
    }

    public User register(User user) {
        // Check if phone already exists
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("User with phone " + user.getPhone() + " already exists");
        }

        // Validate user data
        validateUserRegistration(user);

        // Encode password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // Set default status if not provided
        if (user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }

        return userRepository.save(user);
    }

    public Optional<User> validateToken(String token) {
        try {
            if (jwtService.validateToken(token)) {
                String phone = jwtService.extractUsername(token);
                return userRepository.findByPhone(phone);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String refreshToken(String oldToken) {
        if (jwtService.validateToken(oldToken)) {
            String phone = jwtService.extractUsername(oldToken);
            String role = jwtService.extractRole(oldToken);
            return jwtService.generateToken(phone, role);
        }
        throw new InvalidCredentialsException("Invalid or expired token");
    }

    public boolean changePassword(String phone, String oldPassword, String newPassword) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    private void validateUserRegistration(User user) {
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }

        // Validate phone format (Kenyan format)
        if (!user.getPhone().matches("^(?:254|\\+254|0)?(7\\d{8})$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        // Normalize phone number to 254 format
        user.setPhone(normalizePhoneNumber(user.getPhone()));
    }

    private String normalizePhoneNumber(String phone) {
        // Remove any non-digit characters
        String digits = phone.replaceAll("\\D", "");
        
        if (digits.startsWith("0")) {
            return "254" + digits.substring(1);
        } else if (digits.startsWith("254")) {
            return digits;
        } else if (digits.startsWith("7") && digits.length() == 9) {
            return "254" + digits;
        }
        
        return digits; // Return as is if already in correct format
    }
}