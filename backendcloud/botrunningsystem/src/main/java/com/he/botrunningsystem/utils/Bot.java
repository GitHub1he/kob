package com.he.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bot implements java.util.function.Supplier<Integer>{
    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static class Cell {
        public int x, y;
        public Cell(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private boolean check_tail_increasing(int step) { //检测当前回合，蛇的长度是否增加；
        if(step <= 8) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells(int sx, int sy, String steps){
        List<Cell> res = new ArrayList<>();

        steps = steps.substring(1,steps.length()-1);
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0 , -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for(int i = 0; i < steps.length(); i++){
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if(!check_tail_increasing(++ step)) {
                res.remove(0);
            }
        }
        return res;
    }
    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for(int i = 0,k = 0 ; i < 13 ; i++){
            for(int j = 0 ; j < 14 ; j++, k++){
                if(strs[0].charAt(k) == '1')
                    g[i][j] = 1;
            }
        }

        int mySx = Integer.parseInt(strs[1]), mySy = Integer.parseInt(strs[2]);
        int youSx = Integer.parseInt(strs[4]), youSy = Integer.parseInt(strs[5]);

        List<Cell> myCells = getCells(mySx, mySy, strs[3]);
        List<Cell> youCells = getCells(youSx, youSy, strs[6]);

        for (Cell c : myCells)  g[c.x][c.y] = 1;
        for (Cell c : youCells)  g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0 , -1};
        for (int i = 0; i < 4; i++) {
            int x = myCells.get(myCells.size() - 1).x + dx[i];
            int y = myCells.get(myCells.size() - 1).y + dy[i];
            if(x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0){ //如果下一格能走
                return i;
            }
        }
        return 0;
    }
}
