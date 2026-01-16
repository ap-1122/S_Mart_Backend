 package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "addresses") // Database me table ka naam 'addresses' hoga
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- User Link ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Infinite Loop rokne ke liye
    private UserModel user;

    // --- Address Fields ---
    private String name; // Receiver ka naam (e.g. Aadi Singh)
    private String mobile; // Delivery ke liye contact number
    
    private String street; // House No, Gali No.
    private String city;
    private String state;
    private String zipCode;
    
    // Country default "India" rakh sakte hain ya user se le sakte hain
    private String country = "India"; 

    // --- Constructors ---
    public Address() {}

    public Address(UserModel user, String name, String mobile, String street, String city, String state, String zipCode) {
        this.user = user;
        this.name = name;
        this.mobile = mobile;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}