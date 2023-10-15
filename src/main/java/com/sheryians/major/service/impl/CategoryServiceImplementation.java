package com.sheryians.major.service.impl;


import com.sheryians.major.domain.Category;
import com.sheryians.major.domain.Product;
import com.sheryians.major.repository.CategoryRepository;
import com.sheryians.major.repository.ProductRepository;
import com.sheryians.major.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;


    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {

            throw new RuntimeException("Category name must be unique.");
        }
    }

    @Override
    public void removeCategoryById(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findCategoryByName("name");
    }

    @Override
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
