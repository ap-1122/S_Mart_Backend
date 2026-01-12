 package com.example.demo.repository;

import com.example.demo.model.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    List<ProductSpecification> findByProductId(Long productId);
}