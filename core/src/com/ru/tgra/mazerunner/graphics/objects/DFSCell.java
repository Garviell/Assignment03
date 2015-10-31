package com.ru.tgra.mazerunner.graphics.objects;

import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.objects.Player;
import com.ru.tgra.mazerunner.graphics.objects.Wall;

import java.util.ArrayList;
import java.util.Random;

public class DFSCell {
    public Wall[] walls;
    public Door door;
    public DeadlyFloor floor;
    private int x, z;

    public DFSCell(int x, int z) {
        walls = new Wall[4];
        if (x == 0) walls[0] = new Wall(x, z, 0); //south
        if (z == 0) walls[1] = new Wall(x, z, 1); //west
        walls[2] = new Wall(x, z, 2); //north (Don't question it)
        walls[3] = new Wall(x, z, 3); //east
        this.x = x;
        this.z = z;
        door = null;
        floor = null;
    }

    public void display(Shader shader, float deltaTime) {
        if (door != null ) { door.update(deltaTime); }
        if (floor != null ) { floor.display(shader, deltaTime); }
        for (Wall wall : walls) {
            if (wall != null) { wall.draw(shader); }
            if (door != null) { door.display(shader); }
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


    public Wall intersects(Player player) {
        for (Wall wall : walls){
            if (wall != null){
               if(wall.intersects(player)){
                   return wall;
               }
            }
        }
        return null;
    }

    public void doorFloorCollision(Player player) {
        // Move camera if door.collision
        if (door != null) {if (door.intersects(player.camera)) {
            door.collision(player); } }
        // Check if floor is deadly
        if (floor != null) { floor.onFloor(player); }
    }


    public String toString(){
        return "(" + x + ", " + z + ")";
    }
}
