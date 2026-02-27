package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.UserModel; // Tumhara UserModel import kiya
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Authentication import
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private CartService cartService;

    // Helper method to extract Email from Authentication
    private String getEmailFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        
        // Case 1: Agar principal direct UserModel object hai (Jo abhi ho raha hai)
        if (principal instanceof UserModel) {
            return ((UserModel) principal).getEmail();
        }
        // Case 2: Agar principal UserDetails hai (Standard Spring Security)
        /* else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
             return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } */
        
        // Case 3: Fallback (Agar string hai)
        return principal.toString();
    }

    // 1. Get My Cart
    @GetMapping("/")
    public ResponseEntity<Cart> getMyCart(Authentication authentication) {
        // Yahan hum helper method use kar rahe hain sahi email nikalne ke liye
        String email = getEmailFromAuth(authentication);
        Cart cart = cartService.getCart(email);
        return ResponseEntity.ok(cart);
    }

    // 2. Add Item to Cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestParam Long productId,
            @RequestParam Long variantId,
            @RequestParam int quantity,
            Authentication authentication) {
        
        String email = getEmailFromAuth(authentication);
        Cart updatedCart = cartService.addToCart(email, productId, variantId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // 3. Update Quantity
    @PutMapping("/update")
    public ResponseEntity<Cart> updateQuantity(
            @RequestParam Long cartItemId,
            @RequestParam int quantity,
            Authentication authentication) {
        
        String email = getEmailFromAuth(authentication);
        Cart updatedCart = cartService.updateQuantity(email, cartItemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // 4. Remove Item
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable Long cartItemId, 
            Authentication authentication) {
        
        String email = getEmailFromAuth(authentication);
        Cart updatedCart = cartService.removeFromCart(email, cartItemId);
        return ResponseEntity.ok(updatedCart);
    }
}



















// package com.example.demo.controller;
//
//import com.example.demo.model.Cart;
//import com.example.demo.service.CartService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("/api/cart")
//@CrossOrigin(origins = "http://localhost:5173") // React Frontend URL
//public class CartController {
//
//    @Autowired
//    private CartService cartService;
//
//    // 1. Get My Cart (Login wale user ka cart)
//    @GetMapping("/")
//    public ResponseEntity<Cart> getMyCart(Principal principal) {
//        // Principal se email nikalega jo JWT filter set karta hai
//        String email = principal.getName();
//        Cart cart = cartService.getCart(email);
//        return ResponseEntity.ok(cart);
//    }
//
//    // 2. Add Item to Cart
//    // URL Example: /api/cart/add?productId=1&variantId=5&quantity=1
//    @PostMapping("/add")
//    public ResponseEntity<Cart> addToCart(
//            @RequestParam Long productId,
//            @RequestParam Long variantId,
//            @RequestParam int quantity,
//            Principal principal) {
//        
//        String email = principal.getName();
//        Cart updatedCart = cartService.addToCart(email, productId, variantId, quantity);
//        return ResponseEntity.ok(updatedCart);
//    }
//
//    // 3. Update Quantity
//    // URL Example: /api/cart/update?cartItemId=10&quantity=2
//    @PutMapping("/update")
//    public ResponseEntity<Cart> updateQuantity(
//            @RequestParam Long cartItemId,
//            @RequestParam int quantity,
//            Principal principal) {
//        
//        String email = principal.getName();
//        Cart updatedCart = cartService.updateQuantity(email, cartItemId, quantity);
//        return ResponseEntity.ok(updatedCart);
//    }
//
//    // 4. Remove Item
//    // URL Example: /api/cart/remove/10
//    @DeleteMapping("/remove/{cartItemId}")
//    public ResponseEntity<Cart> removeFromCart(
//            @PathVariable Long cartItemId, 
//            Principal principal) {
//        
//        String email = principal.getName();
//        Cart updatedCart = cartService.removeFromCart(email, cartItemId);
//        return ResponseEntity.ok(updatedCart);
//    }
//}