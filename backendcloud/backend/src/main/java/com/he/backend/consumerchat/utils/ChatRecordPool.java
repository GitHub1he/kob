package com.he.backend.consumerchat.utils;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumerchat.ChatWebSocketServer;
import com.he.backend.pojo.Bot;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class ChatRecordPool extends Thread {
    private static MultiValueMap<Integer, ChatRecord> chatRecords = new LinkedMultiValueMap<>();
    private ReentrantLock lock = new ReentrantLock();

    public void addChatRecord(Integer userId, Integer sendId, String content, Date sendtime) {
        lock.lock();
        try {
            chatRecords.add(userId, new ChatRecord(sendId, content, sendtime));
        } finally {
            lock.unlock();
        }
    }

    public void removeChatRecord(Integer userId) {
        lock.lock();
        try {
            chatRecords.remove(userId);
        } finally {
            lock.unlock();
        }
    }

    private boolean checkUserOnline(Integer userId) { //判断用户是否在线
        return ChatWebSocketServer.users.get(userId) != null ;
    }

    private void sendChatToUser() { //将信息发送到用户
        Set<Integer> hasNewsUserIds  = chatRecords.keySet();
        List<Integer> usedUsers = new ArrayList<>();
        for (Integer newsUserId : hasNewsUserIds) {
            if(checkUserOnline(newsUserId)) {
                JSONObject resp = new JSONObject();
                resp.put("event", "send-message");
                resp.put("chatrecords",chatRecords.get(newsUserId));
                ChatWebSocketServer.users.get(newsUserId).sendMessage(resp.toString());

                usedUsers.add(newsUserId);
            }
        }
        for (Integer usedUser : usedUsers) {
            ChatWebSocketServer.chatRecordPool.removeChatRecord(usedUser);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    sendChatToUser();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
