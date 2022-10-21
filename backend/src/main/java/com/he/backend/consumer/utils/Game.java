package com.he.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;

    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, -1, 0, 1};

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
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
}