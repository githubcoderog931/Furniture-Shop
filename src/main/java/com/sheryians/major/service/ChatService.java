package com.sheryians.major.service;

import com.sheryians.major.domain.Chat;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.ChatRepository;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    UserService userService;

    @Autowired
    ChatRepository chatRepository;

    public void sendChat(String chatContent, String senderId, String receiverId){
        User senderUser = userService.findByUsername(senderId);
        User receiverUser = userService.findByUsername(receiverId);
        Chat chat = new Chat();
        chat.setChatContent(chatContent);
        chat.setSenderId(senderUser.getId());
        chat.setReceiverId(receiverUser.getId());
        chatRepository.save(chat);
    }
}
