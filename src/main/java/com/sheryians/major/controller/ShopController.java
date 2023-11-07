package com.sheryians.major.controller;

import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;


import com.sheryians.major.domain.User;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;



    @GetMapping("/shop")
    public String shop(Model model, Principal principal){

        String username = principal.getName();
        Cart cart = cartService.getCartForUser(username);

        if(username != null){
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("cart",cart);
        return findPaginated(1,model);
    }


    //Pagination handler..

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo, Model model){
        int pageSize = 4;
        Page<Product> page = productService.findPanginated(pageNo,pageSize);
        List<Product> products = page.getContent();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("products",products);

        return "shop1";
    }




    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id,Principal principal){
        String username = principal.getName();
        Cart cart = cartService.getCartForUser(username);

        if(username != null){
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProductsByCategoryId(id));
        model.addAttribute("cart",cart);

        return "shop1";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id,Principal principal){
        String username = principal.getName();
        Cart cart = cartService.getCartForUser(username);

        if(username != null){
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }
        model.addAttribute("product",productService.getProductById(id) );
        model.addAttribute("cart",cart);

        return "viewProduct";
    }


    @GetMapping("/search")
    public String search(@RequestParam(name = "name", required = false) String name, Model model,Principal principal) {
        String username = principal.getName();
        Cart cart = cartService.getCartForUser(username);

        if(username != null){
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }
        List<Product> searchResults = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            searchResults = productService.ignoreCaseForSearchDescription(name);
        }
        model.addAttribute("search", searchResults);
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("cart",cart);


        return "search1";
    }

//    @GetMapping("/payment")
//    public String payment(Model model){
//        model.addAttribute("categories",categoryService.getAllCategory());
//        model.addAttribute("products",productService.getAllProduct());
//
//        return "payment";
//    }


}
