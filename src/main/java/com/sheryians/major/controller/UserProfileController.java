package com.sheryians.major.controller;

import com.sheryians.major.domain.*;
import com.sheryians.major.repository.AddressRepository;
import com.sheryians.major.repository.OrderStatusRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.sheryians.major.repository.OrderItemRepository;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
//@RequestMapping("userProfile")
public class UserProfileController {

    // define autowired
    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;



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
        return "user/orderHistory";
    }



    // cancel order handle

    @GetMapping("/cancelOrder/{id}")
    String cancelOrder(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Orders orders = orderRepository.findById(id).orElse(null);
        List<OrderItem> orderItems = orderItemRepository.findByOrders(orders);
        assert orders != null;
        orders.setOrderStatus(orderStatusRepository.findById(5L).get());
        String cancelStatus = orders.getOrderStatus().getStatus();
        for(OrderItem items : orderItems){
            if (Objects.equals(orders.getOrderStatus().getStatus(), cancelStatus)){
                int quantity = items.getQuantity();
                long stock = items.getProduct().getUnitsInStock();
                items.getProduct().setUnitsInStock(quantity+stock);
            }
        }
        redirectAttributes.addFlashAttribute("cancelOrder","Order cancelled successfully");
        orderRepository.save(orders);
        return "redirect:/orderHistory";
    }




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
                              Model model,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("profileEdited","profile details was changed successfully");

        userProfileService.editProfile(principal.getName(),firstname,lastname);
        return "redirect:/user";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model, Principal principal) {
        return "changePassword";
    }


    // update Address


    // new Password

    @GetMapping("/setNewPassword")
    public String setNewPassword(){
        return "user/enterNewPassword";
    }

    @PostMapping("/setNewPassword")
    public String updatePassword(@RequestParam("confirmPassword") String confirmPassword, @RequestParam("password") String enteredPassword,@RequestParam("currentPassword") String currentPassword, Principal principal, RedirectAttributes redirectAttributes){
        User user = userService.getUserByEmail(principal.getName());
        if (Objects.equals(enteredPassword, confirmPassword)){
            if(bCryptPasswordEncoder.matches(currentPassword,user.getPassword())){
                user.setPassword(bCryptPasswordEncoder.encode(enteredPassword));
                userService.save(user);
            }else {
                redirectAttributes.addFlashAttribute("currentPasswordFail","the password you entered as current one is wrong!!");
                return "redirect:/setNewPassword";
            }
        }else {
            redirectAttributes.addFlashAttribute("confirmFailed","failed to confirm your new password!! check your password");
            return "redirect:/setNewPassword";
        }
        redirectAttributes.addFlashAttribute("passwordChanged","password changed successfully");
        return "redirect:/user";
    }



    // set default addreess

    @GetMapping("/setDefault/{id}")
    public String setAsDefault(@PathVariable("id") Long id,Principal principal){
        User user = userService.getUserByEmail(principal.getName());
        List<Address> addressList = addressService.getAllAddress(user);
        for(Address address: addressList){
            address.setDefault(false);
        }
        Address address = addressService.findById(id);
        address.setDefault(true);
        addressRepository.save(address);

        return "redirect:/showAddress";
    }

}
