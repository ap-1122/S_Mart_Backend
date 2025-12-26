 package com.example.demo.dto;

public class OtpVerificationRequest {
    private String email;
    private String phoneNumber;
    private String otp;

    // Getters Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
