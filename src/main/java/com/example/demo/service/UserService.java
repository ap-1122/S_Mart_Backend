 package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List; // ✅ Import Added

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    // --- EXISTING METHODS (No Changes) ---

    // Registration Logic
    public String register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            return "Email already registered!";
        }

        UserModel user = new UserModel();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhoneNumber(req.getPhone()); 
        user.setAuthProvider("LOCAL"); 
        
        // Default Role 'USER'
        user.setRole("USER");

        userRepository.save(user);

        return "Registered Successfully!";
    }

    // Login Logic
    public String login(LoginRequest req) { 

        UserModel user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        // Return Token with Role
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
    
    // Get Profile (Self)
    public UserModel getUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update Profile (Self)
    public UserModel updateUserProfile(String email, String newName, String newPhone) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newName != null && !newName.trim().isEmpty()) {
            user.setUsername(newName);
        }
        
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            user.setPhoneNumber(newPhone);
        }

        return userRepository.save(user);
    }

    // ==========================================
    // 🚀 NEW: ADMIN FEATURES (ADDED BELOW)
    // ==========================================

    // 1. Get All Users (For Admin Panel)
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Update User Role (Admin can change USER -> SELLER -> ADMIN)
    public UserModel updateUserRole(Long userId, String newRole) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        user.setRole(newRole); // Role Update
        return userRepository.save(user);
    }
}














//IN UPPER CODE WE UPDATE JISSE ADMIN SARE ROLE KO ACCESS KAR SAKE AND CAHNGE BHI

//package com.example.demo.service;
//
//import com.example.demo.dto.LoginRequest;
//import com.example.demo.dto.RegisterRequest;
//import com.example.demo.model.UserModel;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.security.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // Registration Logic
//    public String register(RegisterRequest req) {
//
//        if (userRepository.existsByEmail(req.getEmail())) {
//            return "Email already registered!";
//        }
//
//        UserModel user = new UserModel();
//        user.setEmail(req.getEmail());
//        user.setUsername(req.getUsername());
//        user.setPassword(passwordEncoder.encode(req.getPassword()));
//        user.setPhoneNumber(req.getPhone()); 
//        user.setAuthProvider("LOCAL"); 
//        
//        // Default Role 'USER' UserModel me already set hai, 
//        // par safety ke liye explicit set kar sakte hain
//        user.setRole("USER");
//
//        userRepository.save(user);
//
//        return "Registered Successfully!";
//    }
//
//    // Login Logic
//    public String login(LoginRequest req) { 
//
//        UserModel user = userRepository.findByEmail(req.getEmail())
//                .orElseThrow(() -> new RuntimeException("Invalid Email"));
//
//        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid Password");
//        }
//
//        // ✅ FIX: Yahan Error aa raha tha.
//        // Ab hum Email ke saath Role bhi bhej rahe hain
//        return jwtUtil.generateToken(user.getEmail(), user.getRole());
//    }
//    
//    // Get Profile
//    public UserModel getUserProfile(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    // Update Profile
//    public UserModel updateUserProfile(String email, String newName, String newPhone) {
//        UserModel user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (newName != null && !newName.trim().isEmpty()) {
//            user.setUsername(newName);
//        }
//        
//        if (newPhone != null && !newPhone.trim().isEmpty()) {
//            user.setPhoneNumber(newPhone);
//        }
//
//        return userRepository.save(user);
//    }
//}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////updating role in the token in upper code 
//// package com.example.demo.service;
////
////import com.example.demo.dto.LoginRequest; // Naya DTO import kiya
////import com.example.demo.dto.RegisterRequest;
////import com.example.demo.model.UserModel;
////import com.example.demo.repository.UserRepository;
////import com.example.demo.security.JwtUtil;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.stereotype.Service;
////
////@Service
////public class UserService {
////
////    @Autowired
////    private UserRepository userRepository;
////    
////    @Autowired
////    private PasswordEncoder passwordEncoder;
////    
////    @Autowired
////    private JwtUtil jwtUtil;
////
////    // Registration Logic
////    public String register(RegisterRequest req) {
////
////        if (userRepository.existsByEmail(req.getEmail())) {
////            return "Email already registered!";
////        }
////
////        // FIX: Builder hata diya, Manual 'new' keyword use kar rahe hain
////        UserModel user = new UserModel();
////        user.setEmail(req.getEmail());
////        user.setUsername(req.getUsername());
////        user.setPassword(passwordEncoder.encode(req.getPassword()));
////
////        // Pehle hum phone number save nahi kar rahe the, ab kar rahe hain
////        user.setPhoneNumber(req.getPhone()); 
////        // -------------------------------------------
////
////        user.setAuthProvider("LOCAL"); // Normal registration
////
////        userRepository.save(user);
////
////        return "Registered Successfully!";
////    }
////
////    // Login Logic
////    public String login(LoginRequest req) { // Yahan 'LoginRequest' use ho raha hai
////
////        UserModel user = userRepository.findByEmail(req.getEmail())
////                .orElseThrow(() -> new RuntimeException("Invalid Email"));
////
////        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
////            throw new RuntimeException("Invalid Password");
////        }
////
////        return jwtUtil.generateToken(user.getEmail());
////    }
////    
////    
////    //new update 
//// // 1. Profile Data lane ke liye
////    public UserModel getUserProfile(String email) {
////        return userRepository.findByEmail(email)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////    }
////
////    // 2. Profile Update karne ke liye
////    public UserModel updateUserProfile(String email, String newName, String newPhone) {
////        UserModel user = userRepository.findByEmail(email)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        // Sirf wahi update karo jo user ne bheja hai (null check)
////        if (newName != null && !newName.trim().isEmpty()) {
////            user.setUsername(newName);
////        }
////        
////        if (newPhone != null && !newPhone.trim().isEmpty()) {
////            user.setPhoneNumber(newPhone);
////        }
////
////        // Email aur Password hum yahan update nahi kar rahe (Security reasons)
////        return userRepository.save(user);
////    }
////}