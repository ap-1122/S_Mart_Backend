 package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_specifications")
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specKey;   // e.g. "RAM", "Screen Type"
    private String specValue; // e.g. "8GB", "AMOLED"

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    // --- Constructors ---
    public ProductSpecification() {}

    public ProductSpecification(String specKey, String specValue, Product product) {
        this.specKey = specKey;
        this.specValue = specValue;
        this.product = product;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpecKey() { return specKey; }
    public void setSpecKey(String specKey) { this.specKey = specKey; }

    public String getSpecValue() { return specValue; }
    public void setSpecValue(String specValue) { this.specValue = specValue; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}