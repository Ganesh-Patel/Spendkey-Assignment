package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.AuthResponse;
import com.ecommerce.backend.dto.LoginRequest;
import com.ecommerce.backend.dto.SignupRequest;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.UserRole;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     */
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse("Username already exists");
        }

        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            return new AuthResponse("Mobile number already registered");
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty() &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.USER);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getId(), savedUser.getRole().name());

        return new AuthResponse(savedUser.getId(), savedUser.getUsername(),
                savedUser.getEmail(), savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getMobileNumber(), savedUser.getRole(), token);
    }

    /**
     * Authenticate user login
     */
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsernameOrMobile())
                .or(() -> userRepository.findByMobileNumber(request.getUsernameOrMobile()));

        if (userOpt.isEmpty()) {
            return new AuthResponse("Invalid username/mobile number or password");
        }

        User user = userOpt.get();

        if (!user.getIsActive()) {
            return new AuthResponse("Account is deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid username/mobile number or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());

        return new AuthResponse(
                user.getId(), user.getUsername(),
                user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getMobileNumber(), user.getRole(),
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

        // Check if username is already taken by another user
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            return new AuthResponse("Username already exists");
        }

        // Check if mobile number is already taken by another user
        existingUser = userRepository.findByMobileNumber(request.getMobileNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            return new AuthResponse("Mobile number already registered");
        }

        // Check if email is already taken by another user
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                return new AuthResponse("Email already registered");
            }
        }

        user.setUsername(request.getUsername());
        user.setMobileNumber(request.getMobileNumber());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        // Generate new JWT token
        String token = jwtUtil.generateToken(updatedUser.getUsername(), updatedUser.getId(), updatedUser.getRole().name());

        return new AuthResponse(user.getId(), user.getUsername(),
                user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getMobileNumber(), user.getRole(), token);
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
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return new AuthResponse("Account deactivated successfully");
    }
} 