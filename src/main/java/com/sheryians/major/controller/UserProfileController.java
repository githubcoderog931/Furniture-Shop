package com.sheryians.major.controller;

import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.domain.*;
import com.sheryians.major.repository.*;
//import com.sheryians.major.repository.OrderStatusRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Map;
import java.util.Objects;

@Controller
//@RequestMapping("userProfile")
public class UserProfileController {

    // define autowired

    @Autowired
    ReferralRepository referralRepository;
    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;

    @Autowired
    ChatService chatService;

//    @Autowired
//    OrderStatusRepository orderStatusRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ReferralService referralService;



    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartService cartService;

    @Autowired
    WalletService walletService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    // get all address of user
    @GetMapping("/showAddress")
    public String userDetails(Model model, Principal principal){
        if(principal!=null){
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
        return "redirect:/login";
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
        if(principal!=null){
            List<Orders> orders = null;
            if (principal.getName() != null) {
                orders = orderService.getAllOrders(userService.findByUsername(principal.getName()));
            }
            model.addAttribute("orders", orders);
            return "user/orderHistory";
        }
        return "redirect:/login";
    }



    // cancel order handle

    @GetMapping("/cancelOrder/{id}")
    String cancelOrder(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        if(principal!=null){
            Orders orders = orderRepository.findById(id).orElse(null);
            List<OrderItem> orderItems = orderItemRepository.findByOrders(orders);
            User user = userService.getUserByEmail(principal.getName());
            assert orders != null;
            orders.setOrderStatus(OrderStatus.CANCELLED);
//        String cancelStatus = orders.getOrderStatus().getStatus();
            Wallet wallet = user.getWallet();
            Double previousAmount = wallet.getWalletAmount();
            wallet.setWalletAmount(orders.getAmount()+previousAmount);
            for(OrderItem items : orderItems){
                if (orders.getOrderStatus()==OrderStatus.CANCELLED){
                    int quantity = items.getQuantity();
                    long stock = items.getProduct().getUnitsInStock();
                    items.getProduct().setUnitsInStock(quantity+stock);
                }
            }
            redirectAttributes.addFlashAttribute("cancelOrder","Order cancelled successfully");
            orderRepository.save(orders);
            return "redirect:/orderHistory";
        }
        return "redirect:/login";
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
        if(principal!=null){
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("user",user);
            return "user/editProfile";
        }
        return "redirect:/login";
    }

    // edit profile store data

    @PostMapping("/editProfile")
    public String editProfile(@RequestParam("firstname") String firstname,
                              @RequestParam("lastname") String lastname,
                              Principal principal,
                              Model model,RedirectAttributes redirectAttributes){
        if(principal!=null){
            redirectAttributes.addFlashAttribute("profileEdited","profile details was changed successfully");

            userProfileService.editProfile(principal.getName(),firstname,lastname);
            return "redirect:/user";
        }
        return "redirect:/login";
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
        if(principal!=null){
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
        return "redirect:/login";
    }



    // set default addreess

    @GetMapping("/setDefault/{id}")
    public String setAsDefault(@PathVariable("id") Long id,Principal principal){
       if (principal!=null){
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
       return "redirect:/login";
    }


    // show wallet page

    @GetMapping("/userWallet")
    public String userWallet(){
        return "user/wallet";
    }




    // chat implementation

    @GetMapping("/chat")
    public String chat(Model model,Principal principal){
        if(principal!=null){
            List<Chat> chats = chatRepository.findAll();
            List<Chat> send = new ArrayList<>();
            List<Chat> received = new ArrayList<>();
            List<String> names = new ArrayList<>();

            User user = userService.getUserByEmail(principal.getName());
            Referral referral = referralRepository.findByReferrer(user);

            for(Chat chat : chats) {
                if (Objects.equals(user.getId(), chat.getSenderId())) {
                    Chat chatMessage = new Chat();
                    chatMessage.setChatContent(chat.getChatContent());
                    chatMessage.setReceiverId(chat.getReceiverId());
                    chatMessage.setToWhome(Objects.requireNonNull(userService.findById(chat.getReceiverId()).orElse(null)).getEmail());
                    send.add(chatMessage);
                }
                if (Objects.equals(user.getId(), chat.getReceiverId())) {
                    Chat chatMessage = new Chat();
                    chatMessage.setChatContent(chat.getChatContent());
                    chatMessage.setSenderId(chat.getSenderId());
                    chatMessage.setToWhome(Objects.requireNonNull(userService.findById(chat.getSenderId()).orElse(null)).getEmail());
                    received.add(chatMessage);

                }
                model.addAttribute("replay", names);
                model.addAttribute("send", send);
                model.addAttribute("received", received);
                model.addAttribute("referral", referral.getReferralCode());
            }
            return "user/chat";
        }
        return "redirect:/login";
    }

    @PostMapping("/chat/form")
    public String sendChat(@RequestParam("chatContent") String chatContent,
                           @RequestParam("receiverEmail") String receiverEmail,
                           RedirectAttributes redirectAttributes,
                           Principal principal){
        if (principal!=null){
            String senderEmail = principal.getName();
            String subject = "message from "+senderEmail;
            emailService.sendEmail(receiverEmail, subject, chatContent,senderEmail, receiverEmail);
            System.out.println(chatContent);
            redirectAttributes.addFlashAttribute("chatContent",chatContent);
            redirectAttributes.addFlashAttribute("toWho",receiverEmail);
            return "redirect:/chat";
        }
        return "redirect:/login";
    }



}
