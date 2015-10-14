package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.Random;

public class Maze {
    public int score;
    Random random;
    private int row;
    private int col;
    private float body;
    private Cell[][] cell;
    private int numDoors;
    private Door[] doors;
    private int numPills = 16;
    private int numDeadlyFloors;


    /*
     * A very simple maze that is entirely randomly generated with no smart maze generation
     */
    public Maze() {
        col = 20;
        row = 20;
        cell = new Cell[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cell[i][j] = new Cell(i, j);
            }
        }

        numDoors = 20;
        doors = new Door[numDoors];
        random = new Random();
        for (int i = 0; i < numDoors; i++) {
            int x = random.nextInt(20);
            int z = random.nextInt(20);
            if (cell[x][z].northWall == null && !cell[x][z].door) {
                doors[i] = new Door(cell[x][z]);
                cell[x][z].door = true;
            }
        }

        int n = 0;
        while (n < numPills) {
            int x = random.nextInt(20);
            int z = random.nextInt(20);
            if (cell[x][z].pill == null) {
                cell[x][z].pill = new Pill(x, z);
                n++;
            }
        }

        numDeadlyFloors = 20;
        for (int i = 0; i < numDeadlyFloors; i++) {
            int x = random.nextInt(20);
            int z = random.nextInt(20);
            if (cell[x][z].deadly == null) {
                cell[x][z].deadly = new DeadlyFloor(x, z, 0.9f, 0.3f);
            }
        }

