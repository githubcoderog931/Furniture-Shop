package com.sheryians.major.controller;

import com.sheryians.major.domain.*;
import com.sheryians.major.repository.ReferralRepository;
import com.sheryians.major.service.AddressService;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.ReferralService;
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

    @Autowired
    ReferralRepository referralRepository;

    @GetMapping("/user")
    String userProfile(Model model, Principal name){
        if(name!=null){
            User user = userService.findByUsername(name.getName());
            Referral referral = null;
            if(name.getName()!= null){
                Cart cart = cartService.getCartForUser(name.getName());
                if (cart != null){
                    List<CartItem> cartItemList =  cart.getCartItems();
                    model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                }
                referral = referralRepository.findByReferrer(user);
            }
            assert referral != null;
            model.addAttribute("referralCode",referral.getReferralCode());
            model.addAttribute("user",user);

            return "userProfile";
        }
        return "redirect:/login";
    }


}
