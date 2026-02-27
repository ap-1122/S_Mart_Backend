 package com.example.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    // Application.properties se values utha rahe hain
    @Value("${twilio.account-sid}")
    private String twilioSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    // 1. RANDOM OTP GENERATOR (6 Digits)
    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); 
        return String.valueOf(otp);
    }

    // 2. OTP KO REDIS MEIN SAVE KARNA (Expiry: 5 Minutes)
    // Key: Email ya Phone Number, Value: OTP
    private void saveOtpToRedis(String key, String otp) {
        redisTemplate.opsForValue().set(key, otp, 5, TimeUnit.MINUTES);
    }

    // 3. EMAIL PAR OTP BHEJNA
    public String sendEmailOtp(String email) {
        String otp = generateRandomOtp();
        saveOtpToRedis(email, otp); // Redis me save kiya

        // Email prepare karna
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Login OTP");
        message.setText("Hello, \n\nYour OTP for login is: " + otp + "\n\nThis OTP is valid for 5 minutes.");

        // Email bhejna
        mailSender.send(message);
        return "OTP sent to Email: " + email;
    }

    // 4. SMS PAR OTP BHEJNA (Twilio)
    public String sendSmsOtp(String phoneNumber) {
        String otp = generateRandomOtp();
        saveOtpToRedis(phoneNumber, otp); // Redis me save kiya

        try {
            // Twilio Initialize
            Twilio.init(twilioSid, twilioAuthToken);

            // SMS Bhejna
            Message.creator(
                    new PhoneNumber(phoneNumber), // Kisko bhejna hai (User)
                    new PhoneNumber(twilioPhoneNumber), // Kahan se jayega (Twilio Trial Number)
                    "Your Login OTP is: " + otp // Message Body
            ).create();

            return "OTP sent to Phone: " + phoneNumber;
        } catch (Exception e) {
            throw new RuntimeException("Failed to send SMS: " + e.getMessage());
        }
    }

    // 5. OTP VALIDATE KARNA (Login ke waqt check hoga)
    public boolean validateOtp(String key, String userInputOtp) {
        // Redis se OTP nikalo
        String storedOtp = redisTemplate.opsForValue().get(key);

        // Agar OTP mila aur match ho gaya
        if (storedOtp != null && storedOtp.equals(userInputOtp)) {
            redisTemplate.delete(key); // Use hone ke baad delete kar do taaki dubara use na ho
            return true;
        }
        return false;
    }
    
 // Imports add kar lena: java.util.UUID

    // ... (Purana code upar rahega) ...

    // 6. PASSWORD RESET KE LIYE MAGIC LINK BHEJNA (Email)
    public String sendResetLink(String email) {
        // 1. Unique Token generate karo
        String resetToken = UUID.randomUUID().toString();
        
        // 2. Redis me save karo (Key: "RESET_TOKEN" + token, Value: email)
        // Ye link sirf 10 minute valid rahega
        redisTemplate.opsForValue().set("RESET_" + resetToken, email, 10, TimeUnit.MINUTES);

        // 3. Link Banao (Frontend ka URL)
        // React App port 3000 par chalegi
        String link = "http://localhost:3000/reset-password?token=" + resetToken;

        // 4. Email Send karo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("Click the link below to reset your password:\n" + link + "\n\nValid for 10 minutes.");
        mailSender.send(message);

        return "Reset Link sent to Email!";
    }

    // 7. PASSWORD RESET KE LIYE OTP BHEJNA (Phone)
    // Ye hum purana sendSmsOtp use karke bhi kar sakte hain, bas key alag rakhenge
    public String sendResetOtp(String phoneNumber) {
        String otp = generateRandomOtp();
        // Prefix "RESET_" lagaya taki login OTP se mix na ho
        redisTemplate.opsForValue().set("RESET_" + phoneNumber, otp, 5, TimeUnit.MINUTES);
        
        // Twilio se bhejo (Purana logic copy kar sakte ho ya reuse)
        try {
            Twilio.init(twilioSid, twilioAuthToken);
            Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    "Your Password Reset OTP is: " + otp
            ).create();
            return "Reset OTP sent to Phone!";
        } catch (Exception e) {
            throw new RuntimeException("SMS Failed");
        }
    }

    // 8. TOKEN/OTP VALIDATE KARNA (Helper Method)
    public String validateResetTokenOrOtp(String inputKey) {
        // Redis se Email/Data nikalo
        return redisTemplate.opsForValue().get("RESET_" + inputKey);
    }
    
    // 9. CLEANUP (Kaam hone ke baad delete karo)
    public void deleteResetToken(String inputKey) {
        redisTemplate.delete("RESET_" + inputKey);
    }
}