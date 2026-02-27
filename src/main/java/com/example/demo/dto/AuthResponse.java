package com.example.demo.dto;

public class AuthResponse {
    private String token;
    private String message;
    private String username; // <-- Ye missing tha

 // ✅ NEW FIELD
    private String role; 

    // ✅ UPDATED CONSTRUCTOR
    public AuthResponse(String token, String message, String username, String role) {
        this.token = token;
        this.message = message;
        this.username = username;
        this.role = role;
    }
    
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
 // ✅ Role Getter Setter
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

















//update code to set role 

// package com.example.demo.dto;
//
//public class AuthResponse {
//    private String token;
//    private String message;
//    private String username; // <-- Ye missing tha
//
//    // 3 Arguments wala Constructor (Jo AuthController dhoond raha hai)
//    public AuthResponse(String token, String message, String username) {
//        this.token = token;
//        this.message = message;
//        this.username = username;
//    }
//
//    // Getters and Setters
//    public String getToken() { return token; }
//    public void setToken(String token) { this.token = token; }
//
//    public String getMessage() { return message; }
//    public void setMessage(String message) { this.message = message; }
//
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//}