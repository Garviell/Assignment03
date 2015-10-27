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
        boolean next = false;
//        for (DFSCell cell : cells[1]){
//            cell.destroyWall(Wall.Sides.east);
//        }
//        cells[0][0].destroyWall(Wall.Sides.east);
//        cells[1][0].destroyWall(Wall.Sides.east);
//        cells[2][0].destroyWall(Wall.Sides.east);
       // cells[0][2].destroyWall(Wall.Sides.east);

        for (DFSCell[] cell : cells){
            for (DFSCell c : cell){
                c.printWalls();
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
        dfs(visited, new Pair(0, 0), 0);
//        for (int i = 0;  i < (xSize * zSize)/10; i++){
//            cells[rand.nextInt(xSize)][rand.nextInt(zSize)].destroyRandomWall();
//        }
    }


    private void dfs(boolean[][] visited, Pair curr, int count){
        visited[curr.x][curr.z] = true;
        count++;
        ArrayList<Pair> adj = getAdj(curr);
        for (Pair p : adj){
            if (!visited[p.x][p.z]){
                if (p.x == curr.x){
                    if (p.z < curr.z){
                        System.out.printf("%s destroying west to get to %s\n", curr, p);
                        if (!cells[curr.x][curr.z].destroyWall(1)) { //west
                            System.out.printf("%s destroying east of %s instead\n", curr, p);
                            cells[p.x][p.z].destroyWall(3); //east
                        }
                    } else {
                        System.out.printf("%s destroying east to get to %s\n", curr, p);
                        if (!cells[curr.x][curr.z].destroyWall(3)){ //east
                            System.out.printf("%s destroying west of %s instead\n", curr, p);
                            cells[p.x][p.z].destroyWall(1); //west
                        }
                    }
                } else {
                    if (p.x < curr.x){
                        System.out.printf("%s destroying south to get to %s\n", curr, p);
                        if (!cells[curr.x][curr.z].destroyWall(0)){ //south
                            System.out.printf("%s destroying north of %s instead\n", curr, p);
                            cells[p.x][p.z].destroyWall(2); //north
                        }
                    } else {
                        System.out.printf("%s destroying north to get to %s\n", curr, p);
                        if (!cells[curr.x][curr.z].destroyWall(2)) { //north
                            System.out.printf("%s destroying south of %s instead\n", curr, p);
                            cells[p.x][p.z].destroyWall(0); //south
                        }
                    }
                }
                dfs(visited, p, count);
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

    public void display(Shader shader){
        for (int x = 0; x < xSize; ++x){
            for (int z = 0; z < zSize; ++z){
                cells[x][z].display(shader);
            }
        }
    }

}
