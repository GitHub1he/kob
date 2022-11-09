package com.he.backend.consumerpk.utils;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumerpk.PkWebSocketServer;
import com.he.backend.pojo.Bot;
import com.he.backend.pojo.Record;
import com.he.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, -1, 0, 1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private String status = "playing"; // playing -> finished
    private String winer = ""; //all:平局, A : A赢 ， B : B赢
    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    private ReentrantLock lock = new ReentrantLock();
    public Game(Integer rows,
                Integer cols,
                Integer inner_walls_count,
                Integer idA,
                Bot botA,
                Integer idB,
                Bot botB
    ) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        Integer botIdA = -1,botIdB = -1;
        String botCodeA = "",botCodeB = "";
        if(botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if(botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        playerA = new Player(idA, botIdA, botCodeA,rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB,1, cols - 2, new ArrayList<>());
    }
    public Player getPlayerA(){ return playerA; }
    public Player getPlayerB(){ return playerB; }
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }
    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    //判断是否连通
    private boolean check_connectivity(int sx, int sy, int tx, int ty) { //判断(sx,sy)和(tx,ty) 在图中能不能连通,从点a能否到达点b
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for(int i = 0; i< 4 ;i++){
            int x = sx + dx[i], y = sy + dy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if(check_connectivity(x, y,tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }
    private boolean draw() {
        //给四周加上障碍物
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                if (r == 0 || c == 0 || r + 1 == this.rows || c + 1 == this.cols) {
                    g[r][c] = 1;
                } else {
                    g[r][c] = 0;
                }
            }
        }
        //创建随机障碍物
        Random random = new Random();
        for(int i = 0 ; i < this.inner_walls_count; i++){
            for(int j = 0 ; j < 1000 ; j++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                //保证不是出生点和已经是墙的值不被覆盖
                if (g[r][c] == 1 || g[this.rows -1 -r][this.cols -1 -c] == 1 || r == this.rows -2 && c ==1 || c == this.cols -2 && r == 1) continue;

                g[r][c] = g[this.rows -1 -r][this.cols -1 -c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2) ;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }

    private String getInput(Player player) { // 将当前player的 局面编码为字符串
        // -> 地图 # 自己的sx # sy # 我的操作 # 对手的sx # sy # 对手的操作 #
        Player me , you ;
        if(player.getId().equals(playerA.getId())) {
            me = playerA;
            you = playerB;
        }else {
            me = playerB;
            you = playerA;
        }

        return getMapString() + "#"
                + me.getSx() + "#"
                + me.getSy() + "#("
                + me.getStepsString() + ")#"
                + you.getSx() + "#"
                + you.getSy() + "#("
                + you.getStepsString() + ")";
    }
    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) return; //亲自出马，不需要执行代码

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));

        PkWebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }
    private boolean nextStep(){ //等待两名玩家的下一步操作
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for(int i = 0; i < 24 ; i++){
            try {
                Thread.sleep(200);
                lock.lock();
                try {
                    if(nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendAllMessage(String message) {
        if(PkWebSocketServer.users.get(playerA.getId()) != null)
            PkWebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(PkWebSocketServer.users.get(playerB.getId()) != null)
            PkWebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }
    private boolean check_valid(List<Cell> cells1, List<Cell> cells2){ //判断第一条蛇与第二条蛇是否相撞
        int n = cells1.size();
        Cell cell = cells1.get(n - 1);
        if(g[cell.x][cell.y] == 1) return false;

        for(int i = 0 ; i < n -1 ; i++){
            if(cells1.get(i).x == cell.x && cells1.get(i).y == cell.y) return false;
        }
        for(int i = 0 ; i < n - 1; i++) {
            if(cells2.get(i).x == cell.x && cells2.get(i).y == cell.y) return false;
        }
        return true;
    }
    private void judge() { //判断两位玩家下一步操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if(!validA || !validB){
            status = "finished";
            if(!validA && !validB){
                winer = "all";
            }else if (!validA){
                winer = "B";
            }else {
                winer = "A";
            }
        }
    }

    private void sendMove() { //向两个client传递移动信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }
    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0 ; i < rows; i++ ) {
            for(int j = 0; j < cols; j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDatabase() {
        User userA = PkWebSocketServer.userMapper.selectById(playerA.getId());
        User userB = PkWebSocketServer.userMapper.selectById(playerB.getId());

        if("all".equals(winer)) {
            userA.setRating(userA.getRating() + 1);
            userB.setRating(userB.getRating() + 1);
        } else if ("A".equals(winer)) {
            userA.setRating(userA.getRating() + 5);
            userB.setRating(userB.getRating() - 3);
        } else if ("B".equals(winer)) {
            userA.setRating(userA.getRating() -3);
            userB.setRating(userB.getRating() +5);
        }

        PkWebSocketServer.userMapper.updateById(userA);
        PkWebSocketServer.userMapper.updateById(userB);

        Record record = new Record(
            null,
            playerA.getId(),
            playerA.getSx(),
            playerA.getSy(),
            playerB.getId(),
            playerB.getSx(),
            playerB.getSy(),
            playerA.getStepsString(),
            playerB.getStepsString(),
            getMapString(),
            winer,
            new Date()
        );
        PkWebSocketServer.recordMapper.insert(record);
    }
    private void sendResult() { //向两个client公布结果
        JSONObject resp = new JSONObject();
        resp.put("event" , "result");
        resp.put("winer" , winer);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }
    @Override
    public void run() {
        for(int i = 0 ; i < 1000 ; i++) {
            if(nextStep()){ //客户端双方是否输入下一步操作
                judge();
                if("playing".equals(status)) {
                    sendMove();
                }else {
                    sendResult();
                    break;
                }
            }else {
                status = "finished";
                lock.lock();
                try{
                    if(nextStepA == null && nextStepB == null) {
                        winer = "all";
                    }else if(nextStepA == null) {
                        winer = "B";
                    }else winer = "A";
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}