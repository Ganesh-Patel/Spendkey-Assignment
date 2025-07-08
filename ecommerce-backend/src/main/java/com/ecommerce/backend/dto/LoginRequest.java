package com.ecommerce.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "Username or mobile number is required")
    private String usernameOrMobile;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Default constructor
    public LoginRequest() {}
    
    // Constructor with required fields
    public LoginRequest(String usernameOrMobile, String password) {
        this.usernameOrMobile = usernameOrMobile;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsernameOrMobile() {
        return usernameOrMobile;
    }
    
    public void setUsernameOrMobile(String usernameOrMobile) {
        this.usernameOrMobile = usernameOrMobile;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "LoginRequest{" +
                "usernameOrMobile='" + usernameOrMobile + '\'' +
                '}';
    }
} 