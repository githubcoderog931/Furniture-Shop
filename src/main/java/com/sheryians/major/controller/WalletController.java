package com.sheryians.major.controller;

import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.TransactionDetails;
import com.sheryians.major.domain.User;
import com.sheryians.major.domain.Wallet;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.RazorpayService;
import com.sheryians.major.service.UserService;
import com.sheryians.major.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class WalletController {


    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @Autowired
    CartService cartService;

    @Autowired
    RazorpayService razorpayService;

    @GetMapping("/wallet")
    public String showWallet(Model model, Principal principal){
        if(principal!=null){
            User user = userService.getUserByEmail(principal.getName());
            if(user!=null){
                if(user.getWallet()!=null){
                    Wallet wallet = user.getWallet();
                    Double amount = wallet.getWalletAmount();
                    model.addAttribute("user",user);
                    model.addAttribute("wallet",wallet);
                    model.addAttribute("amount",amount);

                    return "user/wallet";
                }
                Wallet wallet = walletService.createWallet(user);
                Double amount = wallet.getWalletAmount();
                model.addAttribute("user",user);
                model.addAttribute("wallet",wallet);
                model.addAttribute("amount",amount);

                return "user/wallet";
            }
        }

        return "redirect:/login";

    }

    @PostMapping("add/amount/")
    public String addMoneyToWallet(@RequestParam("amount")Double amount, Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());
        if (user.getWallet()!=null){
            Wallet wallet = user.getWallet();
            wallet.setWalletAmount(wallet.getWalletAmount()+amount);
            walletService.saveWallet(wallet);
            System.out.println(wallet);
            TransactionDetails transactionDetails = razorpayService.createTransaction(amount);
            model.addAttribute("transactionDetails",transactionDetails);
            System.out.println(transactionDetails);
        }
        TransactionDetails transactionDetails = razorpayService.createTransaction(amount);
        model.addAttribute("transactionDetails",transactionDetails);

        return "redirect:/user";
    }




    }
//    @GetMapping("/payWallet")
//    public String payByWalletPopup(){
//
//    }

