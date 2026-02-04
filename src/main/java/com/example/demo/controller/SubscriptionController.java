 package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SubscriptionController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private UserRepository userRepository;

    // Helper: Auth se Email nikalo
    private String getEmailFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserModel) return ((UserModel) principal).getEmail();
        return principal.toString();
    }

    // 1. Create Order for Subscription (Fixed Price: ₹499)
    @PostMapping("/create-order")
    public ResponseEntity<?> createSubscriptionOrder() {
        try {
            int amount = 499 * 100; // ₹499 in paise
            
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "sub_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);
            return ResponseEntity.ok(order.toString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    // 2. Success Handler (Jab Payment ho jaye)
    @PostMapping("/success")
    public ResponseEntity<?> activateSubscription(Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        UserModel user = userRepository.findByEmail(email).orElseThrow();

        // Update User Status
        user.setIsPremium(true);
        user.setSubscriptionExpiry(LocalDate.now().plusYears(1)); // 1 Saal ki validity
        
        userRepository.save(user);

        return ResponseEntity.ok("Subscription Activated Successfully! 🎉");
    }
    
    // 3. Check Status
    @GetMapping("/status")
    public ResponseEntity<?> getStatus(Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        UserModel user = userRepository.findByEmail(email).orElseThrow();
        
        boolean isActive = user.hasActiveSubscription();
        return ResponseEntity.ok(Map.of(
            "isPremium", isActive,
            "expiry", user.getSubscriptionExpiry() != null ? user.getSubscriptionExpiry() : "NA"
        ));
    }
}
