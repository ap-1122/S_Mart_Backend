 package com.example.demo.service;

import com.example.demo.dto.BrandRequestDto;
import com.example.demo.dto.BrandResponseDto;
import com.example.demo.model.Brand;
import com.example.demo.repository.BrandRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Create Brand
    public BrandResponseDto createBrand(BrandRequestDto dto) {
        Brand brand = modelMapper.map(dto, Brand.class);
        Brand savedBrand = brandRepository.save(brand);
        return modelMapper.map(savedBrand, BrandResponseDto.class);
    }

    // Get All Active Brands
    public List<BrandResponseDto> getAllBrands() {
        List<Brand> brands = brandRepository.findByIsActiveTrue();
        return brands.stream()
                .map(brand -> modelMapper.map(brand, BrandResponseDto.class))
                .collect(Collectors.toList());
    }

    // Get Brand By ID
    public BrandResponseDto getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        return modelMapper.map(brand, BrandResponseDto.class);
    }
}