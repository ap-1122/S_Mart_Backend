 package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import com.example.demo.model.Product;

@Entity
@Table(name = "categories") // Database me table ka naam 'categories' hoga
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // --- SELF JOIN (Parent-Child) ---
    // Ek category ka ek hi 'Parent' ho sakta hai (e.g., Mobile ka parent Electronics)
    @ManyToOne 
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Ek category ke niche bahut saari 'Sub-categories' ho sakti hain
    // mappedBy = "parent" ka matlab hai ki ye rishta 'parent' field se juda hai
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnore // Ye loop rokne ke liye hai (Parent -> Child -> Parent -> Child...)
    private List<Category> subCategories;

    // Ek category me bahut saare products ho sakte hain
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    // --- GETTERS & SETTERS (Data lene aur dene ke liye) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Category getParent() { return parent; }
    public void setParent(Category parent) { this.parent = parent; }
    
    public List<Category> getSubCategories() { return subCategories; }
    public void setSubCategories(List<Category> subCategories) { this.subCategories = subCategories; }
    
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}