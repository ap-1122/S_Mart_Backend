package com.example.demo.dto;

import java.util.Map;

public class VariantResponseDto {
    private Long id;
    private String sku;
    private Double price;
    private Integer stock;
    
    // ✅ Use Map here (Example: "Color" -> "Red")
    private Map<String, String> attributes; 

    // --- Constructors ---
    public VariantResponseDto() {} // ✅ Default Constructor

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}






















//package com.example.demo.dto;
//
//import java.util.Map;
//
//public class VariantResponseDto {
//
//    private Long id;
//    private String sku;
//    private Double price;
//    private Integer stock;
//    
//    // Example Map: { "Color": "Red", "Size": "XL" }
//    private Map<String, String> attributes; 
//
//    // --- Constructors ---
//    public VariantResponseDto() {}
//
//    // --- Getters and Setters ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getSku() { return sku; }
//    public void setSku(String sku) { this.sku = sku; }
//
//    public Double getPrice() { return price; }
//    public void setPrice(Double price) { this.price = price; }
//
//    public Integer getStock() { return stock; }
//    public void setStock(Integer stock) { this.stock = stock; }
//
//    public Map<String, String> getAttributes() { return attributes; }
//    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
//}