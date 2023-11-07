package com.sheryians.major.controller;

import com.sheryians.major.domain.*;
import com.sheryians.major.repository.*;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckoutController {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;
    @Autowired
    AddressService addressService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProductRepository productRepository;





    // show checkout page by passing values

    @GetMapping("/checkout")
    String checkoutPage(Model model, Principal principle) {
        User user = userService.findByUsername(principle.getName());
        boolean exists = addressRepository.existsByUserId(user.getId());
        Cart cart = cartService.getCartForUser(principle.getName());
        user.getCart();
        if (exists) {
            List<CartItem> cartItems = cartItemService.getAllItems(cart);
            double totalPrice = cart.calculateCartTotal();
            model.addAttribute("items", cartItems);
            model.addAttribute("cart", cart);
            model.addAttribute("total", totalPrice);
            if (principle.getName() != null) {
                if (cart != null) {
                    List<CartItem> cartItemList = cart.getCartItems();
                    model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                }
            }
            return "redirect:/orderPlaced";
        }
        if (principle.getName() != null) {
            if (cart != null) {
                List<CartItem> cartItemList = cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

            }
        }
        return "redirect:/newAddress";
    }


    // get/pass the page for inserting new address

    @GetMapping("/newAddress")
    public String addNewAddress(Model model, Principal principal) {
        model.addAttribute("address", new Address());
        Cart cart = cartService.getCartForUser(principal.getName());
        if (principal.getName() != null) {
            if (cart != null) {
                List<CartItem> cartItemList = cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));
            }
        }
        return "newAddress";
    }


    // update address

    @GetMapping("/updateAddress/{id}")
    public String updateAddress(@PathVariable("id") Long id,Model model, Principal principal) {
        model.addAttribute("address", addressService.findById(id));
        Cart cart = cartService.getCartForUser(principal.getName());
        if (principal.getName() != null) {
            if (cart != null) {
                List<CartItem> cartItemList = cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));
            }
        }
        return "newAddress";
    }




    // post/store new address to database

    @PostMapping("/newAddress")
    String checkoutPost(@Valid @ModelAttribute("address") Address address, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            // If there are validation errors, add them to the model
            model.addAttribute("errors", result.getAllErrors());
            return "newAddress";
        }
        Cart cart = cartService.getCartForUser(principal.getName());
        User user = userService.getUserByEmail(principal.getName());
        List<Address> addressList = addressService.findAllUsersAddress(user.getId());
        if(addressList!=null){
            address.setUser(user);
            address.setDefault(false);
            addressRepository.save(address);
            return "redirect:/showAddress";

        }
            address.setUser(user);
            address.setDefault(true);
            addressRepository.save(address);


        model.addAttribute("address",addressList);

        // define count
        if (principal.getName() != null) {
            if (cart != null) {
                List<CartItem> cartItemList = cart.getCartItems();
                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));
            }
        }
        return "redirect:/showAddress";
    }


    // create/store the order details in database( order placed )

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam Long selectedAddress, Principal principal, Model model, RedirectAttributes redidAttrs){
        Orders order = new Orders();
        User user = userService.getUserByEmail(principal.getName());
        Cart cart = cartService.getCartForUser(principal.getName());
        Address address = addressService.findById(selectedAddress);
        List<CartItem> cartItems = cartItemService.getAllItems(cart);
        CartItem cartItem = cartItems.get(0);
        double totalPrice = cart.calculateCartTotal();

        order.setOrderStatus(orderStatusRepository.findById(1L).get());

        order.setUser(user);
        order.setAmount((int) totalPrice);
        order.setLocalDate(LocalDate.now());
//        order.getOrderItems(cartItems);
        order.setAddress(address);
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemService.moveItemsFromCartToOrder( principal.getName(),order);
        System.out.println(orderItems);

        return "redirect:/";
    }


//    @GetMapping("/makePurchase/{id}")
//    public String makePurchase(@PathVariable("id") long id,Model model,RedirectAttributes redidAttrs){
//        CartItem cartItem =  cartItemRepository.findById(id).orElse(null);
//        if(cartItem!=null){
//            Product product = cartItem.getProduct();
//            if(cartItem.getQuantity()> product.getUnitsInStock()){
//                redidAttrs.addFlashAttribute("outOfStock","OOps!.. "+product.getName()+" has Only " + product.getUnitsInStock() + " piece left");
//                return "redirect:/cart";
//            }
//        }
//            return "redirect:/checkout";
//
//    }





}
