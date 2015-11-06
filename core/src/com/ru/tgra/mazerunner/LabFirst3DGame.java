package com.ru.tgra.mazerunner;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.Sky;
import com.ru.tgra.mazerunner.graphics.objects.Pill;
import com.ru.tgra.mazerunner.graphics.objects.Player;
import com.ru.tgra.mazerunner.graphics.objects.g3djmodel.MeshModel;
import com.ru.tgra.mazerunner.graphics.shapes.*;
import com.ru.tgra.mazerunner.graphics.DFSMaze;
import com.ru.tgra.mazerunner.logic.Camera;
import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.util.Point3D;
import com.ru.tgra.mazerunner.util.Vector3D;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.bind.v2.TODO;
import java.util.Random;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {

    private Player player1;
    private Player player2;
    private Pill pill1;
    private Pill pill2;
    private Shader shader;
    private boolean fullScreen;
    private Texture floor;
    private Texture space;
    private Texture moon;
    private Sky sky;
    private Random rand = new Random();
    private int sizeX = 10;
    private int sizeZ = 10;

//    private Maze maze;
    private DFSMaze maze;

    private float deltaTime;
    private float angle;
    private float fov = 80.0f;
    private int thingsLostWhenDeathOccurs = 2;

    @Override
    public void create() {
        fullScreen = false;
        Graphics.DisplayMode disp = Gdx.graphics.getDesktopDisplayMode();
//        Gdx.graphics.setDisplayMode(disp.width, disp.height, true);
        Gdx.graphics.setDisplayMode(1280, 1024, true);
        shader = new Shader();
        floor =   new Texture(Gdx.files.internal("textures/conc3.jpg"));
        moon =   new Texture(Gdx.files.internal("textures/phobos2k.png"));
        space =   new Texture(Gdx.files.internal("textures/space5.jpg"));
        sky = new Sky(5, -5, 5);

        Gdx.input.setInputProcessor(this);

        //COLOR IS SET HERE
        shader.setMaterialDiffuse(0.7f, 0.2f, 0, 1);

        BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
        SphereGraphic.create();
        SkyGraphic.create();
        SincGraphic.create(shader.getVertexPointer());
        CoordFrameGraphic.create(shader.getVertexPointer());

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        ModelMatrix.main = new ModelMatrix();
        ModelMatrix.main.loadIdentityMatrix();
        shader.setModelMatrix(ModelMatrix.main.getMatrix());

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        //Camera
        player1 = new Player(fov, new Point3D(0.5f, 0.07f, 0.5f), new Point3D(1.5f,0.0f,0.5f), sizeX, sizeZ, true);
        player2 = new Player(fov, new Point3D(0.5f, 0.07f, 1.5f), new Point3D(2.5f,0.0f,1.5f), sizeX, sizeZ, false);
        pill1 = new Pill(1.5f, 0.5f, true);
        pill2 = new Pill(1.5f, 1.5f, false);

        maze = new DFSMaze(sizeX, sizeZ);


        shader.setGlobalAmbient(0.0f, 0.0f, 0.0f, 1.0f);
        shader.setConstantAtt(0.1f, 0);
        shader.setLinearAtt(0.15f, 0);
        shader.setQuadraticAtt(0.0002f, 0);

        shader.setConstantAtt(0.05f, 1);
        shader.setLinearAtt(0.10f, 1);
        shader.setQuadraticAtt(0.0000f, 1);
        shader.setFocus(7, 1);

        for (int i = 2; i < 7; i++){
            shader.setConstantAtt(0.5f, i);
            shader.setLinearAtt(0.01f, i);
            shader.setQuadraticAtt(0.0010f, i);
            shader.setFocus(1,i);
        }

        shader.setLightDiffuse(0.8f, 0.8f, 0.8f, 1.0f, 0);
        shader.setLightPosition(-15, 20, 1, 1, 0);
        shader.setLightDirection(15.2f, -10, 5, 1, 0);
        shader.setFocus(1, 0);

        shader.setLightDiffuse(0.8f, 0.8f, 0.8f, 0.8f, 2);
        shader.setLightPosition(-50, 25, 5, 1, 2);
        shader.setLightDirection(15.2f, -3, 5, 1, 2);

        shader.setLightDiffuse(0.8f, 0.8f, 0.8f, 0.8f, 6);
        shader.setLightPosition(75, 25, 5, 1, 6);
        shader.setLightDirection(-15.2f, -3, 5, 1, 6);

        shader.setLightDiffuse(0.8f, 0.8f, 0.8f, 1.0f, 3);
        shader.setLightPosition(5, 25, -50, 1, 3);
        shader.setLightDirection(5, 3, 15.2f, 1, 3);

        shader.setLightDiffuse(0.8f, 0.8f, 0.8f, 0.8f, 4);
        shader.setLightPosition(5, 30, 75, 1, 4);
        shader.setLightDirection(5, -3, -15.2f, 1, 4);

        shader.setLightDiffuse(0.8f, 0.8f, 0.8f, 0.8f, 5);
        shader.setLightPosition(5, 70, 5, 1, 5);
        shader.setLightDirection(0, -1, 0, 1, 5);

    }

    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.graphics.setDisplayMode(500, 500, false);
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                int width, height;
                if (fullScreen) {
                    width = 1280;
                    height = 1024;
                } else {
                    Graphics.DisplayMode disp = Gdx.graphics.getDesktopDisplayMode();
                    width = disp.width;
                    height = disp.height;
                }
                fullScreen = !fullScreen;
                Gdx.graphics.setDisplayMode(width, height, fullScreen);
            }
        }

        controls();

    }

    private void update() {
        //player2.intersectWithPlayer(player1);
        if (!player1.score.win && !player2.score.win) {
            update(player1);
            update(player2);
            foundApill();
        }
        else {
            endGame();
        }
        //player1.intersectWithPlayer(player2);
    }

    private void update(Player player) {
        //player1.setViweForMap(0, 0, 200, 200);
        //player2.setViweForMap(Gdx.graphics.getWidth() - 400, 0, Gdx.graphics.getWidth() - 200, 200);
        if (player.isAlive()) {
            deltaTime = Gdx.graphics.getDeltaTime();

            angle += 180.0f * deltaTime;
            player.update(deltaTime);
            maze.update(player);

        } else {
            isDead(player, player.count);
        }
    }

    private void isDead(Player player, float count) {
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        if (count < 4) {
            player.count += deltaTime;
        }
        else {
            player.count = 0;
            loseThings(player);
            player.flipAlive();
        }
    }

    private void display() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        player1.display(shader, 0, Gdx.graphics.getWidth() / 2, deltaTime, maze, pill1, pill2);
        sky.draw(shader, deltaTime);
        player2.display(shader, Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth(), deltaTime, maze, pill1, pill2);
        sky.draw(shader, deltaTime);
        if (player1.isAlive()) { player1.displayOtherPlayer(shader, player2); }
        if (player2.isAlive()) { player2.displayOtherPlayer(shader, player1); }
    }

    @Override
    public void render() {
        input();
        update();
        display();

    }

    public void drawCeiling() {
        shader.setMaterialDiffuse(0.5f, 0.3f, 1.0f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(10.0f, 0.5f, 10.0f);
        ModelMatrix.main.addScale(20.2f, 0.1f, 20.2f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube(shader, null, null);
        ModelMatrix.main.popMatrix();
    }

    public void loseThings(Player player) {
        for (int i = 0; i < thingsLostWhenDeathOccurs; i++) {
            if (player.score.numScore > 0) {
                player.score.removething();
            }
        }
    }

    public void controls() {
        // Controls for player 1
        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            player1.camera.rotateY(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            player1.camera.rotateY(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.V)) {
            player1.camera.pitch(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)) {
            player1.camera.pitch(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            player1.camera.slide(-2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            player1.camera.slide(2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            player1.camera.walk(2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            player1.camera.walk(-2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (player1.camera.eye.y == 0.07f) {
                player1.jump = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            player1.changeFov(-20.0f, deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.G)) {
            player1.changeFov(20.0f, deltaTime);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            player1.flashlight = !player1.flashlight;
        }

        // Controls for player 2
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player2.camera.rotateY(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player2.camera.rotateY(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player2.camera.pitch(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player2.camera.pitch(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.H)) {
            player2.camera.slide(-2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)) {
            player2.camera.slide(2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.U)) {
            player2.camera.walk(2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)) {
            player2.camera.walk(-2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            if (player2.camera.eye.y == 0.07f) {
                player2.jump = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
            player2.changeFov(-20.0f, deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            player2.changeFov(20.0f, deltaTime);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            player2.flashlight = !player2.

                    flashlight;
        }
    }

    private void foundApill() {
        if (pillIntersect(player1, pill1)) {
            player1.score.addThing();
            pill1.posX = rand.nextInt(sizeX) + 0.5f;
            pill1.posZ = rand.nextInt(sizeZ) + 0.5f;
        }
        if (pillIntersect(player1, pill2)) {
            player2.score.removething();
            pill2.posX = rand.nextInt(sizeX) + 0.5f;
            pill2.posZ = rand.nextInt(sizeZ) + 0.5f;
        }
        if (pillIntersect(player2, pill2)) {
            player2.score.addThing();
            pill2.posX = rand.nextInt(sizeX) + 0.5f;
            pill2.posZ = rand.nextInt(sizeZ) + 0.5f;
        }
        if (pillIntersect(player2, pill1)) {
            player1.score.removething();
            pill1.posX = rand.nextInt(sizeX) + 0.5f;
            pill1.posZ = rand.nextInt(sizeZ) + 0.5f;
        }
    }

    private boolean pillIntersect(Player player, Pill pill) {
        return Math.abs(player.camera.eye.x - pill.posX) < player.body + pill.body
                && Math.abs(player.camera.eye.z - pill.posZ) < player.body + pill.body;
    }

    private void endGame() {
        player1.score.win = false;
        player2.score.win = false;
        player1.score.numScore = 0;
        player2.score.numScore = 0;
        player1.setPlayerHome(shader, 0, Gdx.graphics.getWidth() / 2);
        player2.setPlayerHome(shader, Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }


}