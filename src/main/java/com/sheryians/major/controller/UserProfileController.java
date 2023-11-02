package com.sheryians.major.controller;

import com.sheryians.major.domain.*;
import com.sheryians.major.repository.OrderItemRepository;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
//@RequestMapping("userProfile")
public class UserProfileController {

    // define autowired
    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserProfileService userProfileService;



    // get all address of user
    @GetMapping("/showAddress")
    public String userDetails(Model model, Principal principal){
        User user = userService.getUserByEmail(principal.getName());
        List<Address> addressList = addressService.findAllUsersAddress(user.getId());
        if(principal.getName()!= null){
            Cart cart = cartService.getCartForUser(principal.getName());
            if (cart != null){
                List<CartItem> cartItemList =  cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }
        model.addAttribute("address",addressList);
        return "user/showAddress";
    }

//    @GetMapping("/orderHistory")
//    public String orderHistory(Model model, Principal principal){
//        User user = userService.getUserByEmail(principal.getName());
//        List<Orders> orders = orderRepository.findByUser(user);
////        List<OrderItem> orderItemList = orderItemRepository.findByOrders(order);
//        if(principal.getName()!= null){
//            Cart cart = cartService.getCartForUser(principal.getName());
//            if (cart != null){
//                List<CartItem> cartItemList =  cart.getCartItems();
//                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));
//
//            }
//        }
//        model.addAttribute("orders",orders);
//        return "user/orderHistory";
//
//    }

    @GetMapping("/orderHistory")
    public String getAllOrders(Model model,Principal principal) {
        List<Orders> orders = null;
        if (principal.getName() != null) {
            orders = orderService.getAllOrders(userService.findByUsername(principal.getName()));
        }
        model.addAttribute("orders", orders);
        User user = userService.findByUsername(principal.getName());
        User user1 = userService.getUserByEmail(principal.getName());
        System.out.println(user1);
        System.out.println(user);
        System.out.println(orders);
        return "user/orderHistory";
    }



    // delete order controller
//
//    @GetMapping("/deleteOrder/{id}")
//    public String deleteOrder(@PathVariable Long id){
//        orderService.deleteOrder(id);
//        return "redirect:/orderHistory";
//    }


    // delete address controller

    @GetMapping("/deleteAddress/{id}")
    public String deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return "redirect:/showAddress";
    }


    // edit profile show page

    @GetMapping("/editProfile")
    public String editProfile(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user",user);
        return "user/editProfile";
    }

    // edit profile store data

    @PostMapping("/editProfile")
    public String editProfile(@RequestParam("firstname") String firstname,
                              @RequestParam("lastname") String lastname,
                              Principal principal,
                              Model model){
        System.out.println(firstname);
        System.out.println(lastname);

        userProfileService.editProfile(principal.getName(),firstname,lastname);
        return "redirect:/user";
    }


    // update Address



}
