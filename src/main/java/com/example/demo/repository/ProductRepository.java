package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Ek Category ke saare products [cite: 658]
    List<Product> findByCategory_Id(Long categoryId);

    // Ek Brand ke saare products [cite: 659]
    List<Product> findByBrand_Id(Long brandId);

     // Sirf Active products dikhane ke liye (Soft Delete logic) [cite: 660]
    List<Product> findByIsActiveTrue();
    
    // Search by Name containing (Search bar ke liye)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}



// package com.example.demo.repository;
//import com.example.demo.model.Product;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface ProductRepository extends JpaRepository<Product, Long> {
//}
