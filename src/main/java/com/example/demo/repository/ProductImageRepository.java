 package com.example.demo.repository;

import com.example.demo.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    // Wo images jo pure product ke liye hain (Kisi specific variant ke liye nahi) [cite: 1887]
    List<ProductImage> findByProductIdAndVariantIdIsNull(Long productId);

   // Wo images jo specific variant ke liye hain (e.g. Sirf Red phone ki photo) [cite: 1888]
    List<ProductImage> findByProductIdAndVariantId(Long productId, Long variantId);
}