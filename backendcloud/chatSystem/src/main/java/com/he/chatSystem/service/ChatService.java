package com.he.chatSystem.service;

public interface ChatService {
    String addChater(Integer userId) ;
    String removeChater(Integer userId);
    String sendChat(Integer sender_id, Integer receiver_id, String content);
    String acceptChat();
}
