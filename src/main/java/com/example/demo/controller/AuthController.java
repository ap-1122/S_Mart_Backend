package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.GoogleLoginRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil; // ✅ IMPORT ZAROORI HAI
import com.example.demo.service.GoogleAuthService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") 
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil; // ✅ 1. Yahan Variable Declare kiya

    // ✅ 2. Constructor me JwtUtil inject kiya
    @Autowired
    public AuthController(GoogleAuthService googleAuthService, 
                          UserService userService, 
                          UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 1. GOOGLE LOGIN (FIXED)
    
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        
        // Step 1: Google verify karo aur agar user nahi hai to bana do (Auto-Register)
        String jwtToken = googleAuthService.authenticateGoogleUser(request.getToken());

        // Step 2: Token se Email nikalo
        String email = jwtUtil.extractEmail(jwtToken);

        // Step 3: Database se user nikalo (Ye ab pakka milega)
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found (Should not happen)"));

        // Step 4: Sahi naam bhejo with role 
        return ResponseEntity.ok(new AuthResponse(jwtToken, "Login Successful", user.getUsername(), user.getRole()));
    }

    // 2. NORMAL REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // 3. NORMAL LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.login(request);
            UserModel user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(new AuthResponse(token, "Login Successful", user.getUsername(), user.getRole()));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Invalid Email")) {
                return ResponseEntity.status(404).body("User not found! Please Register.");
            }
            return ResponseEntity.status(401).body("Invalid Password!");
        }
    }
}
















//this code update only to update  first step-> google login, and remaining are same 
// package com.example.demo.controller;
//
//import com.example.demo.dto.AuthResponse;
//import com.example.demo.dto.GoogleLoginRequest;
//import com.example.demo.dto.LoginRequest;
//import com.example.demo.dto.RegisterRequest;
//import com.example.demo.model.UserModel;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.service.GoogleAuthService;
//import com.example.demo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
////rossOrigin(origins = "http://localhost:3000") // Frontend connection
//public class AuthController {
//
//    private final GoogleAuthService googleAuthService;
//    private final UserService userService;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public AuthController(GoogleAuthService googleAuthService, UserService userService, UserRepository userRepository) {
//        this.googleAuthService = googleAuthService;
//        this.userService = userService;
//        this.userRepository = userRepository;
//    }
//
//    // 1. GOOGLE LOGIN   -this is basic google login so we update this in upper code -jisse hame google userke place pe user name mile 
//    @PostMapping("/google")
//    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
//        String jwtToken = googleAuthService.authenticateGoogleUser(request.getToken());
//        return ResponseEntity.ok(new AuthResponse(jwtToken, "Login Successful", "Google User"));
//    }
// 
//    // 2. NORMAL REGISTER
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(userService.register(request));
//    }
//
//    // 3. NORMAL LOGIN (Smart Error Handling)
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            // Token generate karne ki koshish karo
//            String token = userService.login(request);
//
//            // User ka naam database se nikalo
//            UserModel user = userRepository.findByEmail(request.getEmail())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Sab sahi hai -> 200 OK bhejo
//            return ResponseEntity.ok(new AuthResponse(token, "Login Successful", user.getUsername()));
//
//        } catch (RuntimeException e) {
//            // Agar UserService ne "Invalid Email" bola
//            if (e.getMessage().equals("Invalid Email")) {
//                return ResponseEntity.status(404).body("User not found! Please Register.");
//            }
//            // Agar UserService ne "Invalid Password" bola
//            return ResponseEntity.status(401).body("Invalid Password!");
//        }
//    }
//}