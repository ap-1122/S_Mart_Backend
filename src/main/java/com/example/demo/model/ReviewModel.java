 package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "cart", "roles", "orders"}) 
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"variants", "images", "reviews", "features", "specifications"}) 
    private Product product;

    // ✅ IMPORTANT: Ye field Frontend ko chahiye
    private int rating; 
    
    @Column(length = 1000)
    private String comment;
    
    private LocalDateTime createdAt;

    // --- CONSTRUCTORS ---
    public ReviewModel() {
        this.createdAt = LocalDateTime.now();
    }

    public ReviewModel(UserModel user, Product product, int rating, String comment) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERS & SETTERS (Inke bina JSON me data nahi jayega) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    // 🔴 YE MISSING HONGE, INHE ZAROOR CHECK KARNA
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    
}