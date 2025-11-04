package com.ecommerce.agriconnectke.dto;

public class UpdateUserRequest {
    private String fullName;
    private String phone;
    private String role;
    private String county;
    private String businessType;
    private String password; // Optional

    // Constructors
    public UpdateUserRequest() {}

    // Getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}