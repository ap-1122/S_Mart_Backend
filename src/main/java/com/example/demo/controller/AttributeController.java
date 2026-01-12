 package com.example.demo.controller;

import com.example.demo.dto.AttributeRequestDto;
import com.example.demo.dto.AttributeResponseDto;
import com.example.demo.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    // 1. Create Attribute (Color: Red, Blue)
    @PostMapping
    public ResponseEntity<AttributeResponseDto> createAttribute(@RequestBody AttributeRequestDto dto) {
        return ResponseEntity.ok(attributeService.createAttribute(dto));
    }

    // 2. Get All Attributes
    @GetMapping
    public ResponseEntity<List<AttributeResponseDto>> getAllAttributes() {
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }

    // 3. Get Attribute By ID
    @GetMapping("/{id}")
    public ResponseEntity<AttributeResponseDto> getAttributeById(@PathVariable Long id) {
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }
}