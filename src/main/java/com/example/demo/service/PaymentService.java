 package com.example.demo.service;

import com.example.demo.model.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // ✅ Import Zaroori hai
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private OrderService orderService; 

    // ✅ FIX: Secret Key ko properties file se uthaya
    @Value("${razorpay.key.secret}")
    private String keySecret;

    // 1. Create Razorpay Order
    public String createOrder(Double amount) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // 100 paise = 1 Rupee
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            return razorpayOrder.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage());
        }
    }

    // 2. Verify Payment & Save Order
    public void verifyAndSaveOrder(String email, String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, Long addressId) {
        try {
            // A. Payload banao verify karne ke liye
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            // B. Signature Match karo (Security Check)
            // ✅ Yahan 'keySecret' use kiya jo upar @Value se aaya hai
            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

            if (isValid) {
                // C. Payment Asli hai -> Order Place karo
                orderService.placeOrder(email, addressId, "ONLINE");
            } else {
                throw new RuntimeException("Payment Signature Verification Failed!");
            }

        } catch (Exception e) {
            throw new RuntimeException("Payment Verification Error: " + e.getMessage());
        }
    }
}