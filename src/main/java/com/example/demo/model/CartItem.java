 package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Import Zaroori hai

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cart wapas mat bhejo (Loop Stop)
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    // Product dikhao, lekin uske andar ki heavy cheezein mat dikhao
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "variants", "images", "brand", "category", "features", "specifications", "description", "manufacturerInfo"}) 
    private Product product;

    // Variant dikhao, lekin wapas Product mat dikhao
    @ManyToOne
    @JoinColumn(name = "variant_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "product", "attributeValues"})
    private Variant variant;

    private int quantity;

    // --- CONSTRUCTORS ---
    public CartItem() {}

    public CartItem(Cart cart, Product product, Variant variant, int quantity) {
        this.cart = cart;
        this.product = product;
        this.variant = variant;
        this.quantity = quantity;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Variant getVariant() { return variant; }
    public void setVariant(Variant variant) { this.variant = variant; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Helper Method
    public double getSubTotal() {
        if (variant != null) {
            return variant.getPrice() * quantity;
        }
        return 0.0;
    }
    
 // ... existing code ...

    // Frontend ko Image URL dene ke liye
    public String getProductImage() {
        if (product != null && product.getImages() != null && !product.getImages().isEmpty()) {
            return product.getImages().get(0).getImageUrl();
        }
        return "https://via.placeholder.com/150";
    }
}