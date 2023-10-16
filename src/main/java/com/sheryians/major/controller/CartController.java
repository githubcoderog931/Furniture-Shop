package com.sheryians.major.controller;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.ProductRepository;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.ProductService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

        @GetMapping("cart/add")
        public String addToCart(@RequestParam Long productId,
                                @RequestParam int quantity,
                                Principal principal) {
            if (principal == null) {
                // Handle the case where the user is not logged in
                return "redirect:/login"; // Redirect to the login page
            }

            // Get the authenticated user's username
            String username = principal.getName();

            // Retrieve the user by their username (you need to implement this logic)
            User user = userService.findByUsername(username);

            if (user != null) {
                cartService.addToCart(user, productId, quantity);
            }

            // Redirect to a page, e.g., product details or cart view
            return "redirect:/shop";
        }


        @GetMapping("/cart")
    String viewCart(Principal principal, Model model){
            String username = principal.getName();
            Cart cart = cartService.getCartForUser(username);
            model.addAttribute("cart",cart);
            return "cart1";
        }
    }

