package com.sheryians.major.controller;

import com.sheryians.major.domain.*;
import com.sheryians.major.repository.*;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    AddressRepository addressRepository;

//    @Autowired
//    OrderStatusRepository orderStatusRepository;
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

    private final Environment env;


    public CheckoutController(Environment env) {
        this.env = env;
    }


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
            model.addAttribute("rzp_key_id", env.getProperty("rzp_key_id"));
            model.addAttribute("rzp_currency", env.getProperty("rzp_currency"));
            model.addAttribute("rzp_company_name", env.getProperty("rzp_company_name"));
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
        if(!addressList.isEmpty()){
            address.setUser(user);
            address.setDefault(false);
            addressRepository.save(address);
            return "redirect:/showAddress";

        }else{
            address.setUser(user);
            address.setDefault(true);
            addressRepository.save(address);
            model.addAttribute("address",addressList);
        }




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
