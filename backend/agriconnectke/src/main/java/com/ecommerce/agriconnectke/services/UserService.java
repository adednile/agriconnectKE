package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.dto.UserDTO;
import com.ecommerce.agriconnectke.exceptions.DuplicateResourceException;
import com.ecommerce.agriconnectke.exceptions.UserNotFoundException;
import com.ecommerce.agriconnectke.models.User;
import com.ecommerce.agriconnectke.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Check if phone already exists
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new DuplicateResourceException("User", "phone", user.getPhone());
        }

        // Validate user data
        validateUser(user);

        // Encode password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        return userRepository.save(user);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public UserDTO getUserDTOById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return new UserDTO(user);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<UserDTO> getUsersDTOByRole(String role) {
        return userRepository.findByRole(role).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public List<User> getAllDrivers() {
        return userRepository.findAllDrivers();
    }

    public List<User> getAllFarmers() {
        return userRepository.findAllFarmers();
    }

    public List<User> getAllBuyers() {
        return userRepository.findAllBuyers();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserDTO> getAllUsersDTO() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Check if phone is being changed and if it already exists
        if (!user.getPhone().equals(userDetails.getPhone()) && 
            userRepository.existsByPhone(userDetails.getPhone())) {
            throw new DuplicateResourceException("User", "phone", userDetails.getPhone());
        }

        // Validate updated data
        validateUser(userDetails);

        user.setFullName(userDetails.getFullName());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        user.setCounty(userDetails.getCounty());
        user.setBusinessType(userDetails.getBusinessType());

        // Only update password if provided
        if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
        }

        return userRepository.save(user);
    }

    public User updateUserProfile(Long userId, String fullName, String county, String businessType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName.trim());
        }
        if (county != null) {
            user.setCounty(county);
        }
        if (businessType != null) {
            user.setBusinessType(businessType);
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }

    public List<User> searchUsers(String searchTerm) {
        // Search by name or county
        List<User> byName = userRepository.findByFullNameContaining(searchTerm);
        List<User> byCounty = userRepository.findByCountyContainingIgnoreCase(searchTerm);
        
        // Combine and remove duplicates
        return byName.stream()
                .filter(user -> !byCounty.contains(user))
                .collect(Collectors.toList());
    }

    public List<UserDTO> searchUsersDTO(String searchTerm) {
        return searchUsers(searchTerm).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getUsersCountByRole(String role) {
        return userRepository.findByRole(role).size();
    }

    public boolean isDriver(Long userId) {
        Optional<User> user = userRepository.findByUserIdAndRole(userId, "DRIVER");
        return user.isPresent();
    }

    public boolean isFarmer(Long userId) {
        Optional<User> user = userRepository.findByUserIdAndRole(userId, "FARMER");
        return user.isPresent();
    }

    public boolean isBuyer(Long userId) {
        Optional<User> user = userRepository.findByUserIdAndRole(userId, "BUYER");
        return user.isPresent();
    }

    private void validateUser(User user) {
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Validate phone format (basic validation)
        if (!user.getPhone().matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
        
        // Validate role
        try {
            User.Role.valueOf(user.getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }
    }
}