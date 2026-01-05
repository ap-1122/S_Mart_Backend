package com.example.demo.controller;

import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.model.Attribute;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@CrossOrigin(origins = "http://localhost:5173") // Frontend allowed
public class AdminProductController {

    @Autowired
    private ProductService productService;
    
 // --- NEW REPOSITORIES ADDED ---
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private AttributeRepository attributeRepository;

    // Endpoint: POST http://localhost:8080/admin/products/create
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateDTO dto) {
        Product createdProduct = productService.createProductWithVariants(dto);
        return ResponseEntity.ok(createdProduct);
    }
      // --- NEW GET API: Fetch All Categories ---
     // Frontend dropdown isko call karega
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
    
    // --- NEW GET API: Fetch All Attributes (Color, Size etc.) ---
     // Frontend attributes list isko call karegi
    @GetMapping("/attributes")
    public ResponseEntity<List<Attribute>> getAllAttributes() {
        return ResponseEntity.ok(attributeRepository.findAll());
    }
    
}