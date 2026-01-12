 package com.example.demo.repository;

import com.example.demo.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    // Ek Product ID ke saare variants lao [cite: 1877]
    List<Variant> findByProductId(Long productId);
    
    // Sirf Active Variants
    List<Variant> findByProductIdAndIsActiveTrue(Long productId);
}