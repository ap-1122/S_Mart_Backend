 package com.example.demo.dto;

public class GoogleLoginRequest {
    
    private String token;

    // 1. Empty Constructor
    public GoogleLoginRequest() {
    }

    // 2. Full Constructor
    public GoogleLoginRequest(String token) {
        this.token = token;
    }

    // 3. Getter (Ye wala method missing tha!)
    public String getToken() {
        return token;
    }

    // 4. Setter
    public void setToken(String token) {
        this.token = token;
    }
}