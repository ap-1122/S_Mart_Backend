 package com.example.demo.config;

import com.example.demo.model.Attribute;
import com.example.demo.model.Category;
import com.example.demo.repository.AttributeRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Check if Categories exist, if not, add them
        if (categoryRepository.count() == 0) {
            Category c1 = new Category(); c1.setName("Electronics"); c1.setDescription("Gadgets and Devices");
            Category c2 = new Category(); c2.setName("Fashion"); c2.setDescription("Clothing and Apparels");
            Category c3 = new Category(); c3.setName("Home & Kitchen"); c3.setDescription("Daily needs");
            
            categoryRepository.saveAll(Arrays.asList(c1, c2, c3));
            System.out.println("✅ Default Categories Added!");
        }

        // 2. Check if Attributes exist, if not, add them
        if (attributeRepository.count() == 0) {
            Attribute a1 = new Attribute(); a1.setName("Color");
            Attribute a2 = new Attribute(); a2.setName("Size");
            Attribute a3 = new Attribute(); a3.setName("Storage"); // For phones
            Attribute a4 = new Attribute(); a4.setName("RAM");     // For laptops
            
            attributeRepository.saveAll(Arrays.asList(a1, a2, a3, a4));
            System.out.println("✅ Default Attributes Added!");
        }
    }
}