        body = 0.2f;
        score = 0;
    }

    public void display(Shader shader, float deltaTime) {
        ModelMatrix.main.pushMatrix();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cell[i][j].display(shader);
                if (cell[i][j].pill != null) cell[i][j].pill.display(shader, deltaTime);
                if (cell[i][j].deadly != null) cell[i][j].deadly.display(shader, deltaTime);
            }
        }
        ModelMatrix.main.popMatrix();


    }

    public void displayDoors(Shader shader, float deltaTime) {
        shader.setMaterialSpecular(0.1f, 0.0f, 0.0f, 1.0f);
        shader.setShininess(1030);
        for (int i = 0; i < numDoors; i++) {
            if (doors[i] != null) {
                doors[i].update(deltaTime);
                doors[i].display(shader);
            }
        }

    }

    public void checkCollision(Player player, float deltaTime) {
        Camera cam = player.camera;
        outOfBoard(player);

        ArrayList<Wall> check = new ArrayList<Wall>();
        ArrayList<Wall> hit = new ArrayList<Wall>();

        findClosestWalls(player, check);

        // Find walls that intersects
        boolean goOn = true;
        while (goOn) {
            for (Wall wall : check) {
                if (intersects(cam, wall)) hit.add(wall);
            }
            if (!hit.isEmpty()) {
                float minHit = Float.MAX_VALUE;
                Wall minHitWall = null;
                String xz = "";

                // Find who is nearest
                for (Wall wall : hit) {
                    if (wall.distanceX < minHit) {
                        minHit = wall.distanceX;
                        minHitWall = wall;
                        xz = "X";
                    }
                    if (wall.distanceZ < minHit) {
                        minHit = wall.distanceZ;
                        minHitWall = wall;
                        xz = "Z";
                    }
                }
                moveCamera(cam, minHitWall, xz);
                check.clear();
                check = (ArrayList<Wall>) hit.clone();
                hit.clear();
            } else {
                goOn = false;
            }
        }

        for (int i = 0; i < numDoors; i++) {
            if (doors[i] != null) {
                if (doors[i].intersects(cam)) {
                    doorCollision(player, cam, doors[i]);
                }
            }
        }

    }

    public void findClosestWalls(Player player, ArrayList<Wall> check) {
        Camera cam = player.camera;
        int eyex = (int) Math.floor(cam.eye.x);
        int eyez = (int) Math.floor(cam.eye.z);


        if (cam.eye.x >= 0 && cam.eye.x < 20 && cam.eye.z >= 0 && cam.eye.z < 20) {
            Cell currentCell = cell[eyex][eyez];
            if (currentCell.pill != null) {
                checkPill(cam, cell[eyex][eyez], player);
            }

            for (int i = Math.max(0, eyex - 1); i < Math.min(20, eyex + 2); i++) {
                for (int j = Math.max(0, eyez - 1); j < Math.min(20, eyez + 2); j++) {
                    if (cell[i][j].northWall != null) check.add(cell[i][j].northWall);
                    if (cell[i][j].eastWall != null) check.add(cell[i][j].eastWall);
                }
            }
            if (currentCell.deadly != null) {
                if (currentCell.deadly.isDeadly()) {
                    player.flipAlive();
                }

            }
        }
        // Deal with the corners.. Sort off.
        else if (cam.eye.x <= 0 && cam.eye.x > -1 || cam.eye.x >= 20 && cam.eye.x < 21
                || cam.eye.z <= 0 && cam.eye.z > -1 || cam.eye.z >= 20 && cam.eye.z < 21) {
            for (int i = eyex - 1; i < eyex + 2; i++) {
                for (int j = eyez - 1; j < eyez + 2; j++) {
                    try {
                        if (cell[i][j].northWall != null) check.add(cell[i][j].northWall);
                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                        // It's fine, just carry on, Nothing to see here
                    }

                    try {
                        if (cell[i][j].eastWall != null) check.add(cell[i][j].eastWall);
                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                        // It's fine, just carry on, Nothing to see here
                    }

                }
            }
        }
    }

    //This functionality should largely be in the wall class but we decided to focus on fancier shaders instead
    public boolean intersects(Camera cam, Wall wall) {
        float distanceX = Math.abs(cam.eye.x - wall.posX);
        float distanceZ = Math.abs(cam.eye.z - wall.posZ);
        float buffer = 0.01f;

        if (distanceX > (wall.height / 2 + body - buffer) || distanceZ > (wall.width / 2 + body - buffer)) return false;
        buffer = 0.11f;

        wall.distanceX = (wall.height / 2 + body - buffer) - distanceX;
        wall.distanceZ = (wall.width / 2 + body - buffer) - distanceZ;

        return distanceX <= (wall.height / 2 + body) || distanceZ <= (wall.width / 2 + body);
    }

    //To slide the camera along the wall.
    //Has some hiches because of how we construct the maze out of small pieces.
    public void moveCamera(Camera cam, Wall minHitWall, String xz) {
        if (xz.equals("X")) {
            if (cam.eye.x < minHitWall.posX) {
                cam.eye.x = minHitWall.posX - minHitWall.height / 2 - body;
            } else {
                cam.eye.x = minHitWall.posX + minHitWall.height / 2 + body;
            }
        }
        if (xz.equals("Z")) {
            if (cam.eye.z < minHitWall.posZ) {
                cam.eye.z = minHitWall.posZ - minHitWall.width / 2 - body;
            } else {
                cam.eye.z = minHitWall.posZ + minHitWall.width / 2 + body;
            }

        }
    }

    //Door that kills you,  pretty basic
    public void doorCollision(Player player, Camera cam, Door door) {
        float distanceZ = Math.abs(cam.eye.z - door.endZ);

        if (door.distanceZ < door.distanceX) {
            if (door.width > 0.4f && distanceZ <= 0.21f) {
                score = 0;
                player.flipAlive();
            } else {
                if (cam.eye.z < door.posZ) {
                    cam.eye.z = door.posZ - door.width / 2 - body;
                } else {
                    cam.eye.z = door.posZ + door.width / 2 + body;
                }
            }
        } else {
            if (cam.eye.x < door.posX) {
                cam.eye.x = door.posX - door.height / 2 - body;
            } else {
                cam.eye.x = door.posX + door.height / 2 + body;
            }
        }

    }

    //Check if you are on a pill.
    public void checkPill(Camera cam, Cell cell, Player player) {
        float x0 = cam.eye.x;
        float z0 = cam.eye.z;
        float x1 = cell.pill.posX;
        float z1 = cell.pill.posZ;
        if (Math.pow(x0 - x1, 2) + Math.pow(z1 - z0, 2) <= Math.pow((body - 0.06f), 2)) {
            cell.pill = null;
            player.score.addThing();
            for (Cell[] row: this.cell){
                for (Cell c : row){
                    c.randomize();
                }

            }
            numDoors = 20;
            doors = new Door[numDoors];
            random = new Random();
            for (int i = 0; i < numDoors; i++) {
                int x = random.nextInt(20);
                int z = random.nextInt(20);
                if (this.cell[x][z].northWall == null && !this.cell[x][z].door) {
                    doors[i] = new Door(this.cell[x][z]);
                    this.cell[x][z].door = true;
                }
            }
        }

    }

    //Going off board is lethal
    public void outOfBoard(Player player) {
        Camera cam = player.camera;
        if (cam.eye.x < -1.6f || cam.eye.x > 21.6f || cam.eye.z < -1.6f || cam.eye.z > 21.6f) {
            score = 0;
            player.flipAlive();
        }
    }
}
