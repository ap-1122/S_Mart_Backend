package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Order Confirmation Email bhejne ke liye method
    public void sendOrderConfirmation(String toEmail, Long orderId, Double totalAmount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Confirmed! Order #" + orderId);
            message.setText("Hello,\n\n" +
                    "Thank you for your order! Your order has been placed successfully.\n\n" +
                    "Order ID: " + orderId + "\n" +
                    "Total Amount: ₹ " + totalAmount + "\n\n" +
                    "We will notify you once it's shipped.\n\n" +
                    "Best Regards,\nYour E-Commerce Team");

            mailSender.send(message);
            System.out.println("Order confirmation email sent to: " + toEmail);
        } catch (Exception e) {
            // Email fail hone par system crash nahi hona chahiye, bas log kar do
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}