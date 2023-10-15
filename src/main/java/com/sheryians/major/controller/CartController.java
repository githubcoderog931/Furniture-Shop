package com.sheryians.major.controller;


import com.sheryians.major.service.CartService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @PostMapping("/add-to-cart")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam Long cartId,
            @RequestParam int quantity,
            Principal principal) {

        // Get the authenticated user's username (or other identifier)
        String username = principal.getName();

        // Use the CartService to add the product to the user's cart
        cartService.addToCart("username",cartId, productId, quantity);

        // Redirect to the product list or cart page, depending on your application
        return "redirect:/homepage1";
    }

//    @GetMapping("/cart")
//    public String cartGet(Model model){
//        model.addAttribute("cartCount",GlobalData.cart.size());
//        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
//        model.addAttribute("cart",GlobalData.cart);
//        model.addAttribute("products",productService.getAllProduct());
//        return "/users/cart1";
//    }
//
//    @GetMapping("/cart/removeItem/{index}")
//    public String cartItemRemove(@PathVariable int index){
//        GlobalData.cart.remove((index));
//        return "redirect:/users/cart";
//    }
//
//    @GetMapping("/checkout")
//    public String checkout(Model model){
//        model.addAttribute("cartCount",GlobalData.cart.size());
//        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
//        model.addAttribute("cart",GlobalData.cart);
//        model.addAttribute("products",productService.getAllProduct());
//        return "/users/checkout1";
//    }

}
