package com.sheryians.major.service;

import com.sheryians.major.domain.Category;
import com.sheryians.major.domain.Product;
import com.sheryians.major.repository.CategoryRepository;
import com.sheryians.major.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {

    @Autowired
    CategoryRepository categoryRepository;
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

    public Product getProductById(long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProductsByCategoryId(int id){
        return productRepository.findAllByCategory_Id(id);
    }

    public List<Product> ignoreCaseForSearch(String name) {return productRepository.findByNameContainingIgnoreCase(name);}
    public  List<Product> ignoreCaseForSearchDescription(String name){return productRepository.findByDescriptionContainingIgnoreCase(name);}


    public List<Product> findProductByName(String product) {
        return productRepository.findByName(product);
    }

    public Product findProductByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Page<Product> findPanginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.productRepository.findAll(pageable);
    }

    public List<Product> findProductByPrice(Double min, Double max, int id){
        List<Product> filteredByPrice = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for(Product product : products){
            if(product.getPrice()>min && product.getPrice()<max && product.getCategory().getId()==id){
                filteredByPrice.add(product);
            }
        }
        return filteredByPrice;
    }


}
