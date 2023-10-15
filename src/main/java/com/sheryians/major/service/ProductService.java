package com.sheryians.major.service;


import com.sheryians.major.domain.Product;
import com.sheryians.major.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductService {


    public List<Product> getAllProduct();

    public void addProduct(Product product);

    public void removeProductById(long id);

    public Optional<Product> getProductById(long id);

    public List<Product> getAllProductsByCategoryId(int id);


    List<Product> ignoreCaseForSearch(String name);


    List<Product> findProductByName(String product);

    Product findProductByCategory(String category);







}
