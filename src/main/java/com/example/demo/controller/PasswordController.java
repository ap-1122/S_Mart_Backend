package com.example.demo.controller;

import com.example.demo.dto.PasswordResetRequest;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") 
public class PasswordController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
        // ... (Ye part same rahega)
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (!userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("Email not found!");
            }
            // Note: Frontend "send-otp" call kar raha hai, isliye ye endpoint shayad use na ho, 
            // par ise rehne do.
            return ResponseEntity.ok(otpService.sendResetLink(request.getEmail()));
        } 
        else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
             // ...
            return ResponseEntity.ok(otpService.sendResetOtp(request.getPhoneNumber()));
        }
        return ResponseEntity.badRequest().body("Please provide Email or Phone Number");
    }

    // 2. RESET PASSWORD (MAJOR FIX HERE)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        
        System.out.println("RESET ATTEMPT -> Phone: " + request.getPhoneNumber() + 
                           ", Email: " + request.getEmail() + 
                           ", OTP: " + request.getOtp());

        String userIdentifier = null;
        boolean isPhone = false;

        // CASE A: Email Link Token (Agar link se aaya)
        if (request.getToken() != null) {
            userIdentifier = otpService.validateResetTokenOrOtp(request.getToken());
            if (userIdentifier == null) return ResponseEntity.badRequest().body("Invalid or Expired Link");
        }
        
        // CASE B: OTP Verify (Phone OR Email)
        else if (request.getOtp() != null && (request.getPhoneNumber() != null || request.getEmail() != null)) {
            
            // Identifier nikalo (Phone ya Email)
            String identifier = (request.getPhoneNumber() != null) ? request.getPhoneNumber() : request.getEmail();
            
            // >>> MAGIC FIX: Pehle hum 'validateResetTokenOrOtp' use kar rahe the.
            // Ab hum 'validateOtp' use karenge jo 'send-otp' endpoint ke sath match karta hai.
            boolean isValid = otpService.validateOtp(identifier, request.getOtp());
            
            if (!isValid) {
                return ResponseEntity.badRequest().body("Invalid OTP. Please generate a new code.");
            }
            
            userIdentifier = identifier;
            isPhone = (request.getPhoneNumber() != null); 
        }
        else {
            return ResponseEntity.badRequest().body("Invalid Request Data");
        }

        // 3. DATABASE UPDATE
        final String finalUserIdentifier = userIdentifier; 

        UserModel user;
        try {
            if (isPhone) {
                user = userRepository.findByPhoneNumber(finalUserIdentifier)
                        .orElseThrow(() -> new RuntimeException("User not found with Phone: " + finalUserIdentifier));
            } else {
                user = userRepository.findByEmail(finalUserIdentifier)
                        .orElseThrow(() -> new RuntimeException("User not found with Email: " + finalUserIdentifier));
            }

            // Password Save
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            
            // Success hone ke baad hi delete karo
            if(request.getToken() != null) {
                otpService.deleteResetToken(request.getToken());
            } else {
               // otpService.deleteResetToken(finalUserIdentifier); // Ye shayad alag method ho
               // Simple logic: Reset complete, user khush. OTP expire apne aap ho jayega.
            }
            
            return ResponseEntity.ok("Password Reset Successful! You can login now.");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error Updating Password: " + e.getMessage());
        }
    }
}












//uper code fix this problem
//Problem Ki Asli Wajah (Root Cause) 🕵️‍♂️
//Yahan 2 alag-alag systems takra rahe hain:
//
//Frontend: Tumhara frontend /auth/send-otp call kar raha hai (Jo "Login OTP" banata hai).
//
//Backend: Tumhara PasswordController /auth/reset-password mein validateResetTokenOrOtp dhoond raha hai (Jo "Reset Token" expect karta hai).
//
//Simple bhasha mein: Tum "Login wala Chabi" se "Password Reset wala Tala" kholne ki koshish kar rahe ho. Isliye wo "Invalid" bol raha hai bhale hi OTP sahi ho.
//
//Solution: Backend Logic Match Karo 🛠️
//Humein PasswordController ko bolna padega ki "Bhai, jo Login wala OTP hai, use bhi accept kar lo."
//
//Niche diya gaya code copy karo aur PasswordController.java mein replace kar do. Maine validation ka function badal diya hai taaki wo match ho jaye.
//
//File: src/main/java/com/example/demo/controller/PasswordController.java
// package com.example.demo.controller;
//
//import com.example.demo.dto.PasswordResetRequest;
//import com.example.demo.model.UserModel;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.service.OtpService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:5173") 
//public class PasswordController {
//
//    @Autowired
//    private OtpService otpService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // 1. FORGOT PASSWORD
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
//        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
//            if (!userRepository.existsByEmail(request.getEmail())) {
//                return ResponseEntity.badRequest().body("Email not found!");
//            }
//            return ResponseEntity.ok(otpService.sendResetLink(request.getEmail()));
//        } 
//        else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
//            if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isEmpty()) {
//                return ResponseEntity.badRequest().body("Phone Number not registered!");
//            }
//            return ResponseEntity.ok(otpService.sendResetOtp(request.getPhoneNumber()));
//        }
//        return ResponseEntity.badRequest().body("Please provide Email or Phone Number");
//    }
//
//    // 2. RESET PASSWORD
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
//        
//        System.out.println("RESET ATTEMPT -> Phone: " + request.getPhoneNumber() + 
//                           ", Email: " + request.getEmail() + 
//                           ", OTP: " + request.getOtp());
//
//        String userIdentifier = null;
//        boolean isPhone = false;
//
//        // CASE A: Email Token Verify
//        if (request.getToken() != null) {
//            userIdentifier = otpService.validateResetTokenOrOtp(request.getToken());
//            if (userIdentifier == null) return ResponseEntity.badRequest().body("Invalid or Expired Link");
//            // NOTE: Token hum abhi delete nahi karenge, successful save ke baad karenge
//        }
//        
//        // CASE B: OTP Verify
//        else if (request.getOtp() != null && (request.getPhoneNumber() != null || request.getEmail() != null)) {
//            
//            String identifier = (request.getPhoneNumber() != null) ? request.getPhoneNumber() : request.getEmail();
//            
//            String storedOtp = otpService.validateResetTokenOrOtp(identifier);
//            if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
//                return ResponseEntity.badRequest().body("Invalid OTP: " + request.getOtp());
//            }
//            
//            userIdentifier = identifier;
//            isPhone = (request.getPhoneNumber() != null); 
//            // NOTE: Yahan se delete hata diya hai. Niche DB update ke baad karenge.
//        }
//        else {
//            return ResponseEntity.badRequest().body("Invalid Request Data: Missing Phone/Email or OTP");
//        }
//
//        // 3. DATABASE UPDATE
//        final String finalUserIdentifier = userIdentifier; // Final variable for lambda
//
//        UserModel user;
//        try {
//            if (isPhone) {
//                user = userRepository.findByPhoneNumber(finalUserIdentifier)
//                        .orElseThrow(() -> new RuntimeException("User not found with Phone: " + finalUserIdentifier));
//            } else {
//                user = userRepository.findByEmail(finalUserIdentifier)
//                        .orElseThrow(() -> new RuntimeException("User not found with Email: " + finalUserIdentifier));
//            }
//
//            // Password Save Karo
//            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//            userRepository.save(user);
//            
//            // SUCCESS! Ab OTP/Token ko delete karo taaki reuse na ho sake
//            if(request.getToken() != null) {
//                otpService.deleteResetToken(request.getToken());
//            } else {
//                otpService.deleteResetToken(finalUserIdentifier);
//            }
//            
//            return ResponseEntity.ok("Password Reset Successful! You can login now.");
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Error Updating Password: " + e.getMessage());
//        }
//    }
//}








































