package com.he.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.consumer.WebSocketServer;
import com.he.backend.pojo.Record;

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

    private ReentrantLock lock = new ReentrantLock();
    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, cols - 2, new ArrayList<>());
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

    private boolean nextStep(){ //等待两名玩家的下一步操作
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        if(WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
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
        WebSocketServer.recordMapper.insert(record);
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