 package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.ReviewModel;
import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
    
    // 1. Ek Product ke saare reviews (Newest first)
    List<ReviewModel> findByProductOrderByCreatedAtDesc(Product product);

    // 2. Duplicate Check: Kya iss user ne iss product par pehle review diya hai?
    boolean existsByUserAndProduct(UserModel user, Product product);
    
    // 3. Optional: Ek product ke total reviews count
    long countByProduct(Product product);
}