 package com.example.demo.controller;

import com.example.demo.dto.BrandRequestDto;
import com.example.demo.dto.BrandResponseDto;
import com.example.demo.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // 1. Create Brand
    @PostMapping
    public ResponseEntity<BrandResponseDto> createBrand(@RequestBody BrandRequestDto dto) {
        return ResponseEntity.ok(brandService.createBrand(dto));
    }

    // 2. Get All Brands
    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    // 3. Get Brand By ID
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDto> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }
}