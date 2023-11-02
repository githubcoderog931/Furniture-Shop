package com.sheryians.major.controller;

import com.sheryians.major.domain.Cart;
//import com.sheryians.major.domain.Otp;
import com.sheryians.major.domain.Role;
import com.sheryians.major.domain.User;

//import com.sheryians.major.repository.OtpRepository;
//import com.sheryians.major.service.EmailService;
//import com.sheryians.major.service.OtpService;
import com.sheryians.major.repository.CartRepository;
import com.sheryians.major.repository.RoleRepository;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;






    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CartRepository cartRepository;

//    @Autowired
//    OtpService otpService;
//    @Autowired
//    EmailService emailService;

//    @Autowired
//    OtpRepository otpRepository;





    @GetMapping("/login")
    public String login() {
        return "login";
    }




//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User()); // Initialize a new User object
//        return "register"; // Return the view name
//    }






}
