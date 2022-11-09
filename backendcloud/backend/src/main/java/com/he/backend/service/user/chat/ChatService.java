package com.he.backend.service.user.chat;

public interface ChatService {
    String sendChat(Integer senderId, Integer receiverId, String content);
    String delChatHistory(Integer senderId, Integer receiverId);
}
