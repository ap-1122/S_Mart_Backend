package com.example.demo.repository;

 

import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    // Email se user dhundne ke liye
    Optional<UserModel> findByEmail(String email);
    
    // Check karne ke liye ki email exist karta hai ya nahi
    boolean existsByEmail(String email);
    
 // NEW: Phone ke liye method add kiya
    Optional<UserModel> findByPhoneNumber(String phoneNumber);
}