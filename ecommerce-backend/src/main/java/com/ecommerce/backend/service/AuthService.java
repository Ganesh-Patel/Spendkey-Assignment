package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AuthResponse;
import com.ecommerce.backend.dto.LoginRequest;
import com.ecommerce.backend.dto.SignupRequest;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.UserRole;
import com.ecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Register a new user
     */
    public AuthResponse signup(SignupRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse("Username already exists");
        }
        
        // Check if mobile number already exists
        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            return new AuthResponse("Mobile number already registered");
        }
        
        // Check if email already exists (if provided)
        if (request.getEmail() != null && !request.getEmail().isEmpty() && 
            userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already registered");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(request.getPassword()); // In production, this should be encrypted
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.USER);
        user.setIsActive(true);
        
        // Save user
        User savedUser = userRepository.save(user);
        
        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), 
                              "User registered successfully");
    }
    
    /**
     * Authenticate user login
     */
    public AuthResponse login(LoginRequest request) {
        // Find user by username or mobile number
        User user = userRepository.findByUsernameOrMobileNumber(request.getUsernameOrMobile())
                .orElse(null);
        
        if (user == null) {
            return new AuthResponse("Invalid username/mobile number or password");
        }
        
        // Check if user is active
        if (!user.getIsActive()) {
            return new AuthResponse("Account is deactivated");
        }
        
        // Check password (in production, use proper password hashing)
        if (!user.getPassword().equals(request.getPassword())) {
            return new AuthResponse("Invalid username/mobile number or password");
        }
        
        // Generate simple token (in production, use JWT)
        String token = generateToken();
        
        return new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getMobileNumber(),
            user.getRole(),
            token
        );
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    /**
     * Update user profile
     */
    public AuthResponse updateProfile(Long userId, SignupRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        
        if (user == null) {
            return new AuthResponse("User not found");
        }
        
        // Check if new username already exists (if changed)
        if (!user.getUsername().equals(request.getUsername()) && 
            userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse("Username already exists");
        }
        
        // Check if new mobile number already exists (if changed)
        if (!user.getMobileNumber().equals(request.getMobileNumber()) && 
            userRepository.existsByMobileNumber(request.getMobileNumber())) {
            return new AuthResponse("Mobile number already registered");
        }
        
        // Check if new email already exists (if changed)
        if (request.getEmail() != null && !request.getEmail().isEmpty() && 
            !request.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already registered");
        }
        
        // Update user fields
        user.setUsername(request.getUsername());
        user.setMobileNumber(request.getMobileNumber());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword()); // In production, encrypt
        }
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        // Save updated user
        userRepository.save(user);
        
        return new AuthResponse(user.getId(), user.getUsername(), 
                              "Profile updated successfully");
    }
    
    /**
     * Deactivate user account
     */
    public AuthResponse deactivateAccount(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        
        if (user == null) {
            return new AuthResponse("User not found");
        }
        
        user.setIsActive(false);
        userRepository.save(user);
        
        return new AuthResponse("Account deactivated successfully");
    }
    
    /**
     * Generate simple token (replace with JWT in production)
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
} 