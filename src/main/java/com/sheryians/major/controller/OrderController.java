package com.sheryians.major.controller;

import com.sheryians.major.domain.Address;
import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.AddressRepository;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    // define autowire

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;


    // create order controller

    @GetMapping("/orderPlaced")
    String orderPlaceConfirmation(Model model, Principal principal) {
        String username = principal.getName();
        Cart cart = cartService.getCartForUser(username);
        List<CartItem> cartItemList = cartItemService.getAllItems(cart);
        System.out.println(cartItemList);
        User user = userService.findByUsername(username);
        boolean exists = addressRepository.existsByUserId(user.getId());

        if (exists) {
            List<CartItem> cartItems = cartItemService.getAllItems(cart);
            double totalPrice = cart.calculateCartTotal();
            double discount = 0.0;
            double tax = 0.0;
            if(totalPrice != 0){

                discount = 60.0;
                tax = 80.0;
                totalPrice = totalPrice - (discount + tax);
            }
            List<Address> address = addressService.findAllUsersAddress(user.getId());
            if(principal.getName()!= null){
                if (cart != null){
                    model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                }
            }
            model.addAttribute("address",address);
            model.addAttribute("items", cartItems);
            model.addAttribute("cart", cart);
            model.addAttribute("total", totalPrice);
//            model.addAttribute("discount", discount);
            model.addAttribute("tax", tax);


            if (username != null) {
                if (cart != null) {
                    model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                }
            }


        }
        return "orderPlaced";
    }


    // view order by id








    // show order placed success

    @GetMapping("/successPage")
        String orderPlacedSuccessfully(){
            return "successPage";
        }
    }



