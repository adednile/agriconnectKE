package com.ecommerce.agriconnectke.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long expirationTime;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpirationTime;

    @Value("${jwt.issuer:agriconnectke}")
    private String issuer;

    @Value("${jwt.audience:agriconnectke-web}")
    private String audience;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username, expirationTime);
    }

    public String generateRefreshToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("type", "refresh");
        return createToken(claims, username, refreshExpirationTime);
    }

    public String generateToken(com.ecommerce.agriconnectke.models.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userId", user.getUserId());
        claims.put("fullName", user.getFullName());
        return createToken(claims, user.getPhone(), expirationTime);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Add this method - the one that was missing
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token) && extractUsername(token) != null;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String refreshToken(String token) {
        final String username = extractUsername(token);
        final String role = extractRole(token);
        return generateToken(username, role);
    }

    // Additional utility methods
    public boolean isRefreshToken(String token) {
        try {
            String type = extractClaim(token, claims -> claims.get("type", String.class));
            return "refresh".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        try {
            return extractClaim(token, claims -> claims.get("userId", Long.class));
        } catch (Exception e) {
            return null;
        }
    }

    public String extractFullName(String token) {
        try {
            return extractClaim(token, claims -> claims.get("fullName", String.class));
        } catch (Exception e) {
            return null;
        }
    }
}