package com.sheryians.major.controller;


import com.sheryians.major.domain.*;
import com.sheryians.major.repository.CartItemRepository;
import com.sheryians.major.repository.ReferralRepository;
import com.sheryians.major.repository.WalletRepository;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    ReferralService referralService;

    @Autowired
    ReferralRepository referralRepository;

    @GetMapping("/")
    public String home(Model model, Principal principal, RedirectAttributes redirectAttributes){


        if(principal!=null){
            User user = userService.getUserByEmail(principal.getName());
            if (user.getWallet()==null){
                Wallet wallet = new Wallet();
                wallet.setUser(user);
                wallet.setWalletAmount(0.0);
                walletRepository.save(wallet);
            }
            System.out.println("has wallet");

            if (user.getReferralCode()==null){
                Referral referral = new Referral();
                String referralCode = referralService.generateReferralCode(principal.getName());
                redirectAttributes.addFlashAttribute("referralCode", referralCode);
                referral.setReferralCode(referralCode);
                referral.setCompleted(false);
                referral.setReferrerPurchase(false);
                referral.setReferredPurchase(false);
                referral.setReferrer(user);
                referralRepository.save(referral);
                System.out.println();
            }
        }

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

        return "home";



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
