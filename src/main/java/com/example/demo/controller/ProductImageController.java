 package com.example.demo.controller;

import com.example.demo.model.ProductImage;
import com.example.demo.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

//    // 1. Upload Image
//    @PostMapping("/upload/{productId}")
//    public ResponseEntity<?> uploadImage(
//            @PathVariable Long productId,
//            @RequestParam("file") MultipartFile file) {
//        try {
//            ProductImage image = productImageService.uploadImage(productId, file);
//            return ResponseEntity.ok(image); // Pura object return kar rahe hain abhi ke liye
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
//        }
//    }
    
 // Updated Method to accept Variant ID
    @PostMapping("/upload/{productId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "variantId", required = false) Long variantId // ✅ Added Param
    ) {
        try {
            // Note: Service me bhi variantId pass karna padega
            // Abhi ke liye hum sirf Product ID se save kar rahe hain, 
            // Phase 3 me hum Service update karke Variant link karenge.
            ProductImage image = productImageService.uploadImage(productId, file);
            return ResponseEntity.ok(image);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }
}