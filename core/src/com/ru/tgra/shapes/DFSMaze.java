package com.ru.tgra.shapes;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by andri on 10/27/15.
 */
public class DFSMaze {
    class Pair{
        int x, z;
        Pair(int x, int z){
            this.x = x;
            this.z = z;
        }
        public String toString(){
            return "x: " + x + " z: " + z;
        }
    }
    private int xSize, zSize;
    private DFSCell[][] cells;

    public DFSMaze(int xSize, int zSize){
        this.xSize = xSize;
        this.zSize = zSize;
        Random rand = new Random();
        cells = new DFSCell[xSize][zSize];
        for (int x = 0; x < xSize; x++){
            for (int z = 0; z < zSize; z++){
                cells[x][z] = new DFSCell(x, z);
            }
        }

        boolean[][] visited = new boolean[xSize][zSize];
        dfs(visited, new Pair(xSize/2, zSize/2));
//        for (int i = 0;  i < (xSize * zSize)/5; i++){
//            cells[rand.nextInt(xSize - 2) + 1][rand.nextInt(zSize - 2) + 1].destroyRandomWall();
//        }
    }


    private void dfs(boolean[][] visited, Pair curr){
        visited[curr.x][curr.z] = true;
        ArrayList<Pair> adj = getAdj(curr);
        for (Pair p : adj){
            if (!visited[p.x][p.z]){
                if (p.x == curr.x){
                    if (p.z < curr.z){
                        if (!cells[curr.x][curr.z].destroyWall(1)) { //west
                            cells[p.x][p.z].destroyWall(3); //east
                        }
                    } else {
                        if (!cells[curr.x][curr.z].destroyWall(3)){ //east
                            cells[p.x][p.z].destroyWall(1); //west
                        }
                    }
                } else {
                    if (p.x < curr.x){
                        if (!cells[curr.x][curr.z].destroyWall(0)){ //south
                            cells[p.x][p.z].destroyWall(2); //north
                        }
                    } else {
                        if (!cells[curr.x][curr.z].destroyWall(2)) { //north
                            cells[p.x][p.z].destroyWall(0); //south
                        }
                    }
                }
                dfs(visited, p);
            }
        }
    }


    private ArrayList<Pair> getAdj(Pair p){
        int x = p.x;
        int z = p.z;
        ArrayList<Pair> result = new ArrayList<Pair>();
        if (z > 0){
            result.add(new Pair(x, z - 1));
        }
        if (z < zSize - 1){
            result.add(new Pair(x, z + 1));
        }
        if (x > 0){
            result.add(new Pair(x - 1, z));
        }
        if (x < xSize - 1){
            result.add(new Pair(x + 1, z));
        }
        Collections.shuffle(result);
        return result;
    }

    public void update(Player player, float deltatime){
        for (DFSCell cell : getAdjCell(player.camera.eye.x, player.camera.eye.z)){
            System.out.println(cell);
        }
    }


    private ArrayList<DFSCell> getAdjCell(float x, float z){
        int tx = (int) Math.floor(x);
        int tz = (int) Math.floor(z);
        ArrayList<DFSCell> result = new ArrayList<DFSCell>();
        if (tx < 0 || tx > xSize || tz < 0 || tz > zSize){
            return result;
        }
        result.add(cells[tx][tz]);
        if (tz > 0 && tz < zSize){
            result.add(cells[tx][tz - 1]);
            if (tx > 0){
                result.add(cells[tx - 1][tz - 1]);
            }
            if (tx < zSize - 1){
                result.add(cells[tx + 1][tz - 1]);
            }
        }
        if (tz < zSize - 1 && tz >= 0){
            if (tx > 0){
                result.add(cells[tx - 1][tz + 1]);
            }
            if (tx < zSize - 1){
                result.add(cells[tx + 1][tz + 1]);
            }
        }
        if (tx > 0 && tx < xSize){
            result.add(cells[tx - 1][tz]);
        }
        if (tx < xSize - 1 && tx >= 0){
            result.add(cells[tx + 1][tz]);
        }
        return result;
    }

    public void display(Shader shader){
        for (int x = 0; x < xSize; ++x){
            for (int z = 0; z < zSize; ++z){
                cells[x][z].display(shader);
            }
        }
    }

}
