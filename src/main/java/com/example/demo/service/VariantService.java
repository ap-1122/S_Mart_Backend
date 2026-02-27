package com.example.demo.service;

import com.example.demo.dto.VariantCreateRequestDto;
import com.example.demo.dto.VariantResponseDto;
import com.example.demo.model.Attribute;
import com.example.demo.model.Product;
import com.example.demo.model.Variant;
import com.example.demo.model.VariantAttributeValue;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.VariantRepository;
import com.example.demo.repository.VariantAttributeValueRepository; // Ensure ye file ho
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VariantService {

    @Autowired private VariantRepository variantRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AttributeRepository attributeRepository;
    
    // ✅ Safety: Isse hum attributes ko alag se save karenge
    @Autowired private VariantAttributeValueRepository valRepo; 

    // 1. CREATE VARIANT
    public VariantResponseDto createVariant(Long productId, VariantCreateRequestDto dto) {
        // Product Check
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Variant Setup
        Variant variant = new Variant();
        variant.setProduct(product);
        variant.setSku(dto.getSku());
        variant.setPrice(dto.getPrice());
        variant.setStock(dto.getStock());
        variant.setIsActive(true);

        // ✅ Step A: Pehle Variant Save karo (Taaki ID generate ho jaye)
        Variant savedVariant = variantRepository.save(variant);

        // ✅ Step B: Ab Attributes Save karo (String -> Long Convert karke)
        if (dto.getAttributes() != null) {
            List<VariantAttributeValue> valuesList = new ArrayList<>();
            
            for (Map.Entry<String, String> entry : dto.getAttributes().entrySet()) {
                try {
                    // "1" (String) ko 1 (Long) me badalna
                    Long attrId = Long.parseLong(entry.getKey()); 
                    String attrVal = entry.getValue();

                    Attribute attribute = attributeRepository.findById(attrId)
                            .orElseThrow(() -> new RuntimeException("Attribute ID " + attrId + " not found"));

                    VariantAttributeValue val = new VariantAttributeValue();
                    val.setVariant(savedVariant);
                    val.setAttribute(attribute);
                    val.setValue(attrVal);
                    
                    valuesList.add(val);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid Attribute ID format: " + entry.getKey());
                }
            }
            // Bulk Save Values
            valRepo.saveAll(valuesList);
            savedVariant.setAttributeValues(valuesList);
        }

        return mapToDto(savedVariant);
    }

    // 2. GET VARIANTS
    public List<VariantResponseDto> getVariantsByProduct(Long productId) {
        List<Variant> variants = variantRepository.findByProductId(productId);
        return variants.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // 3. MAP TO DTO
    private VariantResponseDto mapToDto(Variant variant) {
        VariantResponseDto dto = new VariantResponseDto();
        dto.setId(variant.getId());
        dto.setSku(variant.getSku());
        dto.setPrice(variant.getPrice());
        dto.setStock(variant.getStock());

        Map<String, String> attributes = new HashMap<>();
        if (variant.getAttributeValues() != null) {
            for (VariantAttributeValue vav : variant.getAttributeValues()) {
                if (vav.getAttribute() != null) {
                    attributes.put(vav.getAttribute().getName(), vav.getValue());
                }
            }
        }
        dto.setAttributes(attributes);
        return dto;
    }
}































// package com.example.demo.service;
//
//import com.example.demo.dto.VariantCreateRequestDto;
//import com.example.demo.dto.VariantResponseDto;
//import com.example.demo.model.Attribute;
//import com.example.demo.model.Product;
//import com.example.demo.model.Variant;
//import com.example.demo.model.VariantAttributeValue;
//import com.example.demo.repository.AttributeRepository;
//import com.example.demo.repository.ProductRepository;
//import com.example.demo.repository.VariantRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class VariantService {
//
//    @Autowired
//    private VariantRepository variantRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private AttributeRepository attributeRepository;
//
//    // ✅ 1. CREATE VARIANT (Fixed Logic)
//    public VariantResponseDto createVariant(Long productId, VariantCreateRequestDto dto) {
//        // Product Check
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
//
//        // Create Variant
//        Variant variant = new Variant();
//        variant.setProduct(product);
//        variant.setSku(dto.getSku());
//        variant.setPrice(dto.getPrice());
//        variant.setStock(dto.getStock());
//        variant.setIsActive(true);
//
//        // Attributes Logic (Map<Long, String>)
//        List<VariantAttributeValue> attributeValues = new ArrayList<>();
//        
//        if (dto.getAttributes() != null) {
//            for (Map.Entry<Long, String> entry : dto.getAttributes().entrySet()) {
//                Long attrId = entry.getKey();   // e.g. 1 (Color)
//                String attrVal = entry.getValue(); // e.g. "Red"
//
//                // DB se Attribute find karo ID ke basis par (Safer)
//                Attribute attribute = attributeRepository.findById(attrId)
//                        .orElseThrow(() -> new RuntimeException("Attribute not found with ID: " + attrId));
//
//                // Value save karo
//                VariantAttributeValue val = new VariantAttributeValue();
//                val.setVariant(variant);
//                val.setAttribute(attribute);
//                val.setValue(attrVal); // String value set ho rahi hai
//                
//                attributeValues.add(val);
//            }
//        }
//        variant.setAttributeValues(attributeValues);
//
//        // Save
//        Variant savedVariant = variantRepository.save(variant);
//        return mapToDto(savedVariant);
//    }
//
//    // ✅ 2. GET VARIANTS
//    public List<VariantResponseDto> getVariantsByProduct(Long productId) {
//        List<Variant> variants = variantRepository.findByProductId(productId);
//        return variants.stream().map(this::mapToDto).collect(Collectors.toList());
//    }
//
//    // ✅ 3. HELPER METHOD
//    private VariantResponseDto mapToDto(Variant variant) {
//        VariantResponseDto dto = new VariantResponseDto();
//        dto.setId(variant.getId());
//        dto.setSku(variant.getSku());
//        dto.setPrice(variant.getPrice());
//        dto.setStock(variant.getStock());
//
//        // Map Conversion
//        Map<String, String> attributes = new HashMap<>();
//        if (variant.getAttributeValues() != null) {
//            for (VariantAttributeValue vav : variant.getAttributeValues()) {
//                if (vav.getAttribute() != null) {
//                    attributes.put(
//                        vav.getAttribute().getName(), // Key: Color
//                        vav.getValue()                // Value: Red
//                    );
//                }
//            }
//        }
//        dto.setAttributes(attributes);
//        return dto;
//    }
//}
//
//







//package com.example.demo.service;
//
//import com.example.demo.dto.VariantResponseDto;
//import com.example.demo.model.Variant;
//import com.example.demo.model.VariantAttributeValue;
//import com.example.demo.repository.VariantRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class VariantService {
//
//    @Autowired
//    private VariantRepository variantRepository;
//
//    // ... (Baki methods agar hain to wahi rahne do, bas helper method fix karo) ...
//
//    // ✅ FIXED HELPER METHOD
//    private VariantResponseDto mapToDto(Variant variant) {
//        VariantResponseDto dto = new VariantResponseDto();
//        dto.setId(variant.getId());
//        dto.setSku(variant.getSku());
//        dto.setPrice(variant.getPrice());
//        dto.setStock(variant.getStock());
//
//        // Attributes Mapping Logic (Fixed)
//        Map<String, String> attributes = new HashMap<>();
//        if (variant.getAttributeValues() != null) {
//            for (VariantAttributeValue vav : variant.getAttributeValues()) {
//                // Null check zaroori hai
//                if (vav.getAttribute() != null) {
//                    // Key: Attribute Name (e.g., Color)
//                    // Value: Attribute Value (e.g., Red)
//                    attributes.put(
//                        vav.getAttribute().getName(), 
//                        vav.getValue() // ✅ Ab ye sahi method call karega
//                    );
//                }
//            }
//        }
//        dto.setAttributes(attributes);
//
//        return dto;
//    }
//}




















// package com.example.demo.service;
//
//import com.example.demo.dto.VariantCreateRequestDto;
//import com.example.demo.dto.VariantResponseDto;
//import com.example.demo.model.*;
//import com.example.demo.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class VariantService {
//
//    @Autowired
//    private VariantRepository variantRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private AttributeValueRepository attributeValueRepository;
//    
//    // Agar ye repo nahi banayi thi, to please bana lena (Simple JpaRepository)
//    @Autowired
//    private VariantAttributeValueRepository variantAttributeValueRepository; 
//
//    public VariantResponseDto createVariant(Long productId, VariantCreateRequestDto dto) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // 1. Basic Variant Save
//        Variant variant = new Variant();
//        variant.setProduct(product);
//        variant.setSku(dto.getSku());
//        variant.setPrice(dto.getPrice());
//        variant.setStock(dto.getStock());
//        variant.setIsActive(true);
//        
//        Variant savedVariant = variantRepository.save(variant);
//
//        // 2. Attributes Link Karo (e.g. Color -> Red)
//        if (dto.getAttributes() != null) {
//            for (Map.Entry<Long, Long> entry : dto.getAttributes().entrySet()) {
//                // Key = AttributeId (Ignore for now, value is unique enough), Value = AttributeValueId
//                Long attrValueId = entry.getValue();
//                
//                AttributeValue attrValue = attributeValueRepository.findById(attrValueId)
//                        .orElseThrow(() -> new RuntimeException("Attribute Value not found ID: " + attrValueId));
//
//                VariantAttributeValue map = new VariantAttributeValue();
//                map.setVariant(savedVariant);
//                map.setAttributeValue(attrValue);
//                
//                variantAttributeValueRepository.save(map);
//            }
//        }
//
//        return mapToDto(savedVariant);
//    }
//
//    public List<VariantResponseDto> getVariantsByProduct(Long productId) {
//        return variantRepository.findByProductId(productId).stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//    }
//
//    private VariantResponseDto mapToDto(Variant variant) {
//        VariantResponseDto dto = new VariantResponseDto();
//        dto.setId(variant.getId());
//        dto.setSku(variant.getSku());
//        dto.setPrice(variant.getPrice());
//        dto.setStock(variant.getStock());
//        
//        // Attributes fetch karke map me dalo
//        Map<String, String> attributes = new HashMap<>();
//        if (variant.getAttributeValues() != null) {
//            for (VariantAttributeValue vav : variant.getAttributeValues()) {
//                attributes.put(
//                    vav.getAttributeValue().getAttribute().getName(), // Key: "Color"
//                    vav.getAttributeValue().getValue()               // Value: "Red"
//                );
//            }
//        }
//        dto.setAttributes(attributes);
//        return dto;
//    }
//}