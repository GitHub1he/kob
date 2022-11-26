package com.he.backend.consumerchat;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumerchat.utils.Chating;
import com.he.backend.consumerpk.utils.JwtAuthentication;
import com.he.backend.mapper.ChatMapper;
import com.he.backend.mapper.TeamDetailMapper;
import com.he.backend.mapper.TeamMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/chat/{token}")  // 注意不要以'/'结尾
public class ChatWebSocketServer {
    public static final ConcurrentHashMap<Integer, ChatWebSocketServer> users = new ConcurrentHashMap<>();
    private Session session = null;
    private User user;
    public Chating chating = null;
    public static UserMapper chatuserMapper;
    public static ChatMapper chatMapper;
    public static TeamMapper teamMapper;
    public static TeamDetailMapper teamDetailMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        ChatWebSocketServer.chatuserMapper = userMapper;
    }
    @Autowired
    public void setTeamDetailMapper(TeamDetailMapper teamDetailMapper) {
        ChatWebSocketServer.teamDetailMapper = teamDetailMapper;
    }
    @Autowired
    public void setChatMapper(ChatMapper chatMapper) {
        ChatWebSocketServer.chatMapper = chatMapper;
    }
    @Autowired
    public void setTeamMapper(TeamMapper teamMapper) {
        ChatWebSocketServer.teamMapper = teamMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("chat_connected!");
        Integer userId = JwtAuthentication.getUserId(token);

        this.user = chatuserMapper.selectById(userId);
        if(this.user != null) {
            users.put(userId, this);
            this.chating = new Chating(userId);
        }else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("chat_disconnected!");

        if(this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void sendChat(Integer senderId, Integer receiverId, String content) {
        System.out.println("ws-Send-Message");
        if(users.get(senderId) != null) {
            users.get(senderId).chating.sendChat(receiverId, content);
        }else System.out.println("发送者不存在");
    }
    public static void receiveChat(Integer id) {
        System.out.println("ws-Receive-Message");
        if(users.get(id) != null) {
            users.get(id).chating.receiveChat(id);
        }else System.out.println("接收者不存在");
    }

    @OnMessage
    public void onMessage(String message, Session session) { //当做路由
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("send-message".equals(event)){
            Integer senderId = data.getInteger("sender_id");
            Integer receiverId = data.getInteger("receiver_id");
            String content = data.getString("content");
            sendChat(senderId, receiverId, content);
        } else if ("receive-message".equals(event)) {
            receiveChat(this.user.getId());
        } else if ("receive-team-message".equals(event)) {
            Integer id = this.user.getId();
            System.out.println("ws-team-Receive-Message");
            if(users.get(id) != null) {
                users.get(id).chating.receiveTeamChat(id);
            }else System.out.println("接收者不存在");
        } else if ("send-screen-message".equals(event)) {
            Integer senderId = data.getInteger("sender_id");
            Integer receiverId = data.getInteger("receiver_id");
            String content = data.getString("content");
            sendChat(senderId, receiverId, content);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}