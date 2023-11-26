//package com.sheryians.major.controller;
//
//import com.sheryians.major.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class EmailController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @GetMapping("/send-email-form")
//    public String showEmailForm() {
//        return "send-email-form";
//    }
//
//    @PostMapping("/send-email")
//    public String sendEmail(
//            @RequestParam("senderEmail") String senderEmail,
//            @RequestParam("receiverEmail") String receiverEmail,
//            @RequestParam("subject") String subject,
//            @RequestParam("body") String body, Model model) {
//
//        // Send email
//        emailService.sendEmail(receiverEmail, subject, body);
//
//        // Add a success message to be displayed in the template
//        model.addAttribute("successMessage", "Email sent successfully");
//
//        // Return to the form page
//        return "send-email-form";
//    }
//}
