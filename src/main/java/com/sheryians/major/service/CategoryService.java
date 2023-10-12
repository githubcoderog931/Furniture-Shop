package com.sheryians.major.service;

import com.sheryians.major.domain.Category;
import com.sheryians.major.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface CategoryService {



    public List<Category> getAllCategory();

    public void addCategory(Category category);

    public void removeCategoryById(int id);

    public Optional<Category> getCategoryById(int id);

    public Optional<Category> getCategoryByName(String name);

    void saveCategory(Category category);



}
