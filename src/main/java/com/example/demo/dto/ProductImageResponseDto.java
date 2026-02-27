 package com.example.demo.dto;

public class ProductImageResponseDto {

    private Long id;
    private String imageUrl;
    private Integer displayOrder;
    private Boolean isPrimary;
    private Long variantId;

    // --- Constructors ---
    public ProductImageResponseDto() {}

    // --- GETTERS ---
    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Boolean getIsPrimary() { return isPrimary; }
    public Long getVariantId() { return variantId; }

    // --- SETTERS (IMP: setVariantId yahan hai) ---
    public void setId(Long id) { this.id = id; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }
}