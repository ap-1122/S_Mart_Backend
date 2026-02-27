package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List; // ✅ Import Added
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // React URL
public class UserController {

    @Autowired
    private UserService userService;

    // Helper: Token se Email nikalne ke liye
    private String getEmailFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserModel) {
            return ((UserModel) principal).getEmail();
        }
        return principal.toString();
    }

    // --- EXISTING METHODS (No Changes) ---

    // 1. Get Profile Details
    @GetMapping("/profile")
    public ResponseEntity<UserModel> getProfile(Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        UserModel user = userService.getUserProfile(email);
        
        user.setPassword(null); // Security
        return ResponseEntity.ok(user);
    }

    // 2. Update Profile
    @PutMapping("/profile")
    public ResponseEntity<UserModel> updateProfile(@RequestBody Map<String, String> requestData, Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        
        String newName = requestData.get("username");
        String newPhone = requestData.get("phoneNumber");

        UserModel updatedUser = userService.updateUserProfile(email, newName, newPhone);
        
        updatedUser.setPassword(null);
        return ResponseEntity.ok(updatedUser);
    }

    // ==========================================
    // 🚀 NEW: ADMIN ENDPOINTS (ADDED BELOW)
    // ==========================================

    // 3. Get All Users (Admin Only)
    // URL: GET http://localhost:8080/api/users/all
    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        
        // Loop chalakar sabke passwords null kar do taaki frontend pe hash na dikhe
        users.forEach(u -> u.setPassword(null));
        
        return ResponseEntity.ok(users);
    }

    // 4. Update Role by ID (Admin Only)
    // URL: PUT http://localhost:8080/api/users/{id}/role?role=SELLER
    @PutMapping("/{id}/role")
    public ResponseEntity<UserModel> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        
        UserModel updatedUser = userService.updateUserRole(id, role);
        
        updatedUser.setPassword(null);
        return ResponseEntity.ok(updatedUser);
    }
}

















//ADDED NEW METHOD IN UPPER CODE JISSE ADMIN SARE ROLE LE SAKE AND MODIFY KAR SAKE MEANS ROLE DESIDE KAR SAKE MANUAL NA KARNA PADE 

// package com.example.demo.controller;
//
//import com.example.demo.model.UserModel;
//import com.example.demo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:5173") // React URL
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    // Helper: Token se Email nikalne ke liye (Security Context se)
//    private String getEmailFromAuth(Authentication authentication) {
//        // Spring Security user details ko Principal me rakhta hai
//        Object principal = authentication.getPrincipal();
//        
//        if (principal instanceof UserModel) {
//            return ((UserModel) principal).getEmail();
//        }
//        // Kabhi kabhi ye sirf String (email) return karta hai structure ke hisab se
//        return principal.toString();
//    }
//
//    // 1. Get Profile Details
//    // URL: GET http://localhost:8080/api/users/profile
//    @GetMapping("/profile")
//    public ResponseEntity<UserModel> getProfile(Authentication authentication) {
//        String email = getEmailFromAuth(authentication);
//        UserModel user = userService.getUserProfile(email);
//        
//        // Password frontend pe mat bhejo (Security Best Practice)
//        user.setPassword(null); 
//        
//        return ResponseEntity.ok(user);
//    }
//
//    // 2. Update Profile
//    // URL: PUT http://localhost:8080/api/users/profile
//    @PutMapping("/profile")
//    public ResponseEntity<UserModel> updateProfile(@RequestBody Map<String, String> requestData, Authentication authentication) {
//        String email = getEmailFromAuth(authentication);
//        
//        String newName = requestData.get("username");
//        String newPhone = requestData.get("phoneNumber");
//
//        UserModel updatedUser = userService.updateUserProfile(email, newName, newPhone);
//        
//        updatedUser.setPassword(null); // Return karte waqt password chhupa do
//        return ResponseEntity.ok(updatedUser);
//    }
//}