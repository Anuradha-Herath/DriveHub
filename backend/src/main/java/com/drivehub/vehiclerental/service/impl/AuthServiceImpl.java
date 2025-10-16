package com.drivehub.vehiclerental.service.impl;

import com.drivehub.vehiclerental.dto.AuthResponse;
import com.drivehub.vehiclerental.dto.LoginRequest;
import com.drivehub.vehiclerental.dto.RegisterRequest;
import com.drivehub.vehiclerental.entity.Admin;
import com.drivehub.vehiclerental.entity.Customer;
import com.drivehub.vehiclerental.entity.User;
import com.drivehub.vehiclerental.repository.UserRepository;
import com.drivehub.vehiclerental.service.AuthService;
import com.drivehub.vehiclerental.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AuthService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create user based on type
        User user;
        if ("CUSTOMER".equalsIgnoreCase(request.getUserType())) {
            Customer customer = new Customer();
            customer.setDrivingLicenseNumber(request.getDrivingLicenseNumber());
            customer.setAddress(request.getAddress());
            customer.setRole(User.Role.CUSTOMER);
            user = customer;
        } else if ("ADMIN".equalsIgnoreCase(request.getUserType())) {
            Admin admin = new Admin();
            admin.setDepartment(request.getDepartment());
            admin.setEmployeeId(request.getEmployeeId());
            admin.setRole(User.Role.ADMIN);
            user = admin;
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
        
        // Set common fields
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        
        // Save user
        user = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user);
        
        log.info("User registered successfully: {}", user.getEmail());
        
        return new AuthResponse(
            token,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole().name()
        );
    }
    
    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getEmail());
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // Get user details
        User user = (User) authentication.getPrincipal();
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user);
        
        log.info("User logged in successfully: {}", user.getEmail());
        
        return new AuthResponse(
            token,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}
