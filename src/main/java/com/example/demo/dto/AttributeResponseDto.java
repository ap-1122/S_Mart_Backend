 package com.example.demo.dto;

import java.util.List;

public class AttributeResponseDto {

    private Long id;
    private String name;
    private List<String> values;

    // --- Constructors ---
    public AttributeResponseDto() {}

    public AttributeResponseDto(Long id, String name, List<String> values) {
        this.id = id;
        this.name = name;
        this.values = values;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getValues() { return values; }
    public void setValues(List<String> values) { this.values = values; }
}