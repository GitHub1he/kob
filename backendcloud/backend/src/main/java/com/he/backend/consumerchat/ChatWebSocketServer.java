package com.he.backend.consumerchat;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumerchat.utils.ChatRecordPool;
import com.he.backend.consumerchat.utils.SendChating;
import com.he.backend.consumerpk.utils.JwtAuthentication;
import com.he.backend.mapper.ChatMapper;
import com.he.backend.mapper.TeamDetailMapper;
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
    public SendChating sendChating = null;
    public static UserMapper userMapper;
    public static ChatMapper chatMapper;
    public static TeamDetailMapper teamDetailMapper;
    public final static ChatRecordPool chatRecordPool = new ChatRecordPool();

    private static final String sendChatUrl = "http://127.0.0.1:3003/user/chat/send/";
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        ChatWebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setTeamDetailMapper(TeamDetailMapper teamDetailMapper) {
        ChatWebSocketServer.teamDetailMapper = teamDetailMapper;
    }
    @Autowired
    public void setChatMapper(ChatMapper chatMapper) {
        ChatWebSocketServer.chatMapper = chatMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected!");

        Integer userId = JwtAuthentication.getUserId(token);

        this.user = userMapper.selectById(userId);
        if(this.user != null) {
            users.put(userId, this);

        }else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");

        if(this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void sendChat(Integer senderId, Integer receiverId, String content) {
        System.out.println("wsSendMessage");
        SendChating sendChating = new SendChating(senderId, receiverId, content);
        if(users.get(senderId) != null) {
            users.get(senderId).sendChating = sendChating;
            sendChating.start();
        }else System.out.println("发送者不存在");
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
        } else if ("stop-matching".equals(event)) {
//            stopMatching();
        } else if ("move".equals(event)){
//            move(data.getInteger("direction"));
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