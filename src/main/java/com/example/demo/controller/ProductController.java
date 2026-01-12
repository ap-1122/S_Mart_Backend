 package com.example.demo.controller;

import com.example.demo.dto.ProductRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.model.ProductSpecification;
import com.example.demo.model.Product;
import com.example.demo.model.ProductFeature;
import com.example.demo.repository.ProductSpecificationRepository;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.repository.ProductFeatureRepository;
import com.example.demo.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductSpecificationRepository specRepo;
    @Autowired private ProductFeatureRepository featureRepo;

    @Autowired private AttributeRepository  attributeRepository; // ✅ Link karne ke liye zaroori hai
    // 1. Create Product (Parent Only - No Price/Stock here)
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    // 2. Get All Products
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 3. Get Product By ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
 // ✅ 1. Save Specifications in Bulk [cite: 912]
    @PostMapping("/{productId}/specifications/bulk")
    public ResponseEntity<?> saveSpecs(@PathVariable Long productId, @RequestBody List<ProductSpecification> specs) {
        Product product = productRepository.findById(productId).orElseThrow();
        specs.forEach(s -> s.setProduct(product));
        specRepo.saveAll(specs);
        return ResponseEntity.ok("Specifications Saved Successfully");
    }

    // ✅ 2. Save Features in Bulk [cite: 727]
    @PostMapping("/{productId}/features/bulk")
    public ResponseEntity<?> saveFeatures(@PathVariable Long productId, @RequestBody List<String> featureStrings) {
        Product product = productRepository.findById(productId).orElseThrow();
        List<ProductFeature> features = featureStrings.stream()
            .map(f -> new ProductFeature(f, product))
            .collect(Collectors.toList());
        featureRepo.saveAll(features);
        return ResponseEntity.ok("Features Saved Successfully");
    }

    // ✅ 3. Save Manufacturer Info [cite: 982]
    @PostMapping("/{productId}/manufacturer")
    public ResponseEntity<?> saveManufacturerInfo(@PathVariable Long productId, @RequestBody Map<String, String> body) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setManufacturerInfo(body.get("content"));
        productRepository.save(product);
        return ResponseEntity.ok("Manufacturer Info Updated");
    }

    // ✅ 4. Fetch Methods (For Frontend Display) [cite: 714, 880]
    @GetMapping("/{productId}/features")
    public ResponseEntity<?> getFeatures(@PathVariable Long productId) {
        return ResponseEntity.ok(featureRepo.findByProductId(productId));
    }

    @GetMapping("/{productId}/specifications")
    public ResponseEntity<?> getSpecs(@PathVariable Long productId) {
        return ResponseEntity.ok(specRepo.findByProductId(productId));
    }
    
 // ✅ Step 4 के लिए: Product और Attribute को आपस में जोड़ना
    @PostMapping("/{productId}/attributes/{attributeId}")
    public ResponseEntity<?> linkAttributeToProduct(
            @PathVariable Long productId, 
            @PathVariable Long attributeId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            com.example.demo.model.Attribute attribute = attributeRepository.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found"));

            // Many-to-Many relationship maintain karna
            if (!product.getAttributes().contains(attribute)) {
                product.getAttributes().add(attribute);
                productRepository.save(product);
            }

            return ResponseEntity.ok("Attribute linked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
}