package com.ecommerce.agriconnectke.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "user_id")
    private Long userId;

    @Column(name="full_name", nullable=false)
    private String fullName;

    @Column(name="phone", nullable=false, unique=true)
    private String phone;

    @Column(name="role", nullable=false)
    private String role;

    @Column(name="county")
    private String county;

    @Column(name="business_type")
    private String businessType;

    @Column(name="password_hash", nullable=false)
    private String passwordHash;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    // getters & setters
}
    