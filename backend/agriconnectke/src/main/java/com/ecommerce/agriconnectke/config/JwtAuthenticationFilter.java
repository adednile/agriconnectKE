package com.ecommerce.agriconnectke.config;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.agriconnectke.services.JwtService;
import com.ecommerce.agriconnectke.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            
            String token = authHeader.substring(7);
            
            // Use the correct method name - validateToken without parameters
            if (jwtService.validateToken(token)) {
                String phone = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);
                
                if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userService.findByPhone(phone).ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                        
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        logger.debug("Authenticated user: {} with role: {}", phone, role);
                    });
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            // Don't throw exception here, just continue the filter chain
        }
        
        filterChain.doFilter(request, response);
    }
}