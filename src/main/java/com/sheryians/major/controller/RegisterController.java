package com.sheryians.major.controller;


import com.sheryians.major.Util.EmailUtil;
import com.sheryians.major.Util.OtpUtil;
import com.sheryians.major.domain.Role;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.RoleRepository;
import com.sheryians.major.repository.UserRepository;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class RegisterController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    OtpUtil otpUtil;

    @GetMapping("/register")
    public String registerGet(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(false);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    //    @PostMapping("/register")
//    public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request) throws ServletException {
//
//
//        String password = user.getPassword();
//        user.setPassword(bCryptPasswordEncoder.encode(password));
//        List<Role> roles = new ArrayList<>();
//        roles.add(roleRepository.findById(2).get());
//        user.setRoles(roles);
//        user.setEnable(true);
//        userRepository.save(user);
//        request.login(user.getEmail(),password);
//        return "redirect:/login";
//    }


    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") User user, Model model) {


        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).get());
        user.setRoles(roles);
        user.setEnable(true);
        User existing = userService.getUserByEmail(user.getEmail());
        if (existing!=null){
            if (existing.isVerified()) {
                model.addAttribute("registrationError", "You already exists.");
                return "register";
            } else {
                userService.removeUserById(existing.getId());
            }
        }
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "sendOtpRegistered";








//        return "redirect:/login";
//        model.addAttribute("email", user.getEmail());

    }
    @PostMapping("/verifyOtp")
    public String verify(@ModelAttribute("email") String email, @ModelAttribute("otp") String otp,
                         Model model){

        User user = userService.getUserByEmail(email);
        System.out.println(email);
        System.out.println(otp);
        String savedOtp = user.getOtp();
        if (savedOtp.equals(otp)) {
            user.setVerified(true);
            userService.saveUser(user);
            return "verified";
        } else {
            model.addAttribute("email", user.getEmail());
            model.addAttribute("wrong", "Wrong OTP Pin.");
            return "sendOtpRegistered";
        }
    }


}