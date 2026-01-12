package com.example.demo.dto;

import java.util.Map;

public class VariantCreateRequestDto {

    private String sku;
    private Double price;
    private Integer stock;

    // ✅ FIX: Key ko String kar diya taaki JSON parsing error na aaye
    private Map<String, String> attributes; 

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}






















//we update map to take string id value 
//package com.example.demo.dto;
//
//import java.util.Map;
//
//public class VariantCreateRequestDto {
//
//    private String sku;
//    private Double price;
//    private Integer stock;
//
//    // ✅ FIX: Key = Attribute ID (Long), Value = Value Name (String)
//    // Example: { 1 : "Red", 2 : "XL" }
//    private Map<Long, String> attributes; 
//
//    // --- Getters and Setters ---
//    public String getSku() { return sku; }
//    public void setSku(String sku) { this.sku = sku; }
//
//    public Double getPrice() { return price; }
//    public void setPrice(Double price) { this.price = price; }
//
//    public Integer getStock() { return stock; }
//    public void setStock(Integer stock) { this.stock = stock; }
//
//    public Map<Long, String> getAttributes() { return attributes; }
//    public void setAttributes(Map<Long, String> attributes) { this.attributes = attributes; }
//}
//
//















// package com.example.demo.dto;
//
//import java.util.Map;
//
//public class VariantCreateRequestDto {
//
//    private String sku;
//    private Double price;
//    private Integer stock;
//    
//    // Example Map: { 1: 5 } -> (Color ID : Red Value ID)
//    private Map<Long, Long> attributes;
//
//    // --- Constructors ---
//    public VariantCreateRequestDto() {}
//
//    public VariantCreateRequestDto(String sku, Double price, Integer stock, Map<Long, Long> attributes) {
//        this.sku = sku;
//        this.price = price;
//        this.stock = stock;
//        this.attributes = attributes;
//    }
//
//    // --- Getters and Setters ---
//    public String getSku() { return sku; }
//    public void setSku(String sku) { this.sku = sku; }
//
//    public Double getPrice() { return price; }
//    public void setPrice(Double price) { this.price = price; }
//
//    public Integer getStock() { return stock; }
//    public void setStock(Integer stock) { this.stock = stock; }
//
//    public Map<Long, Long> getAttributes() { return attributes; }
//    public void setAttributes(Map<Long, Long> attributes) { this.attributes = attributes; }
//}