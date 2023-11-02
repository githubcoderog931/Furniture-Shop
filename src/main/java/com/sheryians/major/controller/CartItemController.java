package com.sheryians.major.controller;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.CartItemRepository;
import com.sheryians.major.repository.CartRepository;
import com.sheryians.major.repository.ProductRepository;
import com.sheryians.major.repository.UserRepository;
import com.sheryians.major.service.CartItemService;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class CartItemController {
    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;
    @Autowired
    ProductService productService;

    @Autowired
    CartItemRepository cartItemRepository;





//    @GetMapping("/addQuantity/{id}")
//    public String addOneTwoQuantity(@PathVariable Long id, Principal principal, Model model){
//        String username = principal.getName();
//        Product product = productService.getProductById(id);
//        CartItem cartItem = cartItemRepository.findByProduct(product);
//
//
//        // accessing units in stock
//        long unitsInStock;
//        int quantity = 0;
//        if(cartItem.getQuantity()<cartItemService.unitsInStockOfProduct(cartItem.getProduct())){
//            quantity = cartItem.getQuantity()+1;
//            unitsInStock =  cartItemService.unitsInStockOfProduct(cartItem.getProduct())-1;
//            product.setUnitsInStock(unitsInStock);
//        }
//        cartItem.setQuantity(quantity);
//        cartItemService.saveCartItem(cartItem);
//        model.addAttribute("quantity",quantity);
//        return "redirect:/cart";
//    }



    @GetMapping("/addQuantity/{id}")
    public String addOneTwoQuantity(@PathVariable Long id, Principal principal, Model model){
        String username = principal.getName();
        Product product = productService.getProductById(id);
        CartItem cartItem = cartItemRepository.findByProduct(product);
        System.out.println(product);
        System.out.println(cartItem);

        // accessing units in stock
        long stock = product.getUnitsInStock();
        long quantity = cartItem.getQuantity();
        if(quantity<stock){
            quantity = quantity+1;
            stock =  stock-1;
        }
        System.out.println(stock);
        System.out.println(quantity);
        model.addAttribute("quantity",quantity);
        model.addAttribute("stock",stock);
        return "redirect:/cart";
    }



    @GetMapping("/minusQuantity/{productId}")
    public String minusQuantity(@PathVariable Long productId, Principal principal,Model model) {
        String username = principal.getName();

        CartItem cartItem = cartItemRepository.findById(productId).get();
        int quantity = 0;
        if (cartItem.getQuantity() >= 1) {
            quantity = cartItem.getQuantity() - 1;
        }else{
            System.out.println("it goes below zero");
        }
        cartItem.setQuantity(quantity);
        cartItemService.saveCartItem(cartItem);
        model.addAttribute("quantity", quantity);
        return "redirect:/cart";
    }
}
