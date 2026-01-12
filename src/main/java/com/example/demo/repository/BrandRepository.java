 package com.example.demo.repository;

import com.example.demo.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    
    // Naam se Brand dhundne ke liye
    Optional<Brand> findByName(String name);
    
     // Sirf Active Brands lane ke liye [cite: 713]
    List<Brand> findByIsActiveTrue();
}