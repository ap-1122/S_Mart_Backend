 package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_features")
public class ProductFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feature; // e.g. "100% Cotton", "Made in India"

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    // --- Constructors ---
    public ProductFeature() {}
    
    public ProductFeature(String feature, Product product) {
        this.feature = feature;
        this.product = product;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}