 package com.example.demo.service;

import com.example.demo.dto.CategoryRequestDto;
import com.example.demo.dto.CategoryResponseDto;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponseDto createCategory(CategoryRequestDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        
        // Agar Parent Category ID di hai, to use set karo
        if (dto.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent Category not found"));
            category.setParentCategory(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }

    // Sirf Main Categories lao (Jinka koi parent nahi)
    public List<CategoryResponseDto> getRootCategories() {
        List<Category> roots = categoryRepository.findByParentCategoryIsNull();
        return roots.stream()
                .map(cat -> modelMapper.map(cat, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    // Kisi Category ki sub-categories lao
    public List<CategoryResponseDto> getSubCategories(Long parentId) {
        List<Category> subs = categoryRepository.findByParentCategory_Id(parentId);
        return subs.stream()
                .map(cat -> modelMapper.map(cat, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }
}