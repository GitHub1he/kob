package com.he.backend.consumerchat.utils;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.consumerchat.ChatWebSocketServer;
import com.he.backend.pojo.Chat;
import com.he.backend.pojo.TeamsDetail;
import com.he.backend.pojo.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Chating{
    private final Integer id;
    private final String username;
    private final String photo;
    private final Date last_login_time;

    public Chating(Integer id) {
        this.id = id;
        User user = ChatWebSocketServer.chatuserMapper.selectById(id);
        if(user != null){
            this.username = user.getUsername();
            this.photo = user.getPhoto();
            this.last_login_time = user.getLastLoginTime();
        }else {
            this.username = "";
            this.photo = "";
            this.last_login_time = null;
        }
    }

    private boolean isTeam(Integer receiver_id) {
        //是个群
        return receiver_id > 90000;
    }
    private JSONObject senderMessage(String content) {
        Date now = new Date();
        JSONObject isSendItem = new JSONObject();
        isSendItem.put("id", this.id);
        isSendItem.put("name", this.username);
        isSendItem.put("photo", this.photo);
        isSendItem.put("last_login_time", this.last_login_time);
        isSendItem.put("content", new ChatContent(null,false, content, 0, now));
        return isSendItem;
    }
    private void saveToDatabase(Integer receiver_id, String content) {
        Date now = new Date();
        Chat chat = new Chat(null, id, receiver_id, content,null, now,now);
        ChatWebSocketServer.chatMapper.insert(chat);
    }

    /*
     * 在发送群消息时调用此方法
     *
     * 发送群消息
     *   1. 查询组中成员，遍历用户
     *   2. 将发送的消息存到数据库中
     *   3. 如果用户在线则直接发送到client
     * */
    private void sendTeamChat(Integer team_id, String content) {

        QueryWrapper<TeamsDetail> teamsDetailQueryWrapper = new QueryWrapper<>();
        teamsDetailQueryWrapper.eq("team_id", team_id);
        List<TeamsDetail> teamUsers = ChatWebSocketServer.teamDetailMapper.selectList(teamsDetailQueryWrapper); //组员
        for (TeamsDetail teamsuser : teamUsers) {
            User user = ChatWebSocketServer.chatuserMapper.selectById(teamsuser.getUserId());
            if(ChatWebSocketServer.users.get(teamsuser.getUserId()) != null) {
                JSONObject res = new JSONObject();
                res.put("event", "receive_chat");
                res.put("user_id", user.getId());
                res.put("is_receiver", senderMessage(content));

                ChatWebSocketServer.users.get(user.getId()).sendMessage(res.toString());
            }
        }
    }

    /*
     * 在接收历史消息处增加
     *   1. 查询用户所在组
     *   2. 使用List<JSONObject>.add接收所在组的聊天记录
     *
     * 在本方法中增加：
     *   1. 查询组的聊天记录
     *   2. 返回本群记录
     * */
    private List<JSONObject> receiveTeamChat(Integer user_id) {
        List<JSONObject> res = new ArrayList<>();
        QueryWrapper<TeamsDetail> userInTeamQW = new QueryWrapper<>();
        userInTeamQW.eq("user_id", user_id);
        List<TeamsDetail> userInTeams = ChatWebSocketServer.teamDetailMapper.selectList(userInTeamQW);
        for (TeamsDetail userInTeam : userInTeams) {
            JSONObject item = new JSONObject();
            QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
            chatQueryWrapper.eq("receiver_id", userInTeam.getTeamId()).orderByAsc("id");
            List<Chat> chats = ChatWebSocketServer.chatMapper.selectList(chatQueryWrapper);
            item.put("team_id", userInTeam.getTeamId());
            item.put("contents", chats);
            res.add(item);
        }
        return res;
    }

    private JSONObject historyChat(Integer id) { //用户登录时获取历史消息   离线-> 登录
        JSONObject res = new JSONObject();
        res.put("event", "history_chat");
        List<JSONObject> history = new ArrayList<>();

        String chatUserSql = "select id from (\n" +
                "   select id , max(sendtime) as sendtime from (\n" +
                "      select sender_id as `id`,max(id) as sendtime from chat where receiver_id = " + id + " group by sender_id\n" +
                "      union\n" +
                "      select receiver_id as `id`,max(id) as sendtime from chat where sender_id = " + id + " group by receiver_id\n" +
                "   ) as t\n" +
                "   group by id\n" +
                "   order by sendtime desc\n" +
                ") as t1";
        List<User> chatWithUsers = ChatWebSocketServer.chatuserMapper.selectList(new QueryWrapper<User>().inSql("id", chatUserSql));
        if(chatWithUsers != null) {
            for (User chatWithUser : chatWithUsers) {
                JSONObject item = new JSONObject();
                item.put("id", chatWithUser.getId());
                item.put("name", chatWithUser.getUsername());
                item.put("photo", chatWithUser.getPhoto());
                item.put("last_login_time", chatWithUser.getLastLoginTime());
                List<ChatContent> contents = new ArrayList<>();
                QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
                chatQueryWrapper.eq("sender_id", chatWithUser.getId()).eq("receiver_id", id)
                        .or().eq("sender_id", id).eq("receiver_id", chatWithUser.getId())
                        .orderByAsc("id");
                List<Chat> chats = ChatWebSocketServer.chatMapper.selectList(chatQueryWrapper);
                for (Chat chat : chats) {
                    contents.add(new ChatContent(chat.getId(), Objects.equals(chat.getSenderId(), id), chat.getContent(), chat.getStatus(), chat.getSendtime()));
                }
                item.put("contents", contents);
                history.add(item);
            }
        }
        res.put("history", history);

        res.put("team_chat", receiveTeamChat(id));
        return res;
    }

    public void sendChat(Integer receiver_id, String content) {
        JSONObject res = new JSONObject();
        res.put("event", "receive_chat");
        res.put("receive", senderMessage(content));

        if(receiver_id == 0){ //向公屏发送 -> 所有在线的都可以收到
            List<User> allUsers = ChatWebSocketServer.chatuserMapper.selectList(null);
            for (User user : allUsers) {
                if(ChatWebSocketServer.users.get(user.getId()) != null) {
                    ChatWebSocketServer.users.get(user.getId()).sendMessage(res.toString());
                }
            }
        }else if(isTeam(receiver_id)) {
            sendTeamChat(receiver_id, content);
        }else {
            if(ChatWebSocketServer.users.get(receiver_id) != null) {
                ChatWebSocketServer.users.get(receiver_id).sendMessage(res.toString());
            }
        }
        saveToDatabase(receiver_id, content);
    }
    public void receiveChat(Integer id) {
        ChatWebSocketServer.users.get(id).sendMessage(historyChat(id).toString());
    }
}
