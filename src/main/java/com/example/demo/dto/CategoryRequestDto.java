 package com.example.demo.dto;

public class CategoryRequestDto {

    private String name;
    private String description;
    private Long parentCategoryId; // ID bhejenge pura object nahi

    // --- Constructors ---
    public CategoryRequestDto() {}

    public CategoryRequestDto(String name, String description, Long parentCategoryId) {
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }

    // --- Getters and Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(Long parentCategoryId) { this.parentCategoryId = parentCategoryId; }
}