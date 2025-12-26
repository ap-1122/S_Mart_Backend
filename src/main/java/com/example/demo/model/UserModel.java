 package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    private String username;

    @Column(name = "auth_provider")
    private String authProvider;
    
    @Column(unique = true) // Phone number bhi unique hona chahiye
    private String phoneNumber;

    // 1. Empty Constructor (JPA ke liye zaroori)
    public UserModel() {
    }

    // 2. Full Constructor
    public UserModel(String email, String username, String authProvider) {
        this.email = email;
        this.username = username;
        this.authProvider = authProvider;
    }

    // 3. MANUAL GETTERS AND SETTERS (Inke bina 'setEmail' error aayega)
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; } // Ye method zaroori hai

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}