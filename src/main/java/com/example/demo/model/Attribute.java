package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "attributes")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeValue> values;

    // --- Constructors ---
    public Attribute() {}

    public Attribute(String name) {
        this.name = name;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<AttributeValue> getValues() { return values; }
    public void setValues(List<AttributeValue> values) { this.values = values; }
}





















// package com.example.demo.model;
//
//import jakarta.persistence.*;
//import java.util.List;
//
// 
//
//@Entity
//@Table(name = "attributes")
//public class Attribute {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name; // E.g., "Color" ya "Size"
//
//    // Ek Attribute ke bahut saare Values ho sakte hain (Color -> Red, Blue, Green)
//    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
//    private List<AttributeValue> values; // Ab ye sahi wala Model uthayega
//
//    // Getters & Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    
//    public List<AttributeValue> getValues() { return values; }
//    public void setValues(List<AttributeValue> values) { this.values = values; }
//}
