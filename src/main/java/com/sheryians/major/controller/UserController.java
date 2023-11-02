package com.sheryians.major.controller;

import com.sheryians.major.domain.Address;
import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.User;
import com.sheryians.major.service.AddressService;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;

    @Autowired
    CartService cartService;

    @GetMapping("/user")
    String userProfile(Model model, Principal name){
        User user = userService.findByUsername(name.getName());
        if(name.getName()!= null){
            Cart cart = cartService.getCartForUser(name.getName());
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }

        model.addAttribute("user",user);

        return "userProfile";
    }






}
