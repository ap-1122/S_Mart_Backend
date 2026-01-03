package com.example.demo.controller;

import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@CrossOrigin(origins = "http://localhost:5173") // Frontend allowed
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // Endpoint: POST http://localhost:8080/admin/products/create
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateDTO dto) {
        Product createdProduct = productService.createProductWithVariants(dto);
        return ResponseEntity.ok(createdProduct);
    }
}