package com.example.demo.repository;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  // Sirf wo Categories lao jinka koi baap (Parent) nahi hai (Root Categories) [cite: 700]
    List<Category> findByParentCategoryIsNull();

     // Kisi specific Parent ki sub-categories lao (e.g. Electronics ke andar kya hai?) [cite: 701]
    List<Category> findByParentCategory_Id(Long parentId);
    
    // Active categories
    List<Category> findByIsActiveTrue();
}









// package com.example.demo.repository;
//import com.example.demo.model.Category;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface CategoryRepository extends JpaRepository<Category, Long> {
//}