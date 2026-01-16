package com.example.demo.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails { // ✅ 1. Implements UserDetails added

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
    
    @Column(unique = true)
    private String phoneNumber;

    // ✅ 2. New Role Field (Default "USER")
    private String role = "USER"; 

    // --- CONSTRUCTORS ---
    public UserModel() {}

    public UserModel(String email, String username, String authProvider) {
        this.email = email;
        this.username = username;
        this.authProvider = authProvider;
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // ✅ 3. Role Getter/Setter
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // --- USER DETAILS METHODS (Spring Security ke liye zaroori) ---
    
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Ye method batata hai ki user ADMIN hai ya USER
//        return Collections.singletonList(new SimpleGrantedAuthority(role));
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SAFETY CHECK: Agar role null hai, to crash mat ho, "USER" maan lo
        String safeRole = (role == null || role.isEmpty()) ? "USER" : role;
        
        return Collections.singletonList(new SimpleGrantedAuthority(safeRole));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}


















//adding role in the application 
// package com.example.demo.model;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "users")
//public class UserModel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column
//    private String password;
//
//    private String username;
//
//    @Column(name = "auth_provider")
//    private String authProvider;
//    
//    @Column(unique = true) // Phone number bhi unique hona chahiye
//    private String phoneNumber;
//
//    // 1. Empty Constructor (JPA ke liye zaroori)
//    public UserModel() {
//    }
//
//    // 2. Full Constructor
//    public UserModel(String email, String username, String authProvider) {
//        this.email = email;
//        this.username = username;
//        this.authProvider = authProvider;
//    }
//
//    // 3. MANUAL GETTERS AND SETTERS (Inke bina 'setEmail' error aayega)
//    
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; } // Ye method zaroori hai
//
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//
//    public String getAuthProvider() { return authProvider; }
//    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
//    
//    public String getPhoneNumber() { return phoneNumber; }
//    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
//}