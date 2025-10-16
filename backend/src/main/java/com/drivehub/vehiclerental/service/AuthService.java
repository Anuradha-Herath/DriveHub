package com.drivehub.vehiclerental.service;

import com.drivehub.vehiclerental.dto.AuthResponse;
import com.drivehub.vehiclerental.dto.LoginRequest;
import com.drivehub.vehiclerental.dto.RegisterRequest;

/**
 * Service interface for authentication operations
 */
public interface AuthService {
    
    /**
     * Register a new user
     * 
     * @param request Registration request
     * @return Authentication response with JWT token
     */
    AuthResponse register(RegisterRequest request);
    
    /**
     * Authenticate user and generate JWT token
     * 
     * @param request Login request
     * @return Authentication response with JWT token
     */
    AuthResponse login(LoginRequest request);
}
