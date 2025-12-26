 package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.GoogleLoginRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.GoogleAuthService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//rossOrigin(origins = "http://localhost:3000") // Frontend connection
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(GoogleAuthService googleAuthService, UserService userService, UserRepository userRepository) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 1. GOOGLE LOGIN
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        String jwtToken = googleAuthService.authenticateGoogleUser(request.getToken());
        return ResponseEntity.ok(new AuthResponse(jwtToken, "Login Successful", "Google User"));
    }

    // 2. NORMAL REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // 3. NORMAL LOGIN (Smart Error Handling)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Token generate karne ki koshish karo
            String token = userService.login(request);

            // User ka naam database se nikalo
            UserModel user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Sab sahi hai -> 200 OK bhejo
            return ResponseEntity.ok(new AuthResponse(token, "Login Successful", user.getUsername()));

        } catch (RuntimeException e) {
            // Agar UserService ne "Invalid Email" bola
            if (e.getMessage().equals("Invalid Email")) {
                return ResponseEntity.status(404).body("User not found! Please Register.");
            }
            // Agar UserService ne "Invalid Password" bola
            return ResponseEntity.status(401).body("Invalid Password!");
        }
    }
}