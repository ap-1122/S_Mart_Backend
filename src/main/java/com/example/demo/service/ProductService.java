package com.example.demo.service;

import com.example.demo.dto.ProductRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.dto.ProductImageResponseDto;
import com.example.demo.dto.VariantResponseDto;
import com.example.demo.model.*;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private BrandRepository brandRepository;

    // 1. Create Product
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setBrand(brand);
        product.setIsActive(true);

        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    // 2. Get All Products
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // 3. Get Product By ID
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponseDto(product);
    }

    // --- 🔥 MAIN LOGIC UPDATE: Map Entity to DTO (With Safety Checks) ---
    private ProductResponseDto mapToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        
        // ✅ 1. Manufacturer Info Map karo
        dto.setManufacturerInfo(product.getManufacturerInfo());

        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getBrand() != null) {
            dto.setBrandName(product.getBrand().getName());
        }

        // ✅ 2. Map Features (List<Entity> -> List<String>)
        // SAFE: Agar features nahi hain (null), to ye code skip ho jayega. No Error.
        if (product.getFeatures() != null) {
            List<String> featureList = product.getFeatures().stream()
                .map(ProductFeature::getFeature) // Tumhare model me 'getFeature()' hai
                .collect(Collectors.toList());
            dto.setFeatures(featureList);
        }

        // ✅ 3. Map Specifications (List<Entity> -> Map<String, String>)
        // SAFE: Old products me ye null hoga, to koi dikkat nahi.
        if (product.getSpecifications() != null) {
            Map<String, String> specMap = product.getSpecifications().stream()
                .collect(Collectors.toMap(
                    ProductSpecification::getSpecKey,   // e.g. "Ram"
                    ProductSpecification::getSpecValue, // e.g. "8GB"
                    (existing, replacement) -> existing // Duplicate key safety
                ));
            dto.setSpecifications(specMap);
        }

        // --- Images Mapping ---
        if (product.getImages() != null) {
            List<ProductImageResponseDto> imgDtos = product.getImages().stream()
                .map(img -> {
                    ProductImageResponseDto imgDto = new ProductImageResponseDto();
                    imgDto.setId(img.getId());
                    imgDto.setImageUrl(img.getImageUrl());
                    imgDto.setIsPrimary(img.getIsPrimary());
                    imgDto.setDisplayOrder(img.getDisplayOrder());
                    if(img.getVariant() != null) {
                        imgDto.setVariantId(img.getVariant().getId());
                    }
                    return imgDto;
                })
                .collect(Collectors.toList());
            dto.setImages(imgDtos);
        }

        // --- Variants Mapping ---
        if (product.getVariants() != null) {
            List<VariantResponseDto> variantDtos = product.getVariants().stream()
                .map(variant -> {
                    VariantResponseDto vDto = new VariantResponseDto();
                    vDto.setId(variant.getId());
                    vDto.setSku(variant.getSku());
                    vDto.setPrice(variant.getPrice());
                    vDto.setStock(variant.getStock());

                    Map<String, String> attrMap = new HashMap<>();
                    if (variant.getAttributeValues() != null) {
                        for (VariantAttributeValue val : variant.getAttributeValues()) {
                            if (val.getAttribute() != null) {
                                attrMap.put(val.getAttribute().getName(), val.getValue());
                            }
                        }
                    }
                    vDto.setAttributes(attrMap);
                    return vDto;
                })
                .collect(Collectors.toList());
            dto.setVariants(variantDtos);
        }

        return dto;
    }
}





























