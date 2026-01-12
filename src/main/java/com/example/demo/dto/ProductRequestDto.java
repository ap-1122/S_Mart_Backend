package com.example.demo.dto;

public class ProductRequestDto {

    private String name;
    private String description;
    
    // Using simple Long IDs to prevent "Null ID" errors
    private Long categoryId;
    private Long brandId;

    // --- Constructors ---
    public ProductRequestDto() {}

    public ProductRequestDto(String name, String description, Long categoryId, Long brandId) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.brandId = brandId;
    }

    // --- Getters & Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
}




















//upper code is also same but more cleane
// package com.example.demo.dto;
//
//public class ProductRequestDto {
//
//    private String name;
//    private String description;
//    private Long categoryId;
//    private Long brandId;
//
//    // --- Constructors ---
//    public ProductRequestDto() {}
//
//    public ProductRequestDto(String name, String description, Long categoryId, Long brandId) {
//        this.name = name;
//        this.description = description;
//        this.categoryId = categoryId;
//        this.brandId = brandId;
//    }
//
//    // --- GETTERS (Ye methods missing the) ---
//    public String getName() { return name; }
//    public String getDescription() { return description; }
//    public Long getCategoryId() { return categoryId; }
//    public Long getBrandId() { return brandId; }
//
//    // --- SETTERS ---
//    public void setName(String name) { this.name = name; }
//    public void setDescription(String description) { this.description = description; }
//    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
//    public void setBrandId(Long brandId) { this.brandId = brandId; }
//}