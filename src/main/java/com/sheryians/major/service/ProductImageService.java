package com.sheryians.major.service;

import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.ProductImage;
import com.sheryians.major.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {

    // define Autowired
    @Autowired
    ProductImageRepository productImageRepository;


    //get images

    public List<ProductImage> getProductImages(Product product){
        return productImageRepository.findByProduct(product);

    }

    // save images

    public ProductImage saveProductImages(ProductImage productImage){
        return productImageRepository.save(productImage);
    }

    // delete images

    public void deleteProductImages(ProductImage productImage){
        productImageRepository.delete(productImage);
    }

}
