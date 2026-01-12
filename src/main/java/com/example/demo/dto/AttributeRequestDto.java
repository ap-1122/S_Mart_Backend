 package com.example.demo.dto;

import java.util.List;

public class AttributeRequestDto {

    private String name;
    private List<String> values; // Hum seedha list bhejenge (Red, Blue)

    // --- Constructors ---
    public AttributeRequestDto() {}

    public AttributeRequestDto(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    // --- Getters and Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getValues() { return values; }
    public void setValues(List<String> values) { this.values = values; }
}