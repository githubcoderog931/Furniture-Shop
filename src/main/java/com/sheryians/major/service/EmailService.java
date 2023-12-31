package com.sheryians.major.service;

import com.sheryians.major.domain.Chat;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    UserService userService;

    @Autowired
    ChatRepository chatRepository;

    public void sendEmail(String to, String subject, String body, String senderId, String receiverId) {
        SimpleMailMessage message = new SimpleMailMessage();
        User senderUser = userService.findByUsername(senderId);
        User receiverUser = userService.findByUsername(receiverId);
        Chat chat = new Chat();
        chat.setChatContent(body);
        chat.setSenderId(senderUser.getId());
        chat.setReceiverId(receiverUser.getId());
        chatRepository.save(chat);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);


    }


}
