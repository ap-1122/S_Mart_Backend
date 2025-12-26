package com.example.demo.dto;

public class LoginRequest {
    
    private String email;
    private String password;

    // 1. Empty Constructor
    public LoginRequest() {
    }

    // 2. Full Constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 3. Manual Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}