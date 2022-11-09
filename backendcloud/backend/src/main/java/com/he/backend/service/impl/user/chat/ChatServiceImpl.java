package com.he.backend.service.impl.user.chat;

import com.he.backend.consumerchat.ChatWebSocketServer;
import com.he.backend.service.user.chat.ChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    @Override
    public String sendChat(Integer senderId, Integer receiverId, String content) {
        System.out.println(senderId + " -> " + receiverId + " : " + content);
        ChatWebSocketServer.sendChat(senderId, receiverId, content);
        return "send Chat Success!";
    }

    @Override
    public String delChatHistory(Integer senderId, Integer receiverId) {
        return "delChatHistory Success!";
    }
}
