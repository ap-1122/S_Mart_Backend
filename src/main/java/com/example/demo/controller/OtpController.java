 package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.OtpRequest;
import com.example.demo.dto.OtpVerificationRequest;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
// Frontend ke liye CORS add kar diya hai taaki connection issue na aaye
@CrossOrigin(origins = "http://localhost:5173") 
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. SEND OTP API (Email ya Phone par)
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        String response;
        
        // Agar Email hai to Email par bhejo
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            response = otpService.sendEmailOtp(request.getEmail());
        } 
        // Agar Phone hai to SMS bhejo
        else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            response = otpService.sendSmsOtp(request.getPhoneNumber());
        } 
        else {
            return ResponseEntity.badRequest().body("Please provide Email or Phone Number");
        }
        
        return ResponseEntity.ok(response);
    }

    // 2. VERIFY OTP & LOGIN API
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpVerificationRequest request) {
        
        String identifier = request.getEmail(); // Default Email maan rahe hain
        if (identifier == null) {
            identifier = request.getPhoneNumber(); // Agar email null hai to phone use karo
        }

        // 1. OTP Check karo (Redis se)
        boolean isValid = otpService.validateOtp(identifier, request.getOtp());

        if (!isValid) {
            // FIX: 3rd argument 'null' add kiya kyunki Username exist nahi karta error me
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Invalid or Expired OTP", null));
        }

        // 2. Agar OTP sahi hai, to User ko Database me dhundo ya save karo
        // (Note: Yahan hum maan rahe hain ki user pehle se register ho sakta hai ya naya ho sakta hai)
        String finalIdentifier = identifier;
        UserModel user = userRepository.findByEmail(finalIdentifier).orElseGet(() -> {
            UserModel newUser = new UserModel();
            newUser.setEmail(finalIdentifier);
            newUser.setAuthProvider("OTP_LOGIN");
            // Default username set kar rahe hain taaki null na jaye
            newUser.setUsername("Mobile User"); 
            return userRepository.save(newUser);
        });

        // 3. JWT Token generate karo
        String token = jwtUtil.generateToken(user.getEmail());

        // FIX: 3rd argument me 'user.getUsername()' pass kiya
        return ResponseEntity.ok(new AuthResponse(token, "Login Successful via OTP", user.getUsername()));
    }
}