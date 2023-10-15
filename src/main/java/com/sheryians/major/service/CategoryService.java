package com.sheryians.major.service;


import com.sheryians.major.domain.Category;
import com.sheryians.major.repository.CategoryRepository;
import com.sheryians.major.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;



    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }


    public void addCategory(Category category) {
        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {

            throw new RuntimeException("Category name must be unique.");
        }
    }


    public void removeCategoryById(int id) {
        categoryRepository.deleteById(id);
    }


    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }


    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findCategoryByName("name");
    }


    public void saveCategory(Category category) {categoryRepository.save(category);}

//    @Override
//    public boolean getCategoryBooleanId(int id) {
//       List<Product> optionalProduct = productRepository.getProductByCategory(id);
//       if (optionalProduct!=null){
//           return true;
//       }
//       return false;
//    }


}
