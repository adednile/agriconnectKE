package com.ecommerce.agriconnectke.dto;

import com.ecommerce.agriconnectke.models.User;
import java.time.LocalDateTime;

public class UserDTO {
    private Long userId;
    private String fullName;
    private String phone;
    private String email;
    private String role;
    private String county;
    private String businessType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional fields for statistics
    private Long listingCount;
    private Long orderCount;
    private Double totalSales;

    public UserDTO() {}

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.county = user.getCounty();
        this.businessType = user.getBusinessType();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getListingCount() { return listingCount; }
    public void setListingCount(Long listingCount) { this.listingCount = listingCount; }

    public Long getOrderCount() { return orderCount; }
    public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }

    public Double getTotalSales() { return totalSales; }
    public void setTotalSales(Double totalSales) { this.totalSales = totalSales; }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}