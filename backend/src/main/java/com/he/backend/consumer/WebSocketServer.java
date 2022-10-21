package com.he.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumer.utils.Game;
import com.he.backend.consumer.utils.JwtAuthentication;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();  //线程安全 static对所有实例可见
    private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private Session session = null;
    private User user;
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
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
        System.out.println(user);
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");

        if(this.user != null) {
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    private void startMatching() {
        System.out.println("start matching");
        matchpool.add(this.user);

        while (matchpool.size() >= 2) {
            Iterator<User> it = matchpool.iterator();
            User a = it.next(), b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Game game = new Game(13,14, 20);
            game.createMap();
            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("gamemap", game.getG());
            users.get(a.getId()).sendMessage(respA.toString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("gamemap", game.getG());
            users.get(b.getId()).sendMessage(respB.toString());
        }
    }

    private void stopMatching() {
        System.out.println("stop matching");
        matchpool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) { //当做路由
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
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