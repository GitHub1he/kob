package com.he.backend.consumerpk;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumerpk.utils.Game;
import com.he.backend.consumerpk.utils.JwtAuthentication;
import com.he.backend.mapper.BotMapper;
import com.he.backend.mapper.RecordMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.Bot;
import com.he.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/pk/{token}")  // 注意不要以'/'结尾
public class PkWebSocketServer {
    public static final ConcurrentHashMap<Integer, PkWebSocketServer> users = new ConcurrentHashMap<>();  //线程安全 static对所有实例可见
    private Session session = null;
    private User user;
    public static UserMapper pkUserMapper;
    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;
    private static BotMapper botMapper;
    public Game game = null;
    private final static String addPlayUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayUrl = "http://127.0.0.1:3001/player/remove/";


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        PkWebSocketServer.pkUserMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        PkWebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){ //在两个spring中通信
        PkWebSocketServer.restTemplate = restTemplate;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper){
        PkWebSocketServer.botMapper = botMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected!");

        Integer userId = JwtAuthentication.getUserId(token);

        this.user = pkUserMapper.selectById(userId);
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

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = pkUserMapper.selectById(aId), b = pkUserMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId) , botB = botMapper.selectById(bBotId);

        Game game = new Game(13,
                14,
                10,
                a.getId(),
                botA,
                b.getId(),
                botB);
        game.createMap();
        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).game = game;

        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toString());

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toString());
    }
    private void startMatching(Integer botId) {
        System.out.println("start matching");
        MultiValueMap<String , String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        restTemplate.postForObject(addPlayUrl, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayUrl, data, String.class);
    }

    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            if(game.getPlayerA().getBotId().equals(-1)) //如果机器人代码为 -1 ，则为亲自出马
                game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            if(game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) { //当做路由
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching(data.getInteger("bot_id"));
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)){
            move(data.getInteger("direction"));
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