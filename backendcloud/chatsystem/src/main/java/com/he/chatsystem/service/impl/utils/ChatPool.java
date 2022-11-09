package com.he.chatsystem.service.impl.utils;

public class ChatPool extends Thread{

    private static boolean isTeam(Integer receiverId) {
        //是个群
        return receiverId > 90000;
    }
    public void sendchat(Integer senderId, Integer receiverId, String content) {

    }

    @Override
    public void run() {
    }
}