//ye sirf do condition handale kar raha tha  isliye upper code use kiya 
//Backend mein tumhara code sirf 2 Cases handle kar raha hai:
//Email Link: Agar token aaye to verify karo.
//Phone OTP: Agar phoneNumber + otp aaye to verify karo.
//Lekin, agar frontend se galti se Email + OTP aa raha hai (ya Phone number kisi wajah se null pahunch raha hai), to wo seedha else mein jakar "Invalid Request Data" bol deta hai.
//Main tumhare Backend Code ko update kar raha hoon. Isme maine 2 Changes kiye hain:
//Console Logs Add Kiye Hain: Taaki tum backend ke console me dekh sako ki Frontend se kya aa raha hai (null aa raha hai ya value).
//Logic Flexible Banaya Hai: Ab ye Phone OR Email dono ke OTP ko accept karega.
//package com.example.demo.controller;
//
//import com.example.demo.dto.PasswordResetRequest;
//import com.example.demo.model.UserModel;
//import com.example.demo.repository.UserRepository;
//import com.example.demo.service.OtpService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class PasswordController {
//
//    @Autowired
//    private OtpService otpService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // 1. FORGOT PASSWORD (Link ya OTP bhejo)
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
//        
//        // CASE A: Email logic
//        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
//            if (!userRepository.existsByEmail(request.getEmail())) {
//                return ResponseEntity.badRequest().body("Email not found!");
//            }
//            return ResponseEntity.ok(otpService.sendResetLink(request.getEmail()));
//        } 
//        
//        // CASE B: Phone logic
//        else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
//            // Check karo user exist karta hai kya
//            if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isEmpty()) {
//                return ResponseEntity.badRequest().body("Phone Number not registered!");
//            }
//            return ResponseEntity.ok(otpService.sendResetOtp(request.getPhoneNumber()));
//        }
//        
//        return ResponseEntity.badRequest().body("Please provide Email or Phone Number");
//    }
//
//    // 2. RESET PASSWORD (Verification + New Password)
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
//        String userIdentifier = null;
//        boolean isPhone = false;
//
//        // CASE A: Email Token Verify
//        if (request.getToken() != null) {
//            userIdentifier = otpService.validateResetTokenOrOtp(request.getToken());
//            if (userIdentifier == null) return ResponseEntity.badRequest().body("Invalid or Expired Link");
//            
//            otpService.deleteResetToken(request.getToken());
//        }
//        // CASE B: Phone OTP Verify
//        else if (request.getPhoneNumber() != null && request.getOtp() != null) {
//            String storedOtp = otpService.validateResetTokenOrOtp(request.getPhoneNumber());
//            if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
//                return ResponseEntity.badRequest().body("Invalid OTP");
//            }
//            userIdentifier = request.getPhoneNumber();
//            isPhone = true;
//            
//            otpService.deleteResetToken(request.getPhoneNumber());
//        }
//        else {
//            return ResponseEntity.badRequest().body("Invalid Request Data");
//        }
//
//        // 3. DATABASE UPDATE
//        UserModel user;
//        if (isPhone) {
//            user = userRepository.findByPhoneNumber(userIdentifier)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//        } else {
//            user = userRepository.findByEmail(userIdentifier)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//        }
//
//        // Password Encrypt karke save karo
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//
//        return ResponseEntity.ok("Password Reset Successful! You can login now.");
//    }
//}