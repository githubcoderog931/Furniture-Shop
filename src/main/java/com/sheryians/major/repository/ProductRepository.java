package com.sheryians.major.repository;

import com.sheryians.major.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByCategory_Id(int id);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByName(String product);

    Product findByCategory(String category);

    List<Product> getProductByCategory(int id);

    Optional<Product> findById(Long Id);

    List<Product> findByDescriptionContainingIgnoreCase(String name);

    List<Product> findByPrice(Double price);

}
