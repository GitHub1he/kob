package com.he.chatsystem.service;

public interface ChatService {
    String sendChat(Integer senderId, Integer receiverId, String content);
}
