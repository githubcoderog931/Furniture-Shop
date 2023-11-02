//package com.sheryians.major.service;
//
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//@Service
//
//public class EmailService {
//    private final JavaMailSender javaMailSender;
//
//    public EmailService(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setSubject("Your OTP Code");
//        helper.setTo(toEmail);
//        helper.setText("Your OTP code is: " + otp);
//
//        javaMailSender.send(message);
//    }
//}
