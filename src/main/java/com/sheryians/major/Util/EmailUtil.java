package com.sheryians.major.Util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class EmailUtil {

    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        SimpleMailMessage mimeMessage = new SimpleMailMessage();
        mimeMessage.setTo(email);
        mimeMessage.setSubject("Verify OTP");
        mimeMessage.setText("Your OTP is " + otp);
        javaMailSender.send(mimeMessage);
    }
}