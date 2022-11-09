package com.he.backend;

import com.he.backend.consumerchat.ChatWebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        ChatWebSocketServer.chatRecordPool.start();
        SpringApplication.run(BackendApplication.class, args);
    }

}
