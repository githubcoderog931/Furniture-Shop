package com.sheryians.major.controller;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.ProductImage;
import com.sheryians.major.repository.CartItemRepository;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductImageService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;

    @GetMapping("/")
    public String home(Model model, Principal principal){

        if(principal == null){
            model.addAttribute("categories",categoryService.getAllCategory());
            model.addAttribute("products",productService.getAllProduct());
            return "home";
        }

        if(principal.getName()!= null){
            Cart cart = cartService.getCartForUser(principal.getName());
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProduct());

        return "/home";



    }



    // get request to show product details

    @GetMapping("/productDetails/{id}")
    public String showProductDetails(@PathVariable("id") Long id, Model model){
        Product product = productService.getProductById(id);
        List<ProductImage> productImages =  productImageService.getProductImages(product);
        model.addAttribute("product", product);
        model.addAttribute("productImages",productImages);

        return "productDetails";
    }
}
