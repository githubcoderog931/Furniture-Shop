package com.sheryians.major.controller;

import com.sheryians.major.domain.Product;
import com.sheryians.major.global.GlobalData;

import com.sheryians.major.repository.ProductRepository;
import com.sheryians.major.repository.UserRepository;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;



    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("cartCount",GlobalData.cart.size());

        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id){
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProductsByCategoryId(id));
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id){
        model.addAttribute("product",productService.getProductById(id).get() );
        model.addAttribute("cartCount",GlobalData.cart.size());

        return "viewProduct";
    }


    @GetMapping("/search")
    public String search(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Product> searchResults = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            searchResults = productService.ignoreCaseForSearch(name);
        }
        model.addAttribute("search", searchResults);
        return "search";
    }


}
