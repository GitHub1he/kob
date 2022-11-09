package com.he.chatsystem.service.impl;

import com.he.chatsystem.service.ChatService;
import com.he.chatsystem.service.impl.utils.ChatPool;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    public final static ChatPool chatPool = new ChatPool();
    @Override
    public String sendChat(Integer senderId, Integer receiverId, String content) {
        System.out.println("charsystem sendChat" + senderId + "-> " + receiverId + ": " + content);
        chatPool.sendchat(senderId, receiverId, content);
        return "sendChat success";
    }
}
