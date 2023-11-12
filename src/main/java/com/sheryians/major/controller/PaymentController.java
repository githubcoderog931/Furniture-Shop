package com.sheryians.major.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sheryians.major.domain.*;
import com.sheryians.major.repository.*;
import com.sheryians.major.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    CartService cartService;

    @Autowired
    AddressService addressService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    UserService userService;

    private final Environment env;


    public PaymentController(Environment env) {
        this.env = env;
    }

    @Autowired
    CouponService couponService;
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
     PaymentMethodRepository paymentMethodRepository;

    double TotalPriceAfterDiscount=0;
    double totalPrice =0;

    @PostMapping("/user/couponApply")
    public String userApplyCoupon( @RequestParam("couponCode") String couponCode, Model model, Principal principal, RedirectAttributes redirectAttributes){



            if (principal != null) {
                User user = userService.getUserByEmail(principal.getName());
                Cart cart = cartService.getCartForUser(principal.getName());

                List<CartItem> cartItems = cartItemService.getAllItems(cart);
                List<Address> address = addressService.findAllUsersAddress(user.getId());


                double totalPrice = cart.calculateCartTotal();

                Coupon coupon = couponRepository.findByCouponCode(couponCode);

                if (coupon != null) {
                    if (totalPrice >= coupon.getCartAmount()) {
                        if (couponService.couponIsApplicable(coupon, totalPrice)) {
                            double discountAmount = (totalPrice * coupon.getDiscount()) / 100.0;
                            if (discountAmount > coupon.getMaxAmount()) {
                                discountAmount = coupon.getMaxAmount();
                                TotalPriceAfterDiscount = totalPrice - discountAmount;
                                model.addAttribute("total", TotalPriceAfterDiscount);
                            }
                            TotalPriceAfterDiscount = totalPrice - discountAmount;
                            model.addAttribute("total", TotalPriceAfterDiscount);
                        }
                    }

                    for (Address defaultAddress : address) {
                        if (defaultAddress.isDefault()) {
                            model.addAttribute("default", defaultAddress);
                        } else {
                            if (defaultAddress.isDefault()) {
                                continue;
                            }
                            model.addAttribute("address", address);
                        }

                    }

                    if (cart != null) {
                        List<CartItem> cartItemList = cart.getCartItems();
                        model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));

                    }

                    model.addAttribute("items", cartItems);
                    model.addAttribute("cart", cart);
                    model.addAttribute("goodCoupon","!!!Coupon Applied successfully.");
                    return "orderPlaced";
                }else {
                    redirectAttributes.addFlashAttribute("badCoupon","!!!Error processing the coupon check the coupon");
                    return "redirect:/orderPlaced";
                }
            }
            return "/login";

    }



    @GetMapping("/payWallet")
    public String payUsingWallet(@RequestParam Long selectedAddress, Principal principal, Model model, RedirectAttributes redidAttrs) {
        User user = userService.getUserByEmail(principal.getName());
        Cart cart = cartService.getCartForUser(principal.getName());
        Address address = addressService.findById(selectedAddress);
        Wallet wallet = user.getWallet();


        if(TotalPriceAfterDiscount!=0){
            totalPrice = TotalPriceAfterDiscount;
            System.out.println(totalPrice);
        }else{
            totalPrice = cart.calculateCartTotal();
            System.out.println(totalPrice);
        }

        if (wallet.getWalletAmount() > totalPrice) {
            double currentWalletAmt = wallet.getWalletAmount();
            wallet.setWalletAmount(currentWalletAmt - totalPrice);
            walletRepository.save(wallet);
            Orders order = new Orders();
            order.setOrderStatus(orderStatusRepository.findById(1L).get());
            order.setUser(user);
            order.setAmount((int) totalPrice);
            order.setLocalDate(LocalDate.now());
//        order.getOrderItems(cartItems);
            order.setAddress(address);
            order.setPaymentMethod(paymentMethodRepository.findById(3L).orElse(null));
            orderRepository.save(order);
            System.out.println("ngeeeeeeeeeeeeee" + wallet.getWalletAmount() + "" + totalPrice);
            return "redirect:/";
        } else {
            System.out.println("your wallet doesn't have enough money");
        }
        redidAttrs.addFlashAttribute("noMoney","!!!You dont have enough money in your wallet");
        return "redirect:/orderPlaced";
    }


    @PostMapping("/user/submitOrder")
    public String submitOrder(@RequestParam Long selectedAddress, Principal principal, Model model, RedirectAttributes redidAttrs){
        User user = userService.getUserByEmail(principal.getName());
        Cart cart = cartService.getCartForUser(principal.getName());
        Address address = addressService.findById(selectedAddress);
        List<CartItem> cartItems = cartItemService.getAllItems(cart);
        CartItem cartItem = cartItems.get(0);

        if(TotalPriceAfterDiscount!=0){
            totalPrice = TotalPriceAfterDiscount;
            System.out.println(totalPrice);
        }else{
            totalPrice = cart.calculateCartTotal();
            System.out.println(totalPrice);
        }

        Orders order = new Orders();
        order.setOrderStatus(orderStatusRepository.findById(1L).get());
        order.setUser(user);
        order.setAmount((int) totalPrice);
        order.setLocalDate(LocalDate.now());
//        order.getOrderItems(cartItems);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethodRepository.findById(2L).orElse(null));
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemService.moveItemsFromCartToOrder( principal.getName(),order);
        System.out.println(orderItems);

        return "redirect:/";
    }




    @PostMapping("/payment/createOrder")
    @ResponseBody
    public String createOrder(@RequestParam("amount") Integer amount,
            @RequestParam("selectedAddressId") Long selectedAddressId,
            HttpSession session) {
        String orderId=null;
        session.setAttribute("selectedAddressId", selectedAddressId);

        try {
            Integer totalAmount = amount * 100;

            RazorpayClient razorpayClient = new RazorpayClient(env.getProperty("rzp_key_id"), env.getProperty("rzp_key_secret"));

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", totalAmount);
            orderRequest.put("currency", env.getProperty("rzp_currency"));
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("payment_capture", 1); // Auto-capture payment

            Order order = razorpayClient.orders.create(orderRequest);
            orderId = order.get("id");



        } catch (RazorpayException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return orderId;
    }

    @PostMapping("/payment/success/")
    public String paymentSuccess(Principal principal, HttpSession session) {
        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");
        Orders order = new Orders();
        User user = userService.getUserByEmail(principal.getName());
        Cart cart = cartService.getCartForUser(principal.getName());
        Address address = addressService.findById(selectedAddressId);
        List<CartItem> cartItems = cartItemService.getAllItems(cart);
        CartItem cartItem = cartItems.get(0);
        double totalPrice = cart.calculateCartTotal();

        order.setOrderStatus(orderStatusRepository.findById(1L).get());

        order.setUser(user);
        order.setAmount((int) totalPrice);
        order.setLocalDate(LocalDate.now());
//        order.getOrderItems(cartItems);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethodRepository.findById(1L).orElse(null));
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemService.moveItemsFromCartToOrder( principal.getName(),order);
        System.out.println(orderItems);
        return "redirect:/";
    }

}
