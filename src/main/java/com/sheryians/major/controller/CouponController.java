package com.sheryians.major.controller;


import com.sheryians.major.domain.*;
import com.sheryians.major.repository.CouponRepository;
import com.sheryians.major.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CouponController {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    CouponService couponService;


    @GetMapping("/admin/coupon")
    public String showCoupon(Model model){
        List<Coupon> coupons = couponRepository.findAll();
        model.addAttribute("coupons",coupons);
        System.out.println(coupons);
        return "adminCoupon";
    }

    @GetMapping("/admin/addCoupon")
    public String addCouponPage(){
        return "adminAddCoupon";
    }


    @PostMapping ("/admin/addCoupon")
    public String addCouponByAdmin(@RequestParam("couponCode") String couponCode,
                                   @RequestParam("discount") int discount,
                                   @RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
                                   @RequestParam("maxAmount") int maxAmount,
                                   @RequestParam("couponStock") int couponStock,
                                   @RequestParam("cartAmount") Double cartAmount,
                                   RedirectAttributes redirectAttributes){
        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponCode);
        coupon.setDiscount(discount);
        coupon.setExpiryDate(expiryDate);
        coupon.setMaxAmount(maxAmount);
        coupon.setCouponStock(couponStock);
        coupon.setCartAmount(cartAmount);
        couponRepository.save(coupon);
        redirectAttributes.addFlashAttribute("couponAdded","!!The Coupon has been Added successfully!!!");
        return "redirect:/admin/coupon";
    }

    @GetMapping("/admin/deleteCoupon/{id}")
    public String deleteCoupon(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        Coupon coupon = couponRepository.findById(id).orElse(null);
        assert coupon != null;
        couponRepository.delete(coupon);
        redirectAttributes.addFlashAttribute("couponDeleted","!!The Coupon has been deleted successfully!!!");
        return "redirect:/admin/coupon";
    }



//    @PostMapping("/user/couponApply")
//    public String userApplyCoupon(@RequestParam("couponCode") String couponCode, Model model, Principal principal, RedirectAttributes redirectAttributes){
//
//        if (principal!=null){
//            User user = userService.getUserByEmail(principal.getName());
//            Cart cart = cartService.getCartForUser(principal.getName());
//            Coupon coupon = couponRepository.findByCouponCode(couponCode);
//            List<CartItem> cartItems = cartItemService.getAllItems(cart);
//            List<Address> address = addressService.findAllUsersAddress(user.getId());
//
//            for(Address defaultAddress : address){
//                if(defaultAddress.isDefault()){
//                    model.addAttribute("default",defaultAddress);
//                }else {
//                    if (defaultAddress.isDefault()){
//                        continue;
//                    }
//                    model.addAttribute("address", address);
//                }
//
//            }
//            double totalPrice = cart.calculateCartTotal();
//
//            model.addAttribute("items",cartItems);
//            model.addAttribute("cart",cart);
//            if(totalPrice >= coupon.getCartAmount()){
//                if(couponService.couponIsApplicable(coupon,totalPrice)){
//                    double discountAmount = (totalPrice * coupon.getDiscount()) / 100.0;
//                    System.out.println(discountAmount);
//                    if (discountAmount > coupon.getMaxAmount()) {
//                        discountAmount = coupon.getMaxAmount();
//
//                        double TotalPriceAfterDiscount = totalPrice - discountAmount;
//                        model.addAttribute("total","TotalPriceAfterDiscount");
////                        model.addAttribute("applied","reduced "+discountAmount+ " price.");
//                        System.out.println(TotalPriceAfterDiscount);
//                    }
//
//
//
//                    System.out.println(couponService.couponIsApplicable(coupon,totalPrice));
//                }
//                System.out.println(totalPrice >= coupon.getCartAmount());
//            }
//
//
//
//            if (cart != null){
//                List<CartItem> cartItemList =  cart.getCartItems();
//                model.addAttribute("cartCount", cartItemList.stream().map(x -> x.getQuantity()).reduce(0, Integer::sum));
//
//            }
//        }
//        return "orderPlaced";
//
//        }





    }


