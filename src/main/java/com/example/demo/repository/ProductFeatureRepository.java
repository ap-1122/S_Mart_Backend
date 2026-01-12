package com.example.demo.repository;

import com.example.demo.model.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    List<ProductFeature> findByProductId(Long productId);
}