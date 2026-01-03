 package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductCreateDTO {
    
    // --- PARENT PRODUCT DETAILS ---
    // Ye cheezein sabhi variants ke liye same rahengi
    private String name;         // e.g., "iPhone 15"
    private String description;
    private Long categoryId;     // e.g., 1 (Mobiles)
    private String imageUrl;     // Common image for listing

    // --- LIST OF VARIANTS ---
    // Ek parent ke bahut saare bacchon ka data yahan aayega
    private List<VariantDTO> variants;

    // --- GETTERS & SETTERS (Manual) ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<VariantDTO> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantDTO> variants) {
        this.variants = variants;
    }

    // --- INTERNAL CLASS FOR VARIANTS ---
    public static class VariantDTO {
        private BigDecimal price;    // e.g., 79999.00
        private Integer stock;       // e.g., 50
        private String sku;          // e.g., "IP15-RED-128"
        
        // Attributes (Frontend humein bas IDs bhejega, e.g., [1, 5] -> Red, 128GB)
        private List<Long> attributeValueIds; 

        // --- GETTERS & SETTERS FOR VARIANT ---
        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public List<Long> getAttributeValueIds() {
            return attributeValueIds;
        }

        public void setAttributeValueIds(List<Long> attributeValueIds) {
            this.attributeValueIds = attributeValueIds;
        }
    }
}