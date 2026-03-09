 package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ReviewModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewController {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    // Helper: Get User from Token
    private UserModel getAuthenticatedUser(Authentication authentication) {
        String email = ((UserModel) authentication.getPrincipal()).getEmail();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ 1. Check Eligibility (Frontend button dikhane ke liye)
    @GetMapping("/can-review/{productId}")
    public ResponseEntity<Boolean> canReview(@PathVariable Long productId, Authentication authentication) {
        if (authentication == null) return ResponseEntity.ok(false);
        
        UserModel user = getAuthenticatedUser(authentication);
        
        // Rule 1: Kya product khareeda aur DELIVER hua hai?
        boolean bought = orderRepository.hasUserBoughtProduct(user.getId(), productId);
        
        // Rule 2: Kya pehle se review de chuka hai? (Duplicate not allowed)
        Product product = new Product(); product.setId(productId); // Dummy for check
        boolean alreadyReviewed = reviewRepository.existsByUserAndProduct(user, product);

        return ResponseEntity.ok(bought && !alreadyReviewed);
    }

    // ✅ 2. Add Review
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            Long productId = Long.valueOf(payload.get("productId").toString());
            
            // ✅ FIX: Rating ko safely parse karo
            int rating = 0;
            try {
                rating = Integer.parseInt(payload.get("rating").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body("Invalid Rating Format");
            }

            // 🛡️ SECURITY: 0 ya 5 se zyada rating mat ane do
            if (rating < 1 || rating > 5) {
                return ResponseEntity.status(400).body("Please select at least 1 star!");
            }

            String comment = payload.get("comment").toString();

            UserModel user = getAuthenticatedUser(authentication);
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

            // Check if purchased
            boolean bought = orderRepository.hasUserBoughtProduct(user.getId(), productId);
            if (!bought) {
                return ResponseEntity.status(403).body("You can only review products you have purchased and received.");
            }

            // Check duplicate
            if (reviewRepository.existsByUserAndProduct(user, product)) {
                return ResponseEntity.status(400).body("You have already reviewed this product.");
            }

            ReviewModel review = new ReviewModel(user, product, rating, comment);
            reviewRepository.save(review);

            return ResponseEntity.ok("Review Submitted Successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // ✅ 3. Get Reviews for a Product
    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewModel>> getProductReviews(@PathVariable Long productId) {
        Product product = new Product();
        product.setId(productId);
        return ResponseEntity.ok(reviewRepository.findByProductOrderByCreatedAtDesc(product));
    }
}
