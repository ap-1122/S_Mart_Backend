package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Self-Join for Parent Category
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    @JsonIgnore
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategories;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // --- Constructors ---
    public Category() {}

    public Category(String name, String description, Category parentCategory) {
        this.name = name;
        this.description = description;
        this.parentCategory = parentCategory;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getParentCategory() { return parentCategory; }
    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }

    public List<Category> getSubCategories() { return subCategories; }
    public void setSubCategories(List<Category> subCategories) { this.subCategories = subCategories; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
























// package com.example.demo.model;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import java.util.List;
//import com.example.demo.model.Product;
//
//@Entity
//@Table(name = "categories") // Database me table ka naam 'categories' hoga
//public class Category {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    // --- SELF JOIN (Parent-Child) ---
//    // Ek category ka ek hi 'Parent' ho sakta hai (e.g., Mobile ka parent Electronics)
//    @ManyToOne 
//    @JoinColumn(name = "parent_id")
//    private Category parent;
// 
//    private String description;
//
//    // Ek category ke niche bahut saari 'Sub-categories' ho sakti hain
//    // mappedBy = "parent" ka matlab hai ki ye rishta 'parent' field se juda hai
//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    @JsonIgnore // Ye loop rokne ke liye hai (Parent -> Child -> Parent -> Child...)
//    private List<Category> subCategories;
//
//    // Ek category me bahut saare products ho sakte hain
//    @OneToMany(mappedBy = "category")
//    @JsonIgnore
//    private List<Product> products;
//
//    // --- GETTERS & SETTERS (Data lene aur dene ke liye) ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    
//    public Category getParent() { return parent; }
//    public void setParent(Category parent) { this.parent = parent; }
//    
//    public List<Category> getSubCategories() { return subCategories; }
//    public void setSubCategories(List<Category> subCategories) { this.subCategories = subCategories; }
//    
//    public List<Product> getProducts() { return products; }
//    public void setProducts(List<Product> products) { this.products = products; }
//    
// 
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//}