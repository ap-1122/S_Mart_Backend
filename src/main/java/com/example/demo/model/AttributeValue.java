package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attribute_values")
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    @JsonIgnore
    private Attribute attribute;

    @Column(nullable = false)
    private String value;

    // --- Constructors ---
    public AttributeValue() {}

    public AttributeValue(Attribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Attribute getAttribute() { return attribute; }
    public void setAttribute(Attribute attribute) { this.attribute = attribute; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}











// package com.example.demo.model;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//@Table(name = "attribute_values")
//public class AttributeValue {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
// // --- FIX IS HERE ---
//    @Column(name = "attr_value") // Database me column ka naam 'attr_value' hoga
//    private String value; // E.g., "Red" ya "XL"
//
//    // Har value kisi ek Attribute se judi hoti hai (Red juda hai Color se)
//    @ManyToOne
//    @JoinColumn(name = "attribute_id")
//    @JsonIgnore
//    private Attribute attribute;
//
//    // Getters & Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    
//    public String getValue() { return value; }
//    public void setValue(String value) { this.value = value; }
//    
//    public Attribute getAttribute() { return attribute; }
//    public void setAttribute(Attribute attribute) { this.attribute = attribute; }
//}