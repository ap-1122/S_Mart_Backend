 package com.example.demo.dto;

import java.util.List;

public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private Long parentCategoryId;
    private List<CategoryResponseDto> subCategories; // Recursion ke liye (Mobile -> SmartPhone)

    // --- Constructors ---
    public CategoryResponseDto() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(Long parentCategoryId) { this.parentCategoryId = parentCategoryId; }

    public List<CategoryResponseDto> getSubCategories() { return subCategories; }
    public void setSubCategories(List<CategoryResponseDto> subCategories) { this.subCategories = subCategories; }
}