//THIS PAGE IS WORKING BUT AFTER ADDING SOME FEATURE WE ADD NEW UPPER CODE
//package com.example.demo.service;
//
//import com.example.demo.dto.ProductRequestDto;
//import com.example.demo.dto.ProductResponseDto;
//import com.example.demo.dto.ProductImageResponseDto;
//import com.example.demo.dto.VariantResponseDto;
//import com.example.demo.model.Brand;
//import com.example.demo.model.Category;
//import com.example.demo.model.Product;
//import com.example.demo.model.ProductImage;
//import com.example.demo.model.VariantAttributeValue;
//import com.example.demo.repository.BrandRepository;
//import com.example.demo.repository.CategoryRepository;
//import com.example.demo.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private BrandRepository brandRepository;
//
//    // 1. Create Product
//    public ProductResponseDto createProduct(ProductRequestDto dto) {
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        Brand brand = brandRepository.findById(dto.getBrandId())
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//
//        Product product = new Product();
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setCategory(category);
//        product.setBrand(brand);
//        product.setIsActive(true);
//
//        Product savedProduct = productRepository.save(product);
//        return mapToResponseDto(savedProduct);
//    }
//
//    // 2. Get All Products
//    public List<ProductResponseDto> getAllProducts() {
//        return productRepository.findAll().stream()
//                .map(this::mapToResponseDto)
//                .collect(Collectors.toList());
//    }
//
//    // 3. Get Product By ID
//    public ProductResponseDto getProductById(Long id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return mapToResponseDto(product);
//    }
//
//    // --- Helper Method (FIXED LOGIC HERE) ---
//    private ProductResponseDto mapToResponseDto(Product product) {
//        ProductResponseDto dto = new ProductResponseDto();
//        dto.setId(product.getId());
//        dto.setName(product.getName());
//        dto.setDescription(product.getDescription());
//
//        if (product.getCategory() != null) {
//            dto.setCategoryName(product.getCategory().getName());
//        }
//
//        if (product.getBrand() != null) {
//            dto.setBrandName(product.getBrand().getName());
//        }
//
//        // --- Images Mapping ---
//        if (product.getImages() != null) {
//            List<ProductImageResponseDto> imgDtos = product.getImages().stream()
//                .map(img -> {
//                    ProductImageResponseDto imgDto = new ProductImageResponseDto();
//                    imgDto.setId(img.getId());
//                    imgDto.setImageUrl(img.getImageUrl());
//                    imgDto.setIsPrimary(img.getIsPrimary());
//                    imgDto.setDisplayOrder(img.getDisplayOrder());
//                    if(img.getVariant() != null) {
//                        imgDto.setVariantId(img.getVariant().getId());
//                    }
//                    return imgDto;
//                })
//                .collect(Collectors.toList());
//            dto.setImages(imgDtos);
//        }
//
//        // --- ✅ Variants Mapping (FIXED) ---
//        if (product.getVariants() != null) {
//            List<VariantResponseDto> variantDtos = product.getVariants().stream()
//                .map(variant -> {
//                    VariantResponseDto vDto = new VariantResponseDto();
//                    vDto.setId(variant.getId());
//                    vDto.setSku(variant.getSku());
//                    vDto.setPrice(variant.getPrice());
//                    vDto.setStock(variant.getStock());
//
//                    // ✅ Convert List<VariantAttributeValue> to Map<String, String>
//                    Map<String, String> attrMap = new HashMap<>();
//                    if (variant.getAttributeValues() != null) {
//                        for (VariantAttributeValue val : variant.getAttributeValues()) {
//                            // Ensure attribute and value exist to avoid NullPointer
//                            if (val.getAttribute() != null) {
//                                // Key: Attribute Name (e.g. Color)
//                                // Value: The Value (e.g. Red)
//                                attrMap.put(val.getAttribute().getName(), val.getValue());
//                            }
//                        }
//                    }
//                    vDto.setAttributes(attrMap);
//
//                    return vDto;
//                })
//                .collect(Collectors.toList());
//            dto.setVariants(variantDtos);
//        }
//
//        return dto;
//    }
//}
//











