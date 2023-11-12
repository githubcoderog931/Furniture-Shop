package com.sheryians.major.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sheryians.major.domain.*;

import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.repository.OrderStatusRepository;
import com.sheryians.major.repository.PaymentRepository;
import com.sheryians.major.service.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class RazorController {

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

    public RazorController(Environment env) {
        this.env = env;
    }

    @RequestMapping(value = {"/payment"}, method = RequestMethod.GET)
    public String payment(Model model){
        model.addAttribute("rzp_key_id", env.getProperty("rzp_key_id"));
        model.addAttribute("rzp_currency", env.getProperty("rzp_currency"));
        model.addAttribute("rzp_company_name", env.getProperty("rzp_company_name"));
        return "payment";
    }
    @GetMapping("/payment/createOrderId/{amount}")
    @ResponseBody
    public String createPaymentOrderId(@PathVariable String amount) {
        String orderId=null;
        try {
            RazorpayClient razorpay = new RazorpayClient(env.getProperty("rzp_key_id"), env.getProperty("rzp_key_secret"));
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount); // amount in the smallest currency unit
            orderRequest.put("currency", env.getProperty("rzp_currency"));
            orderRequest.put("receipt", "order_rcptid_11");

            Order order = razorpay.orders.create(orderRequest);
            orderId = order.get("id");

        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        }
        return orderId;
    }

    @RequestMapping(value = {"/payment/succes/{amount}/{companyName}"}, method = RequestMethod.POST)
    public String paymentSuccess(Model model,
                                 Authentication authentication,
                                 @RequestParam("razorpay_payment_id") String razorpayPaymentId,
                                 @RequestParam("razorpay_order_id") String razorpayOrderId,
                                 @RequestParam("razorpay_signature") String razorpaySignature,
                                 @PathVariable Float amount,
//                                 @PathVariable Integer contactCount,
                                 @PathVariable String companyName,
//                                 @PathVariable String currency,
//                                 @PathVariable String description,
                                 RedirectAttributes redirectAttributes
    ){
        Payments payment = new Payments();
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setAmount(amount);
        paymentRepository.save(payment);
        System.out.println("Save all data, which on success we get!");
        return "redirect:/";
    }


}

