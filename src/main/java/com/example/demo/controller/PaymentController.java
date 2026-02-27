package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.service.PaymentService;
import com.razorpay.Utils; // ✅ Import Added
import org.json.JSONObject; // ✅ Import Added
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // ✅ Import Added
import org.springframework.http.HttpStatus; // ✅ Import Added
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ✅ NEW: Secret key properties file se ayegi
    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    private String getEmailFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserModel) {
            return ((UserModel) principal).getEmail();
        }
        return principal.toString();
    }

    // 1. Create Order API
    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam Double amount) {
        String response = paymentService.createOrder(amount);
        return ResponseEntity.ok(response);
    }

    // 2. Verify Payment API (Frontend se call hota hai)
    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(
            @RequestBody Map<String, Object> data, 
            Authentication authentication) {
        
        String email = getEmailFromAuth(authentication);
        
        String orderId = (String) data.get("razorpayOrderId");
        String paymentId = (String) data.get("razorpayPaymentId");
        String signature = (String) data.get("razorpaySignature");
        
        // Address ID Integer me aa sakta hai, isliye safe conversion
        Long addressId = Long.valueOf(data.get("addressId").toString());

        paymentService.verifyAndSaveOrder(email, orderId, paymentId, signature, addressId);

        return ResponseEntity.ok("Payment Verified and Order Placed!");
    }

    // 🚀 3. NEW: Webhook API (Jo Razorpay Automatic Call karega)
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, 
                                                @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            // A. Verify Signature (Security Check)
            // Ye check karta hai ki call sach me Razorpay se aayi hai ya nahi
            boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
            
            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
            }

            // B. Event Read karo
            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");

            // C. Agar Payment Capture (Success) ho gaya hai
            if ("payment.captured".equals(event)) {
                JSONObject entity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                
                String paymentId = entity.getString("id");
                String orderId = entity.getString("order_id");
                String email = entity.optString("email"); // Agar user email Razorpay form me bhare

                System.out.println("✅ WEBHOOK: Payment Captured for Order: " + orderId + ", Payment ID: " + paymentId);
                
                // FUTURE: Yahan tum Order status update karne ka logic laga sakte ho
                // paymentService.updateStatus(orderId, "CONFIRMED");
            }

            return ResponseEntity.ok("Webhook Received");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }
}























// in upper code we add new end point for webhook for global payment using ngrok -it works as tunnel btw rozer and our website localhost

// package com.example.demo.controller;
//
//import com.example.demo.model.UserModel;
//import com.example.demo.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payment")
//@CrossOrigin(origins = "http://localhost:5173")
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    private String getEmailFromAuth(Authentication authentication) {
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserModel) {
//            return ((UserModel) principal).getEmail();
//        }
//        return principal.toString();
//    }
//
//    // 1. Create Order API
//    @PostMapping("/create-order")
//    public ResponseEntity<String> createOrder(@RequestParam Double amount) {
//        String response = paymentService.createOrder(amount);
//        return ResponseEntity.ok(response);
//    }
//
//    // 2. Verify Payment API
//    @PostMapping("/verify-payment")
//    public ResponseEntity<String> verifyPayment(
//            @RequestBody Map<String, Object> data, 
//            Authentication authentication) {
//        
//        String email = getEmailFromAuth(authentication);
//        
//        String orderId = (String) data.get("razorpayOrderId");
//        String paymentId = (String) data.get("razorpayPaymentId");
//        String signature = (String) data.get("razorpaySignature");
//        
//        // Address ID Integer me aa sakta hai, isliye safe conversion
//        Long addressId = Long.valueOf(data.get("addressId").toString());
//
//        paymentService.verifyAndSaveOrder(email, orderId, paymentId, signature, addressId);
//
//        return ResponseEntity.ok("Payment Verified and Order Placed!");
//    }
//}