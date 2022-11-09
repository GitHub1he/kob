package com.he.chatsystem;

import com.he.chatsystem.service.impl.ChatServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatSystemApplication {
    public static void main(String[] args) {
        ChatServiceImpl.chatPool.start();
        SpringApplication.run(ChatSystemApplication.class, args);
    }
}