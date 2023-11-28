package com.sheryians.major.controller;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.ProductRepository;
import com.sheryians.major.service.CartItemService;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.ProductService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;

    @Autowired
    UserService userService;

        @GetMapping("cart/add/{productId}")
        public String addToCart(@PathVariable Long productId,
                                Principal principal) {
            if (principal!=null){
                int quantity = 1;

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
            return "redirect:/login";
        }


        @GetMapping("/cart")
            String viewCart(Principal principal, Model model){
            if (principal!=null){
                String username = principal.getName();
                Cart cart = cartService.getCartForUser(username);
                List<CartItem> cartItems = cartItemService.getAllItems(cart);
                double totalPrice = cart.calculateCartTotal();
//                double discount = 0.0;
//                double tax = 0.0;
//                if(totalPrice != 0){
//
//                    discount = 60.0;
//                    tax = 80.0;
//                    totalPrice = totalPrice - (discount + tax);
//                }
                if(username != null){
                    if (cart != null){
                        List<CartItem> cartItemList =  cart.getCartItems();
                        model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                    }
                }
                model.addAttribute("items",cartItems);
                model.addAttribute("cart",cart);
                model.addAttribute("totalPrice",totalPrice);
//                model.addAttribute("discount",discount);
//                model.addAttribute("tax",tax);

                return "cart1";
            }
            return "redirect:/login";
        }


        @GetMapping("delete/{productId}")
    String deleteCartItems(@PathVariable Long productId, RedirectAttributes redidAttrs){
            cartItemService.deleteCartItemById(productId);
            redidAttrs.addFlashAttribute("delete","item deleted successfully");
            return "redirect:/cart";

        }
    }

