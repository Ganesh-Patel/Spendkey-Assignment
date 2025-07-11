package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.AuthResponse;
import com.ecommerce.backend.dto.LoginRequest;
import com.ecommerce.backend.dto.SignupRequest;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.security.JwtUtil;
import com.ecommerce.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * POST /auth/signup - Register a new user
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        if (response.getMessage().equals("Authentication successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * POST /auth/login - Authenticate user login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        if (response.getMessage().equals("Authentication successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * GET /auth/profile/{userId} - Get user profile
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getProfile(@PathVariable Long userId) {
        User user = authService.getUserById(userId);
        if (user != null) {
            // Don't return password
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /auth/profile/{userId} - Update user profile
     */
    @PutMapping("/profile/{userId}")
    public ResponseEntity<AuthResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.updateProfile(userId, request);
        if (response.getMessage().equals("Authentication successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * DELETE /auth/profile/{userId} - Deactivate user account
     */
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<AuthResponse> deactivateAccount(@PathVariable Long userId) {
        AuthResponse response = authService.deactivateAccount(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /auth/check-username/{username} - Check if username is available
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok(user == null);
    }

    /**
     * POST /auth/validate-token - Validate authentication token
     */
    @PostMapping("/validate-token")
    public ResponseEntity<AuthResponse> validateToken(@RequestParam String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);
            
            if (username != null && userId != null && role != null && jwtUtil.validateToken(token, username)) {
                User user = authService.getUserById(userId);
                if (user != null && user.getIsActive()) {
                    return ResponseEntity.ok(new AuthResponse(userId, username, 
                            user.getEmail(), user.getFirstName(), user.getLastName(),
                            user.getMobileNumber(), user.getRole(), token));
                }
            }
            return ResponseEntity.badRequest().body(new AuthResponse("Invalid or expired token"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Invalid token format"));
        }
    }
} 