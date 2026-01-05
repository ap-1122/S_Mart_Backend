 package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/products") // Ye public rasta hai
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {

    @Autowired
    private ProductRepository productRepository;

    // API to get ALL products
    // URL: http://localhost:8080/public/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}