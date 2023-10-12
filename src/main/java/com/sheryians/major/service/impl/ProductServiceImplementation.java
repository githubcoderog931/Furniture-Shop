package com.sheryians.major.service.impl;

import com.sheryians.major.domain.Product;
import com.sheryians.major.repository.ProductRepository;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImplementation implements ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public void removeProductById(long id){
        productRepository.deleteById(id);
    }

    public Optional<Product> getProductById(long id){
        return productRepository.findById(id);
    }

    public List<Product> getAllProductsByCategoryId(int id){
        return productRepository.findAllByCategory_Id(id);
    }

    public List<Product> ignoreCaseForSearch(String name) {return productRepository.findByNameContainingIgnoreCase(name);}

    @Override
    public Product findProductByName(String product) {
        return productRepository.findByName(product);
    }

    @Override
    public Product findProductByCategory(String category) {
        return productRepository.findByCategory(category);
    }


}