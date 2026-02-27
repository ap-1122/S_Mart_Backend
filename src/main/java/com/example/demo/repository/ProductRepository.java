package com.example.demo.repository;

import com.example.demo.model.Product;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    
    
 // ==========================================
    // 🚀 NEW: ADVANCE FILTER QUERY
    // ==========================================
    
    // Ye query check karegi:
    // 1. Agar Category ID diya hai to match karo, nahi to ignore karo (:categoryId IS NULL)
    // 2. Product ke VARIANTS check karo ki price range (min-max) me hai ya nahi
    // 3. DISTINCT isliye taki agar ek product ke 2 variant range me ho, to product do baar na dikhe
    
    @Query("SELECT DISTINCT p FROM Product p JOIN p.variants v WHERE " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(v.price BETWEEN :minPrice AND :maxPrice) AND " +
           "p.isActive = true")
    List<Product> findFilteredProducts(
        @Param("categoryId") Long categoryId, 
        @Param("minPrice") Double minPrice, 
        @Param("maxPrice") Double maxPrice
    );
    
    
}


















//IN UPPER CODE : 🚀 NEW: ADVANCE FILTER QUERY


//package com.example.demo.repository;
//
//import com.example.demo.model.Product;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface ProductRepository extends JpaRepository<Product, Long> {
//
//    // Ek Category ke saare products [cite: 658]
//    List<Product> findByCategory_Id(Long categoryId);
//
//    // Ek Brand ke saare products [cite: 659]
//    List<Product> findByBrand_Id(Long brandId);
//
//     // Sirf Active products dikhane ke liye (Soft Delete logic) [cite: 660]
//    List<Product> findByIsActiveTrue();
//    
//    // Search by Name containing (Search bar ke liye)
//    List<Product> findByNameContainingIgnoreCase(String keyword);
//}



// package com.example.demo.repository;
//import com.example.demo.model.Product;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface ProductRepository extends JpaRepository<Product, Long> {
//}
