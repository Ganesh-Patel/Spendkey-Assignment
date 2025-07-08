package com.ecommerce.backend.dto;

import com.ecommerce.backend.entity.UserRole;
import java.time.LocalDateTime;

public class AuthResponse {
    
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private UserRole role;
    private String token;
    private LocalDateTime loginTime;
    private String message;
    
    // Default constructor
    public AuthResponse() {
        this.loginTime = LocalDateTime.now();
    }
    
    // Constructor for successful login
    public AuthResponse(Long userId, String username, String email, String firstName, 
                       String lastName, String mobileNumber, UserRole role, String token) {
        this();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.token = token;
        this.message = "Authentication successful";
    }
    
    // Constructor for signup success
    public AuthResponse(Long userId, String username, String message) {
        this();
        this.userId = userId;
        this.username = username;
        this.message = message;
    }
    
    // Constructor for error response
    public AuthResponse(String message) {
        this();
        this.message = message;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMobileNumber() {
        return mobileNumber;
    }
    
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", role=" + role +
                ", loginTime=" + loginTime +
                ", message='" + message + '\'' +
                '}';
    }
} 