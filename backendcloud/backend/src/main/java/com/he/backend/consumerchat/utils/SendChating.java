package com.he.backend.consumerchat.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.consumerchat.ChatWebSocketServer;
import com.he.backend.pojo.Chat;
import com.he.backend.pojo.TeamsDetail;
import com.he.backend.pojo.User;

import java.util.Date;
import java.util.List;

public class SendChating extends Thread{
    private final Integer sender_id;
    private final Integer receiver_id;
    private final String content;

    public SendChating(Integer sender_id, Integer receiver_id, String content) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
    }

    private boolean isTeam() {
        //是个群
        return this.receiver_id > 90000;
    }
    private void saveToDatabase() {
        Date now = new Date();
        Chat chat = new Chat(null, sender_id, receiver_id, content, now);
        ChatWebSocketServer.chatMapper.insert(chat);
    }
    public void sendChat() {
        Date now = new Date();

        if(receiver_id == 0){ //向公屏发送 -> 所有在线的都可以收到
            List<User> allUsers = ChatWebSocketServer.userMapper.selectList(null);
            for (User user : allUsers) {
                if(ChatWebSocketServer.users.get(user.getId()) != null)
                    ChatWebSocketServer.chatRecordPool.addChatRecord(user.getId(), sender_id, content, now);
            }
        }else if(isTeam()) {
            QueryWrapper<TeamsDetail> teamsDetailQueryWrapper = new QueryWrapper<>();
            teamsDetailQueryWrapper.eq("team_id", receiver_id);
            List<TeamsDetail> teamUsersHasNews = ChatWebSocketServer.teamDetailMapper.selectList(teamsDetailQueryWrapper);
            for (TeamsDetail teamsuser : teamUsersHasNews) {
                ChatWebSocketServer.chatRecordPool.addChatRecord(teamsuser.getUserId(), sender_id, content, now);
            }
        }else {
            ChatWebSocketServer.chatRecordPool.addChatRecord(receiver_id, sender_id, content, now);
        }
        saveToDatabase();
    }

    @Override
    public void run() {
        sendChat();
    }
}
