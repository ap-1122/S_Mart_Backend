 package com.example.demo.service;

import com.example.demo.dto.LoginRequest; // Naya DTO import kiya
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Registration Logic
    public String register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            return "Email already registered!";
        }

        // FIX: Builder hata diya, Manual 'new' keyword use kar rahe hain
        UserModel user = new UserModel();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // Pehle hum phone number save nahi kar rahe the, ab kar rahe hain
        user.setPhoneNumber(req.getPhone()); 
        // -------------------------------------------

        user.setAuthProvider("LOCAL"); // Normal registration

        userRepository.save(user);

        return "Registered Successfully!";
    }

    // Login Logic
    public String login(LoginRequest req) { // Yahan 'LoginRequest' use ho raha hai

        UserModel user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}