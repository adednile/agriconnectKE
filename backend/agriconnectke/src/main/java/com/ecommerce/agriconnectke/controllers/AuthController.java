package com.ecommerce.agriconnectke.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.dto.AuthResponse;
import com.ecommerce.agriconnectke.dto.CreateUserRequest;
import com.ecommerce.agriconnectke.dto.LoginRequest;
import com.ecommerce.agriconnectke.dto.UserDTO;
import com.ecommerce.agriconnectke.models.User;
import com.ecommerce.agriconnectke.services.AuthService;
import com.ecommerce.agriconnectke.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest request) {
        try {
            // Convert DTO to User entity
            User user = new User();
            user.setFullName(request.getFullName());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setPasswordHash(request.getPassword()); // Will be encoded in service
            user.setRole(request.getRole());
            user.setCounty(request.getCounty());
            user.setBusinessType(request.getBusinessType());

            User registeredUser = userService.registerUser(user);
            
            // Generate token
            String token = authService.authenticate(user.getPhone(), request.getPassword());
            
            UserDTO userDTO = new UserDTO(registeredUser);
            AuthResponse authResponse = new AuthResponse(token, userDTO);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticate(loginRequest.getPhone(), loginRequest.getPassword());
            User user = userService.findByPhone(loginRequest.getPhone())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            UserDTO userDTO = new UserDTO(user);
            AuthResponse authResponse = new AuthResponse(token, userDTO);
            
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials: " + e.getMessage());
        }
    }
}