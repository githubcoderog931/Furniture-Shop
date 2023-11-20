package com.sheryians.major.controller;

import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.domain.*;
import com.sheryians.major.repository.*;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    // define autowire

    @Autowired
    private OrderRepository orderRepository;

//    @Autowired
//    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentRepository paymentRepository;

    private final Environment env;

    public OrderController(Environment env) {
        this.env = env;
    }












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
//            double discount = 0.0;
//            double tax = 0.0;
//            if (totalPrice != 0) {
//
//                discount = 60.0;
//                tax = 80.0;
//                totalPrice = totalPrice - (discount + tax);
//            }
            List<Address> address = addressService.findAllUsersAddress(user.getId());
            if (principal.getName() != null) {
                if (cart != null) {
                    model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                }
            }
            for(Address defaultAddress : address){
                if(defaultAddress.isDefault()){
                    model.addAttribute("default",defaultAddress);
                }else {
                    if (defaultAddress.isDefault()){
                        continue;
                    }
                    model.addAttribute("address", address);
                }

            }
            model.addAttribute("items", cartItems);
            model.addAttribute("cart", cart);
            model.addAttribute("total", totalPrice);
//            model.addAttribute("discount", discount);
//            model.addAttribute("tax", tax);
//            model.addAttribute("rzp_key_id", env.getProperty("rzp_key_id"));
//            model.addAttribute("rzp_currency", env.getProperty("rzp_currency"));
//            model.addAttribute("rzp_company_name", env.getProperty("rzp_company_name"));


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
    String orderPlacedSuccessfully() {
        return "successPage";
    }



    // return order handle

    @GetMapping("/returnProduct/{id}")
    String returnOrders(@PathVariable("id") long id,Model model) {
        Orders orders = orderRepository.findById(id).orElse(null);
        if (orders != null) {
            // Assuming you have an OrderStatusRepository to fetch the 'RETURNED' status
            OrderStatus returnedStatus = OrderStatus.RETURNED;

            orders.setOrderStatus(returnedStatus);
            orderRepository.save(orders);
        }
        return "redirect:/orderHistory";
    }


    @GetMapping("/confirm/{id}")
    String confirmOrders(@PathVariable("id") long id, Model model) {
        Orders orders = orderRepository.findById(id).orElse(null);
        if (orders != null) {
            // Assuming you have an OrderStatusRepository to fetch the 'ORDER' status
            OrderStatus confirmedStatus = OrderStatus.ORDER;

            orders.setOrderStatus(confirmedStatus);
            orderRepository.save(orders);
        }
        return "redirect:/orderHistory";

    }




//    @GetMapping("/orderDetails/{id}")
//    public String getOrderDetails(@PathVariable("id") long id, Model model) {
//        Orders orders = orderRepository.findById(id).orElse(null);
//        if (orders != null) {
//            model.addAttribute("orderStatus", orders.getOrderStatus().getStatus());
//        }
//        return "orderDetailsPage";
//    }

}



