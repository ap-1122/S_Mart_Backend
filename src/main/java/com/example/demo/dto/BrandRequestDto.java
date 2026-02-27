 package com.example.demo.dto;

public class BrandRequestDto {

    private String name;
    private String description;
    private String logoUrl;

    // --- Constructors ---
    public BrandRequestDto() {}

    public BrandRequestDto(String name, String description, String logoUrl) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
    }

    // --- Getters and Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}