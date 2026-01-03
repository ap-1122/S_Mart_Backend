 package com.example.demo.service;

import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private AttributeValueRepository attributeValueRepository;

    // @Transactional ka matlab: Agar beech me koi error aaya (e.g., variant save fail),
    // to Pura process cancel ho jayega (Parent bhi delete ho jayega). Safe!
    @Transactional 
    public Product createProductWithVariants(ProductCreateDTO dto) {
        
        // --- STEP 1: CATEGORY DHOONDO ---
        Category cat = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + dto.getCategoryId()));

        // --- STEP 2: PARENT PRODUCT BANAO ---
        Product parent = new Product();
        parent.setName(dto.getName());
        parent.setDescription(dto.getDescription());
        parent.setImageUrl(dto.getImageUrl());
        parent.setCategory(cat);
        
        // Parent ko save karo taaki humein iski ID mil jaye
        Product savedParent = productRepository.save(parent);

        // --- STEP 3: VARIANTS BANAO ---
        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            
            for (ProductCreateDTO.VariantDTO variantDto : dto.getVariants()) {
                Product variant = new Product();
                
                // Naam thoda descriptive banate hain (Internal use ke liye)
                variant.setName(savedParent.getName() + " - Variant"); 
                
                // Specific details
                variant.setPrice(variantDto.getPrice());
                variant.setStock(variantDto.getStock());
                variant.setSku(variantDto.getSku());
                
                // Agar variant ki photo nahi hai, to Parent ki photo use karo
                variant.setImageUrl(savedParent.getImageUrl()); 
                
                // --- RELATIONS SET KARO ---
                variant.setParentProduct(savedParent); // Baap set kiya
                variant.setCategory(cat);              // Category same hogi
                
                // --- ATTRIBUTES SET KARO (Magic) ---
                // Frontend ne IDs bheji thi [1, 5], hum Database se wo objects nikalenge
                List<AttributeValue> attrs = attributeValueRepository.findAllById(variantDto.getAttributeValueIds());
                variant.setAttributeValues(new HashSet<>(attrs));

                // Variant save karo
                productRepository.save(variant);
            }
        }
        
        return savedParent; // Parent wapas bhej do confirmation ke liye
    }
}