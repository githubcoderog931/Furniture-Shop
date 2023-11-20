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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class CartItemController {
    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;


    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    CartRepository cartRepository;






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
    public String addOneTwoQuantity(@PathVariable Long id, Principal principal, Model model, RedirectAttributes redidAttrs) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        long quantity = 0;
        assert cartItem != null;
        Product product = cartItem.getProduct();
        if(cartItem.getQuantity()>= product.getUnitsInStock()){
            redidAttrs.addFlashAttribute("outOfStock","OOps!.. "+product.getName()+" has Only " + product.getUnitsInStock() + " piece in stock");
                return "redirect:/cart";
        }else {
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
        }
        return "redirect:/cart";
    }



    @GetMapping("/minusQuantity/{id}")
    public String minusQuantity(@PathVariable Long id, Principal principal, Model model, RedirectAttributes redidAttrs) {
        if(id!=null){
            CartItem cartItem = cartItemRepository.findById(id).orElse(null);
            assert cartItem != null;
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            if (quantity>1){
                cartItem.setQuantity(quantity-1);
                cartItemRepository.save(cartItem);
            }
        }
        return "redirect:/cart";
    }
}
