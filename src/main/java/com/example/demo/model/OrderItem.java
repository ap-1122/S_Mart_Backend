package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Import Add karo

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore // Loop rokne ke liye
    private Order order;

    // 🔴 CHANGE 1: Product ko control karo
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "variants", "images", "brand", "category", "features", "specifications", "description", "manufacturerInfo"}) 
    private Product product;

    // 🔴 CHANGE 2: Variant ko control karo
    @ManyToOne
    @JoinColumn(name = "variant_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "product", "attributeValues"})
    private Variant variant;

    private int quantity;
    private double orderedPrice;

    // --- CONSTRUCTORS ---
    public OrderItem() {}

    public OrderItem(Order order, Product product, Variant variant, int quantity, double orderedPrice) {
        this.order = order;
        this.product = product;
        this.variant = variant;
        this.quantity = quantity;
        this.orderedPrice = orderedPrice;
    }

    // --- GETTERS & SETTERS (Same as before) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Variant getVariant() { return variant; }
    public void setVariant(Variant variant) { this.variant = variant; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getOrderedPrice() { return orderedPrice; }
    public void setOrderedPrice(double orderedPrice) { this.orderedPrice = orderedPrice; }
    
 // ✅ NEW: Ye method Frontend ko image URL dega
    // Jackson ise automatic "productImage" field maan lega
    public String getProductImage() {
        if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
            return product.getImages().get(0).getImageUrl();
        }
        return null; // Frontend placeholder use kar lega
    }
}

















//THIS CODE CREATE UNNESSESURY INFINITE DATa send PROBLEM TO FRONTEND TO UPDATE IN UPPER CODE THIS IS SAME PROMLEM AS PREVIOUS DURING WE CREATE CART
// package com.example.demo.model;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//public class OrderItem {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    @JsonIgnore // Loop rokne ke liye
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "variant_id")
//    private Variant variant;
//
//    private int quantity;
//    private double orderedPrice; // PRICE FREEZE: Order ke time ki price
//
//    // --- CONSTRUCTORS ---
//    public OrderItem() {}
//
//    public OrderItem(Order order, Product product, Variant variant, int quantity, double orderedPrice) {
//        this.order = order;
//        this.product = product;
//        this.variant = variant;
//        this.quantity = quantity;
//        this.orderedPrice = orderedPrice;
//    }
//
//    // --- GETTERS & SETTERS ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Order getOrder() { return order; }
//    public void setOrder(Order order) { this.order = order; }
//
//    public Product getProduct() { return product; }
//    public void setProduct(Product product) { this.product = product; }
//
//    public Variant getVariant() { return variant; }
//    public void setVariant(Variant variant) { this.variant = variant; }
//
//    public int getQuantity() { return quantity; }
//    public void setQuantity(int quantity) { this.quantity = quantity; }
//
//    public double getOrderedPrice() { return orderedPrice; }
//    public void setOrderedPrice(double orderedPrice) { this.orderedPrice = orderedPrice; }
//}