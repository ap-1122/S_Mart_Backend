 package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @OneToMany(mappedBy = "brand", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // --- Constructors ---
    public Brand() {}

    public Brand(String name, String description, String logoUrl) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}