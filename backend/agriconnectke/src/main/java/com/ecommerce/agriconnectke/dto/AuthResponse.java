package com.ecommerce.agriconnectke.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UserDTO user;
    private String refreshToken;

    public AuthResponse() {}

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public AuthResponse(String token, UserDTO user, String refreshToken) {
        this.token = token;
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public AuthResponse(String token, String type, UserDTO user) {
        this.token = token;
        this.type = type;
        this.user = user;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "type='" + type + '\'' +
                ", user=" + user +
                '}';
    }
}