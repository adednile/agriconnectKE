package com.ecommerce.agriconnectke.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public LoginRequest() {}

    public LoginRequest(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    // Getters and Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "phone='" + phone + '\'' +
                '}';
    }
}