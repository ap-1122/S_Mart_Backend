 package com.example.demo.controller;

import com.example.demo.dto.CategoryRequestDto;
import com.example.demo.dto.CategoryResponseDto;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 1. Create Category
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    // 2. Get Root Categories (Jinka koi parent nahi)
    @GetMapping("/root")
    public ResponseEntity<List<CategoryResponseDto>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    // 3. Get Sub-Categories (e.g. Electronics ke andar kya hai)
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryResponseDto>> getSubCategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getSubCategories(parentId));
    }
}