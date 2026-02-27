package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand; // ✨ New Field

    // ✨ New: Product ke bacche (Variants) - Jaise iPhone 15 (Red, Blue)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();

    // ✨ New: Multiple Images support
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @Column(name = "is_active")
    private Boolean isActive = true;
    
    
 // ✅ NEW FIELD 1: Manufacturer Info (Text Area ke liye)
    @Column(columnDefinition = "TEXT")
    private String manufacturerInfo;

    // ✅ NEW FIELD 2: Link to Specifications
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSpecification> specifications;

    // ✅ NEW FIELD 3: Link to Features (Highlights)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeature> features;

    
 // ✅ NEW: Product और Attributes का रिश्ता (Many-to-Many)
    @ManyToMany
    @JoinTable(
        name = "product_attributes",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private List<Attribute> attributes = new ArrayList<>();
    
    // --- Constructors ---
    public Product() {}

    public Product(String name, String description, Category category, Brand brand) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.brand = brand;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public List<Variant> getVariants() { return variants; }
    public void setVariants(List<Variant> variants) { this.variants = variants; }

    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
 // ✅ NEW GETTERS & SETTERS  
    public String getManufacturerInfo() { return manufacturerInfo; }
    public void setManufacturerInfo(String manufacturerInfo) { this.manufacturerInfo = manufacturerInfo; }

    public List<ProductSpecification> getSpecifications() { return specifications; }
    public void setSpecifications(List<ProductSpecification> specifications) { this.specifications = specifications; }

    public List<ProductFeature> getFeatures() { return features; }
    public void setFeatures(List<ProductFeature> features) { this.features = features; }
    
    public List<Attribute> getAttributes() { return attributes; }
    public void setAttributes(List<Attribute> attributes) { this.attributes = attributes; }

}










//package com.example.demo.model;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Set;
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
//    private BigDecimal price; // Paise ke liye BigDecimal best hai
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
//    @ManyToOne
//    @JoinColumn(name = "parent_product_id")
//    private Product parentProduct;
//
//    // 3. List of Variants
//    @OneToMany(mappedBy = "parentProduct", cascade = CascadeType.ALL)
//    private List<Product> variants;
//
//    // 4. Attributes Connection (Many-to-Many)
//    // Yehi wo field hai jiska error aa raha tha
//    @ManyToMany
//    @JoinTable(
//        name = "product_variant_attributes", // Beech ki bridge table ka naam
//        joinColumns = @JoinColumn(name = "product_id"),
//        inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
//    )
//    private Set<AttributeValue> attributeValues;
//
//    // --- MANUAL GETTERS & SETTERS (Sab Errors Fix Karne Ke Liye) ---
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//
//    public BigDecimal getPrice() { return price; }
//    public void setPrice(BigDecimal price) { this.price = price; }
//
//    public Integer getStock() { return stock; }
//    public void setStock(Integer stock) { this.stock = stock; }
//
//    public String getSku() { return sku; }
//    public void setSku(String sku) { this.sku = sku; }
//
//    public Category getCategory() { return category; }
//    public void setCategory(Category category) { this.category = category; }
//
//    public Product getParentProduct() { return parentProduct; }
//    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }
//
//    public List<Product> getVariants() { return variants; }
//    public void setVariants(List<Product> variants) { this.variants = variants; }
//
//    // -> YE RAHA WO METHOD JO ERROR DE RAHA THA <-
//    public Set<AttributeValue> getAttributeValues() { return attributeValues; }
//    public void setAttributeValues(Set<AttributeValue> attributeValues) { this.attributeValues = attributeValues; }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package com.example.demo.model;
////
////import jakarta.persistence.*;
////import java.math.BigDecimal;
////import java.util.List;
////import java.util.Set;
////
////import io.opencensus.trace.AttributeValue;
////
////@Entity
////@Table(name = "products")
////public class Product {
////    
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    private String name; // Parent: "iPhone 15", Child: "iPhone 15 - Red 128GB"
////    
////    @Column(length = 2000) // Description lamba ho sakta hai
////    private String description;
////    
////    private String imageUrl;
////
////    // --- VARIANT FIELDS (Inki value Parent ke liye null ho sakti hai) ---
////    private BigDecimal price; // Paise ke liye BigDecimal best hai (Calculation sahi hoti hai)
////    private Integer stock;    // Kitne piece bache hain
////    private String sku;       // Stock Keeping Unit (Unique ID warehouse ke liye)
////
////    // --- CONNECTIONS ---
////
////    // 1. Category Connection
////    @ManyToOne
////    @JoinColumn(name = "category_id")
////    private Category category;
////
////    // 2. Parent-Child Connection (Self Join)
////    // Agar ye Parent product hai, to ye field null hoga.
////    // Agar ye Variant hai, to isme Parent Product ki ID hogi.
////    @ManyToOne
////    @JoinColumn(name = "parent_product_id")
////    private Product parentProduct;
////
////    // 3. List of Variants
////    // Parent product ke pass uske bacchon (variants) ki list hogi
////    @OneToMany(mappedBy = "parentProduct", cascade = CascadeType.ALL)
////    private List<Product> variants;
////
////    // 4. Attributes Connection (Many-to-Many)
////    // Ek Variant ke pass bahut saare attributes ho sakte hain (Red + XL + Cotton)
////    @ManyToMany
////    @JoinTable(
////        name = "product_variant_attributes", // Beech ki bridge table ka naam
////        joinColumns = @JoinColumn(name = "product_id"),
////        inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
////    )
////    private Set<AttributeValue> attributeValues;
////
////    // --- GETTERS & SETTERS (IDE se generate kar lena, par main de raha hoon) ---
////    public Long getId() { return id; }
////    public void setId(Long id) { this.id = id; }
////    public String getName() { return name; }
////    public void setName(String name) { this.name = name; }
////    public String getDescription() { return description; }
////    public void setDescription(String description) { this.description = description; }
////    public String getImageUrl() { return imageUrl; }
////    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
////    public BigDecimal getPrice() { return price; }
////    public void setPrice(BigDecimal price) { this.price = price; }
////    public Integer getStock() { return stock; }
////    public void setStock(Integer stock) { this.stock = stock; }
////    public String getSku() { return sku; }
////    public void setSku(String sku) { this.sku = sku; }
////    public Category getCategory() { return category; }
////    public void setCategory(Category category) { this.category = category; }
////    public Product getParentProduct() { return parentProduct; }
////    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }
////    public List<Product> getVariants() { return variants; }
////    public void setVariants(List<Product> variants) { this.variants = variants; }
////    public Set<AttributeValue> getAttributeValues() { return attributeValues; }
////    public void setAttributeValues(Set<AttributeValue> attributeValues) { this.attributeValues = attributeValues; }
////}