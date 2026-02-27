//package com.example.demo.model;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Entity
//@Table(name = "variants")
//public class Variant {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    @JsonIgnore
//    private Product product;
//
//    @Column(unique = true)
//    private String sku;
//
//    private Double price;
//    private Integer stock;
//
//    @Column(name = "is_active")
//    private Boolean isActive = true;
//
//    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<VariantAttributeValue> attributeValues;
//
//    public Variant() {}
//
//    public Variant(Product product, String sku, Double price, Integer stock) {
//        this.product = product;
//        this.sku = sku;
//        this.price = price;
//        this.stock = stock;
//    }
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Product getProduct() { return product; }
//    public void setProduct(Product product) { this.product = product; }
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
//    public Boolean getIsActive() { return isActive; }
//    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
//
//    public List<VariantAttributeValue> getAttributeValues() { return attributeValues; }
//    public void setAttributeValues(List<VariantAttributeValue> attributeValues) { this.attributeValues = attributeValues; }
//
//    // ✅ Added method for DTO mapping
//    public Map<String, String> getAttributes() {
//        if (attributeValues == null) return null;
//        return attributeValues.stream()
//            .collect(Collectors.toMap(
//                v -> v.getAttribute().getName(),         // key: attribute name
//                VariantAttributeValue::getValue,         // value: attribute value
//                (v1, v2) -> v2                           // ✅ if duplicate key, keep latest
//            ));
//    }
//
//}
//
//











 package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "variants")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore // Infinite loop rokne ke liye
    private Product product;

    @Column(unique = true)
    private String sku; // Stock Keeping Unit (e.g., IPHONE-15-RED-128)

    private Double price;
    private Integer stock;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // ✨ Link to VariantAttributeValue (e.g., Ye variant "Red" hai)
    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantAttributeValue> attributeValues;

    // --- Constructors ---
    public Variant() {}

    public Variant(Product product, String sku, Double price, Integer stock) {
        this.product = product;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<VariantAttributeValue> getAttributeValues() { return attributeValues; }
    public void setAttributeValues(List<VariantAttributeValue> attributeValues) { this.attributeValues = attributeValues; }
}
