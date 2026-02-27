package com.example.demo.dto;

import java.util.List;
import java.util.Map;
import lombok.Data; // Agar Lombok nahi hai to Getters/Setters niche se copy karo

public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private String categoryName;
    private String brandName;
    
    // ✅ NEW: Extra Details for Product Page
    private String manufacturerInfo;
    private List<String> features;          // e.g., ["Frost Free", "Inverter"]
    private Map<String, String> specifications; // e.g., {"Capacity": "236L", "Star": "3 Star"}

    private List<ProductImageResponseDto> images;
    private List<VariantResponseDto> variants;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getManufacturerInfo() { return manufacturerInfo; }
    public void setManufacturerInfo(String manufacturerInfo) { this.manufacturerInfo = manufacturerInfo; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    public Map<String, String> getSpecifications() { return specifications; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }

    public List<ProductImageResponseDto> getImages() { return images; }
    public void setImages(List<ProductImageResponseDto> images) { this.images = images; }
    
    public List<VariantResponseDto> getVariants() { return variants; }
    public void setVariants(List<VariantResponseDto> variants) { this.variants = variants; }
}














//THIS CODE IS FULLY WORKING BUT WE ADD 3 MORE DETAIL FOR PRODUCT JISSE AUR JADA FEATURS ADD HO SAKE PRODUCT DETAILS PAGE ME DIKHANE KE LIYE 

//package com.example.demo.dto;
//
//import java.util.List;
//
//public class ProductResponseDto {
//    private Long id;
//    private String name;
//    private String description;
//    private String categoryName;
//    private String brandName;
//    private List<ProductImageResponseDto> images;
//    private List<VariantResponseDto> variants; // ✅ Added for variants
//
//    // --- Getters and Setters ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public String getCategoryName() { return categoryName; }
//    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
//
//    public String getBrandName() { return brandName; }
//    public void setBrandName(String brandName) { this.brandName = brandName; }
//
//    public List<ProductImageResponseDto> getImages() { return images; }
//    public void setImages(List<ProductImageResponseDto> images) { this.images = images; }
//
//    public List<VariantResponseDto> getVariants() { return variants; }
//    public void setVariants(List<VariantResponseDto> variants) { this.variants = variants; }
//}













//THIS CODE ONLY DESIGN FOR IMAGE MAPPING BUT WE ADDED VARIENT MAPPING ALSO SO UPDATE THIS CODE 
// package com.example.demo.dto;
//
//import java.util.List;
//
//public class ProductResponseDto {
//
//    private Long id;
//    private String name;
//    private String description;
//    private String categoryName;
//    private String brandName;
//    private List<ProductImageResponseDto> images;
//
//    // --- Constructors ---
//    public ProductResponseDto() {}
//
//    // --- GETTERS ---
//    public Long getId() { return id; }
//    public String getName() { return name; }
//    public String getDescription() { return description; }
//    public String getCategoryName() { return categoryName; }
//    public String getBrandName() { return brandName; }
//    public List<ProductImageResponseDto> getImages() { return images; }
//
//    // --- SETTERS (Ye missing the) ---
//    public void setId(Long id) { this.id = id; }
//    public void setName(String name) { this.name = name; }
//    public void setDescription(String description) { this.description = description; }
//    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
//    public void setBrandName(String brandName) { this.brandName = brandName; }
//    public void setImages(List<ProductImageResponseDto> images) { this.images = images; }
//}