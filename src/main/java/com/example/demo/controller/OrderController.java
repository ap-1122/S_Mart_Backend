package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.UserModel;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private String getEmailFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserModel) {
            return ((UserModel) principal).getEmail();
        }
        return principal.toString();
    }

    // 1. Place Order API
    // URL: POST /api/orders/place?addressId=1&paymentMethod=COD
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            @RequestParam Long addressId,
            @RequestParam String paymentMethod,
            Authentication authentication) {
        
        String email = getEmailFromAuth(authentication);
        Order order = orderService.placeOrder(email, addressId, paymentMethod);
        return ResponseEntity.ok(order);
    }

    // 2. Get My Orders API (Order History)
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        List<Order> orders = orderService.getUserOrders(email);
        return ResponseEntity.ok(orders);
    }
    
    // URL: POST /api/orders/{id}/cancel
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        Order cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }

    // ==========================================
    // 🚀 NEW: ADMIN ENDPOINTS
    // ==========================================

    // 1. Get All Orders (Admin Dashboard ke liye)
    @GetMapping("/admin/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 2. Update Status (e.g., ?status=SHIPPED)
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
}













//update this code in upper code so admin gets all order 

// package com.example.demo.controller;
//
//import com.example.demo.model.Order;
//import com.example.demo.model.UserModel;
//import com.example.demo.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/orders")
//@CrossOrigin(origins = "http://localhost:5173")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    private String getEmailFromAuth(Authentication authentication) {
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserModel) {
//            return ((UserModel) principal).getEmail();
//        }
//        return principal.toString();
//    }
//
//    // 1. Place Order API
//    // URL: POST /api/orders/place?addressId=1&paymentMethod=COD
//    @PostMapping("/place")
//    public ResponseEntity<Order> placeOrder(
//            @RequestParam Long addressId,
//            @RequestParam String paymentMethod,
//            Authentication authentication) {
//        
//        String email = getEmailFromAuth(authentication);
//        Order order = orderService.placeOrder(email, addressId, paymentMethod);
//        return ResponseEntity.ok(order);
//    }
//
//    // 2. Get My Orders API (Order History)
//    @GetMapping("/my-orders")
//    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
//        String email = getEmailFromAuth(authentication);
//        List<Order> orders = orderService.getUserOrders(email);
//        return ResponseEntity.ok(orders);
//    }
//    
// // URL: POST /api/orders/{id}/cancel
//    @PostMapping("/{id}/cancel")
//    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
//        Order cancelledOrder = orderService.cancelOrder(id);
//        return ResponseEntity.ok(cancelledOrder);
//    }
//}