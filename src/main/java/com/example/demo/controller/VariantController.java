 package com.example.demo.controller;

import com.example.demo.dto.VariantCreateRequestDto;
import com.example.demo.dto.VariantResponseDto;
import com.example.demo.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variants")
//👇👇👇 YE LINE JODNA SABSE ZAROORI HAI (Isse 403 Error hat jayega)
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class VariantController {

    @Autowired
    private VariantService variantService;

//    // 1. Create Variant (e.g. Add "Red, XL" to iPhone)
//    @PostMapping("/{productId}")
//    public ResponseEntity<VariantResponseDto> createVariant(
//            @PathVariable Long productId,
//            @RequestBody VariantCreateRequestDto dto) {
//        return ResponseEntity.ok(variantService.createVariant(productId, dto));
//    }
    
 // ✅ DEBUGGER VERSION: Ye error ko Postman me dikhayega
    @PostMapping("/{productId}")
    public ResponseEntity<?> createVariant(
            @PathVariable Long productId,
            @RequestBody VariantCreateRequestDto dto) {
        try {
            return ResponseEntity.ok(variantService.createVariant(productId, dto));
        } catch (Exception e) {
            // Error ko console me print karo
            e.printStackTrace(); 
            // Error ko Postman me bhejo
            return ResponseEntity.status(500)
                .body("🔥 ERROR DETECTED: " + e.getMessage() + " || CHECK CAUSE: " + e.getClass().getName());
        }
    }

    // 2. Get Variants for a Product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<VariantResponseDto>> getVariantsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(variantService.getVariantsByProduct(productId));
    }
}