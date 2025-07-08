package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AuthResponse;
import com.ecommerce.backend.dto.LoginRequest;
import com.ecommerce.backend.dto.SignupRequest;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * POST /auth/signup - Register a new user
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        
        if (response.getMessage().contains("already exists") || 
            response.getMessage().contains("already registered")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /auth/login - Authenticate user login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        
        if (response.getMessage().contains("Invalid") || 
            response.getMessage().contains("deactivated")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /auth/profile/{userId} - Get user profile
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getProfile(@PathVariable Long userId) {
        User user = authService.getUserById(userId);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Don't return password in response
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
    
    /**
     * PUT /auth/profile/{userId} - Update user profile
     */
    @PutMapping("/profile/{userId}")
    public ResponseEntity<AuthResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.updateProfile(userId, request);
        
        if (response.getMessage().contains("not found") || 
            response.getMessage().contains("already exists") || 
            response.getMessage().contains("already registered")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * DELETE /auth/profile/{userId} - Deactivate user account
     */
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<AuthResponse> deactivateAccount(@PathVariable Long userId) {
        AuthResponse response = authService.deactivateAccount(userId);
        
        if (response.getMessage().contains("not found")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /auth/check-username/{username} - Check if username is available
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameAvailability(@PathVariable String username) {
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok(user == null);
    }
    
    /**
     * POST /auth/validate-token - Validate authentication token
     */
    @PostMapping("/validate-token")
    public ResponseEntity<AuthResponse> validateToken(@RequestParam String token) {
        // In production, implement proper JWT token validation
        // For now, return a simple response
        AuthResponse response = new AuthResponse("Token validation endpoint - implement JWT validation");
        return ResponseEntity.ok(response);
    }
} 