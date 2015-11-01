package com.ru.tgra.mazerunner.graphics;

import com.ru.tgra.mazerunner.graphics.objects.*;

import java.util.*;


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
    private int numDoors = 8;
    private int numFloors = 8;

    public DFSMaze(int xSize, int zSize){
        this.xSize = xSize;
        this.zSize = zSize;
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

        int a = 0;
        while (a < numDoors) {
            Random r = new Random();
            int x = r.nextInt(xSize-1) + 1;
            int z = r.nextInt(xSize-1) + 1;
            if (cells[x][z].walls[3] != null && cells[x][z-1].walls[3] != null) {
                if (cells[x][z].door == null) {
                    cells[x][z].door = new Door(x, z);
                    a++;
                }
            }
        }

        a = 0;
        while (a < numFloors) {
            Random r = new Random();
            int x = r.nextInt(xSize);
            int z = r.nextInt(xSize);
            if (cells[x][z].floor == null) {
                cells[x][z].floor = new DeadlyFloor(x, z, 0.9f, 0.3f);
                a++;
            }
        }

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

    public void update(Player player){
        checkCollision(player);
    }


    private ArrayList<DFSCell> getAdjCell(float x, float z){
        int tx = (int) Math.floor(x);
        int tz = (int) Math.floor(z);

        //System.out.println("x: " + x + " z: " + z + "  tx: " + tx + " tz: " + tz);
        ArrayList<DFSCell> result = new ArrayList<DFSCell>();
        if (tx < 0 || tx > xSize || tz < 0 || tz > zSize) {
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
            result.add(cells[tx][tz + 1]);
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

    private void checkCollision(Player player) {
        ArrayList<Wall> hit = new ArrayList<Wall>();

        boolean goOn = true;
        while (goOn) {

            for (DFSCell cell : getAdjCell(player.camera.eye.x, player.camera.eye.z)) {
                for (Wall wall : cell.walls) {
                    if (wall != null) {
                        if (wall.intersects(player)) {
                            hit.add(wall);
                        }
                    }
                }
            }

            if (!hit.isEmpty()) {
                float minHit = Float.MAX_VALUE;
                Wall minHitWall = null;
                String xz = "";

                // Find who is nearest
                for (Wall wall : hit) {
                    float distX = wall.getDistX();
                    if (distX < minHit) {
                        minHit = distX;
                        minHitWall = wall;
                        xz = "X";
                    }

                    float distZ = wall.getDistZ();
                    if (distZ < minHit) {
                        minHit = distZ;
                        minHitWall = wall;
                        xz = "Z";
                    }
                }

                moveCamera(player, minHitWall, xz);
                hit.clear();
            } else {
                goOn = false;
            }
        }

        for (DFSCell cell : getAdjCell(player.camera.eye.x, player.camera.eye.z)) {
            if (cell.door != null) { cell.doorCollision(player); }
        }

        int x = (int) Math.floor(player.camera.eye.x);
        int z = (int) Math.floor(player.camera.eye.z);
        if (x >= 0 && x < xSize && z >= 0 && z < zSize) {
            if (cells[x][z].floor != null) {
                cells[x][z].floor.onFloor(player);
            }
        }

    }

    private void moveCamera(Player player, Wall minHitWall, String xz) {
        if (xz.equals("Z")) {
            float posX = minHitWall.getPosX();
            float widthX = minHitWall.getWidX();
            if (player.camera.eye.x < posX) {
                player.camera.eye.x = posX - (widthX / 2) - player.body;
            } else {
                player.camera.eye.x = posX + (widthX / 2) + player.body;
            }
        }
        if (xz.equals("X")) {
            float posZ = minHitWall.getPosZ();
            float widthZ = minHitWall.getWidZ();
            if (player.camera.eye.z < posZ) {
                player.camera.eye.z = posZ - (widthZ / 2) - player.body;
            } else {
                player.camera.eye.z = posZ + (widthZ / 2) + player.body;
            }

        }
    }

    public void display(Shader shader, float deltaTime){
        for (int x = 0; x < xSize; ++x){
            for (int z = 0; z < zSize; ++z){
                cells[x][z].display(shader, deltaTime);
            }
        }
    }

}
