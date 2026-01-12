package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "variant_attribute_values")
public class VariantAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    @JsonIgnore
    private Variant variant;

    // ✅ This was missing or causing the error
    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute; 

    private String value; // e.g., "Red", "XL"

    // --- Constructors ---
    public VariantAttributeValue() {}

    public VariantAttributeValue(Variant variant, Attribute attribute, String value) {
        this.variant = variant;
        this.attribute = attribute;
        this.value = value;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Variant getVariant() { return variant; }
    public void setVariant(Variant variant) { this.variant = variant; }

    // ✅ Ye method ab ProductService me call hoga
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
//@Table(name = "variant_attribute_values")
//public class VariantAttributeValue {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "variant_id", nullable = false)
//    @JsonIgnore
//    private Variant variant;
//
//    @ManyToOne
//    @JoinColumn(name = "attribute_value_id", nullable = false)
//    private AttributeValue attributeValue; // e.g., Red, XL
//
//    // --- Constructors ---
//    public VariantAttributeValue() {}
//
//    public VariantAttributeValue(Variant variant, AttributeValue attributeValue) {
//        this.variant = variant;
//        this.attributeValue = attributeValue;
//    }
//
//    // --- Getters and Setters ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Variant getVariant() { return variant; }
//    public void setVariant(Variant variant) { this.variant = variant; }
//
//    public AttributeValue getAttributeValue() { return attributeValue; }
//    public void setAttributeValue(AttributeValue attributeValue) { this.attributeValue = attributeValue; }
//}