//IN THIS CODE ONLY IMAGE MAPPING ARE THERE BUT AT UPPER CODE WE ADDED IMAGE +VARIENT MAPPING 
// package com.example.demo.service;
//
//import com.example.demo.dto.ProductRequestDto;
//import com.example.demo.dto.ProductResponseDto;
//import com.example.demo.dto.ProductImageResponseDto;
//import com.example.demo.model.Brand;
//import com.example.demo.model.Category;
//import com.example.demo.model.Product;
//import com.example.demo.model.ProductImage; // Import added
//import com.example.demo.repository.BrandRepository;
//import com.example.demo.repository.CategoryRepository;
//import com.example.demo.repository.ProductRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private BrandRepository brandRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    // 1. Create Product
//    public ProductResponseDto createProduct(ProductRequestDto dto) {
//        // Category Fetch
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        // Brand Fetch
//        Brand brand = brandRepository.findById(dto.getBrandId())
//                .orElseThrow(() -> new RuntimeException("Brand not found"));
//
//        // Entity Setup
//        Product product = new Product();
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setCategory(category);
//        product.setBrand(brand);
//        product.setIsActive(true);
//
//        Product savedProduct = productRepository.save(product);
//        return mapToResponseDto(savedProduct);
//    }
//
//    // 2. Get All Products
//    public List<ProductResponseDto> getAllProducts() {
//        return productRepository.findAll().stream()
//                .map(this::mapToResponseDto)
//                .collect(Collectors.toList());
//    }
//
//    // 3. Get Product By ID
//    public ProductResponseDto getProductById(Long id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return mapToResponseDto(product);
//    }
//
//    // --- Helper Method ---
//    private ProductResponseDto mapToResponseDto(Product product) {
//        ProductResponseDto dto = new ProductResponseDto();
//        dto.setId(product.getId());
//        dto.setName(product.getName());
//        dto.setDescription(product.getDescription());
//        
//        if (product.getCategory() != null) {
//            dto.setCategoryName(product.getCategory().getName());
//        }
//        
//        if (product.getBrand() != null) {
//            dto.setBrandName(product.getBrand().getName());
//        }
//        
//        // Convert Images to DTO
//        if (product.getImages() != null) {
//            List<ProductImageResponseDto> imgDtos = product.getImages().stream()
//                .map(img -> {
//                    ProductImageResponseDto imgDto = new ProductImageResponseDto();
//                    imgDto.setId(img.getId());
//                    imgDto.setImageUrl(img.getImageUrl());
//                    imgDto.setIsPrimary(img.getIsPrimary());
//                    imgDto.setDisplayOrder(img.getDisplayOrder());
//                    if(img.getVariant() != null) {
//                        imgDto.setVariantId(img.getVariant().getId());
//                    }
//                    return imgDto;
//                }).collect(Collectors.toList());
//            dto.setImages(imgDtos);
//        }
//        
//        return dto;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//








//
// package com.example.demo.service;
//
//import com.example.demo.dto.ProductCreateDTO;
//import com.example.demo.model.*;
//import com.example.demo.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashSet;
//import java.util.List;
//
//@Service
//public class ProductService {
//
//    @Autowired private ProductRepository productRepository;
//    @Autowired private CategoryRepository categoryRepository;
//    @Autowired private AttributeValueRepository attributeValueRepository;
//
//    // @Transactional ka matlab: Agar beech me koi error aaya (e.g., variant save fail),
//    // to Pura process cancel ho jayega (Parent bhi delete ho jayega). Safe!
//    @Transactional 
//    public Product createProductWithVariants(ProductCreateDTO dto) {
//        
//        // --- STEP 1: CATEGORY DHOONDO ---
//        Category cat = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + dto.getCategoryId()));
//
//        // --- STEP 2: PARENT PRODUCT BANAO ---
//        Product parent = new Product();
//        parent.setName(dto.getName());
//        parent.setDescription(dto.getDescription());
//        parent.setImageUrl(dto.getImageUrl());
//        parent.setCategory(cat);
//        
//        // Parent ko save karo taaki humein iski ID mil jaye
//        Product savedParent = productRepository.save(parent);
//
//        // --- STEP 3: VARIANTS BANAO ---
//        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
//            
//            for (ProductCreateDTO.VariantDTO variantDto : dto.getVariants()) {
//                Product variant = new Product();
//                
//                // Naam thoda descriptive banate hain (Internal use ke liye)
//                variant.setName(savedParent.getName() + " - Variant"); 
//                
//                // Specific details
//                variant.setPrice(variantDto.getPrice());
//                variant.setStock(variantDto.getStock());
//                variant.setSku(variantDto.getSku());
//                
//                // Agar variant ki photo nahi hai, to Parent ki photo use karo
//                variant.setImageUrl(savedParent.getImageUrl()); 
//                
//                // --- RELATIONS SET KARO ---
//                variant.setParentProduct(savedParent); // Baap set kiya
//                variant.setCategory(cat);              // Category same hogi
//                
//                // --- ATTRIBUTES SET KARO (Magic) ---
//                // Frontend ne IDs bheji thi [1, 5], hum Database se wo objects nikalenge
//                List<AttributeValue> attrs = attributeValueRepository.findAllById(variantDto.getAttributeValueIds());
//                variant.setAttributeValues(new HashSet<>(attrs));
//
//                // Variant save karo
//                productRepository.save(variant);
//            }
//        }
//        
//        return savedParent; // Parent wapas bhej do confirmation ke liye
//    }
//}