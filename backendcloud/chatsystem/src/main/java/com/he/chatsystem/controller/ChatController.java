package com.he.chatsystem.controller;

import com.he.chatsystem.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/user/chat/send/")
    public String sendChat(@RequestParam MultiValueMap<String, String> data) {
        Integer senderId = Integer.parseInt(Objects.requireNonNull(data.getFirst("sender_id")));
        Integer receiverId = Integer.parseInt(Objects.requireNonNull(data.getFirst("receiver_id")));
        String content = data.getFirst("content");
        return chatService.sendChat(senderId, receiverId, content);
    }
}
