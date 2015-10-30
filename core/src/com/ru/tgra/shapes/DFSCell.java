package com.ru.tgra.shapes;

import java.util.Random;

/**
 * Created by andri on 10/27/15.
 */
public class DFSCell {
    private Wall[] walls;
    private int x, z;

    public DFSCell(int x, int z) {
        walls = new Wall[4];
        if (x == 0) walls[0] = new Wall(x, z, 0); //south
        if (z == 0) walls[1] = new Wall(x, z, 1); //west
        walls[2] = new Wall(x, z, 2); //east (Don't question it)
        walls[3] = new Wall(x, z, 3); //north
        this.x = x;
        this.z = z;
    }

    public void display(Shader shader) {
        for (Wall wall : walls) {
            if (wall != null) {
                wall.draw(shader);
            }
        }
    }

    public boolean destroyWall(int side) {
        if (walls[side] == null) return false;
        walls[side] = null;
        return true;
    }

    public void destroyRandomWall() {
        Random rand = new Random();
        boolean check = false;
        for (Wall wall : walls) {
            if (wall != null) check = true;
        }

        while (check) {
            int tmp = rand.nextInt(4);
            if (walls[tmp] != null) {
                walls[tmp] = null;
                check = false;
            }
        }

    }


    public void DESTROY() {
        for (int i = 0; i < 4; i++) {
            walls[i] = null;
        }
        System.out.println();
    }


    public boolean intersects(Player player) {
        for (Wall wall : walls){
            if (wall != null){
                wall.intersects(player);
            }
        }

        //Intersects DEATHCRUSHWALLS
        //Intersects DEATHFALLFLOORS
        return false;
    }

    private void moveCamera(Player player){
        return;
    }

    public String toString(){
        return "(" + x + " + " + z + ")";
    }
}
