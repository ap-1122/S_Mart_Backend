package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Parent: "iPhone 15", Child: "iPhone 15 - Red 128GB"
    
    @Column(length = 2000) // Description lamba ho sakta hai
    private String description;
    
    private String imageUrl;

    // --- VARIANT FIELDS (Inki value Parent ke liye null ho sakti hai) ---
    private BigDecimal price; // Paise ke liye BigDecimal best hai
    private Integer stock;    // Kitne piece bache hain
    private String sku;       // Stock Keeping Unit (Unique ID warehouse ke liye)

    // --- CONNECTIONS ---

    // 1. Category Connection
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // 2. Parent-Child Connection (Self Join)
    @ManyToOne
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;

    // 3. List of Variants
    @OneToMany(mappedBy = "parentProduct", cascade = CascadeType.ALL)
    private List<Product> variants;

    // 4. Attributes Connection (Many-to-Many)
    // Yehi wo field hai jiska error aa raha tha
    @ManyToMany
    @JoinTable(
        name = "product_variant_attributes", // Beech ki bridge table ka naam
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private Set<AttributeValue> attributeValues;

    // --- MANUAL GETTERS & SETTERS (Sab Errors Fix Karne Ke Liye) ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Product getParentProduct() { return parentProduct; }
    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }

    public List<Product> getVariants() { return variants; }
    public void setVariants(List<Product> variants) { this.variants = variants; }

    // -> YE RAHA WO METHOD JO ERROR DE RAHA THA <-
    public Set<AttributeValue> getAttributeValues() { return attributeValues; }
    public void setAttributeValues(Set<AttributeValue> attributeValues) { this.attributeValues = attributeValues; }
}




















//package com.example.demo.model;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Set;
//
//import io.opencensus.trace.AttributeValue;
//
//@Entity
//@Table(name = "products")
//public class Product {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name; // Parent: "iPhone 15", Child: "iPhone 15 - Red 128GB"
//    
//    @Column(length = 2000) // Description lamba ho sakta hai
//    private String description;
//    
//    private String imageUrl;
//
//    // --- VARIANT FIELDS (Inki value Parent ke liye null ho sakti hai) ---
//    private BigDecimal price; // Paise ke liye BigDecimal best hai (Calculation sahi hoti hai)
//    private Integer stock;    // Kitne piece bache hain
//    private String sku;       // Stock Keeping Unit (Unique ID warehouse ke liye)
//
//    // --- CONNECTIONS ---
//
//    // 1. Category Connection
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;
//
//    // 2. Parent-Child Connection (Self Join)
//    // Agar ye Parent product hai, to ye field null hoga.
//    // Agar ye Variant hai, to isme Parent Product ki ID hogi.
//    @ManyToOne
//    @JoinColumn(name = "parent_product_id")
//    private Product parentProduct;
//
//    // 3. List of Variants
//    // Parent product ke pass uske bacchon (variants) ki list hogi
//    @OneToMany(mappedBy = "parentProduct", cascade = CascadeType.ALL)
//    private List<Product> variants;
//
//    // 4. Attributes Connection (Many-to-Many)
//    // Ek Variant ke pass bahut saare attributes ho sakte hain (Red + XL + Cotton)
//    @ManyToMany
//    @JoinTable(
//        name = "product_variant_attributes", // Beech ki bridge table ka naam
//        joinColumns = @JoinColumn(name = "product_id"),
//        inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
//    )
//    private Set<AttributeValue> attributeValues;
//
//    // --- GETTERS & SETTERS (IDE se generate kar lena, par main de raha hoon) ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//    public BigDecimal getPrice() { return price; }
//    public void setPrice(BigDecimal price) { this.price = price; }
//    public Integer getStock() { return stock; }
//    public void setStock(Integer stock) { this.stock = stock; }
//    public String getSku() { return sku; }
//    public void setSku(String sku) { this.sku = sku; }
//    public Category getCategory() { return category; }
//    public void setCategory(Category category) { this.category = category; }
//    public Product getParentProduct() { return parentProduct; }
//    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }
//    public List<Product> getVariants() { return variants; }
//    public void setVariants(List<Product> variants) { this.variants = variants; }
//    public Set<AttributeValue> getAttributeValues() { return attributeValues; }
//    public void setAttributeValues(Set<AttributeValue> attributeValues) { this.attributeValues = attributeValues; }
//}