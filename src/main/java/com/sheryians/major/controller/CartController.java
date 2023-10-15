package com.sheryians.major.controller;

import com.sheryians.major.domain.Product;
import com.sheryians.major.global.GlobalData;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id, HttpServletRequest request){

        String referer = request.getHeader("Referer");

        if (referer != null && referer.contains("/shop")) {
            // If the referring page contains "/shop", redirect to the shop page
            GlobalData.cart.add(productService.getProductById(id).get());

            return "redirect:/shop";
        } else if (referer.contains("/cart")){
            // Otherwise, redirect to the home page
            GlobalData.cart.add(productService.getProductById(id).get());

            return "redirect:/cart";
        } else if (referer.contains("/search")) {
            // Otherwise, redirect to the home page
            GlobalData.cart.add(productService.getProductById(id).get());

            return "redirect:/shop";
        } else{
            GlobalData.cart.add(productService.getProductById(id).get());
            return "redirect:/";

        }
    }


    @GetMapping("/cart")
    public String cartGet(Model model){
        model.addAttribute("cartCount",GlobalData.cart.size());
        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart",GlobalData.cart);
        model.addAttribute("products",productService.getAllProduct());
        return "/users/cart1";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String cartItemRemove(@PathVariable int index){
        GlobalData.cart.remove((index));
        return "redirect:/users/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){
        model.addAttribute("cartCount",GlobalData.cart.size());
        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart",GlobalData.cart);
        model.addAttribute("products",productService.getAllProduct());
        return "/users/checkout1";
    }

}
