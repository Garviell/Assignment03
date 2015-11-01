package com.ru.tgra.mazerunner;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.Sky;
import com.ru.tgra.mazerunner.graphics.objects.Player;
import com.ru.tgra.mazerunner.graphics.objects.g3djmodel.MeshModel;
import com.ru.tgra.mazerunner.graphics.shapes.*;
import com.ru.tgra.mazerunner.graphics.DFSMaze;
import com.ru.tgra.mazerunner.logic.Camera;
import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.util.Point3D;
import com.ru.tgra.mazerunner.util.Vector3D;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {

    private Player player;
    private Camera orthoCam;
    private Shader shader;
    private boolean fullScreen;
    private Texture floor;
    private Texture space;
    private Texture moon;
    private Sky sky;

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
        floor =   new Texture(Gdx.files.internal("textures/conc4.jpg"));
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
        player = new Player(fov);
        orthoCam = new Camera();
        orthoCam.orthographicProjection(-4, 4, -4, 4, 3.0f, 100);

        maze = new DFSMaze(10, 10);


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
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
//            maze.reset();
//            maze.moreDeath();
//        }

    }

    private void update() {
        if (player.isAlive()) {
            deltaTime = Gdx.graphics.getDeltaTime();

            angle += 180.0f * deltaTime;
            player.update(deltaTime);
            maze.update(player);
//            maze.checkCollision(player, deltaTime);

        } else {
            player.flipAlive();
            loseThings();
            shader.setModelMatrix(ModelMatrix.main.getMatrix());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void display() {
        if (player.isAlive()) {
            //do all actual drawing and rendering here
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            for (int viewNum = 0; viewNum < 2; viewNum++) {
                Camera camera = player.camera;
                player.display(shader);
                sky.draw(shader, deltaTime);
                if (viewNum == 0) {

                    player.score.display(shader, deltaTime);
                    drawFloor();

                } else {
                    shader.setLightDiffuse(1.0f, 0.9f, 1.0f, 0.0f, 0);
                    shader.setLightPosition(camera.eye.x, 10.0f, camera.eye.z, 0.0f, 0);
                    shader.setLightDirection(0, -1, 0, 1, 0);
                    shader.setFocus(1, 0);
                    shader.setEyePosition(camera.eye.x, 20.f, camera.eye.z, 1.0f);
                    Gdx.gl.glViewport(0, 0, 200, 200);

                    orthoCam.look(new Point3D(camera.eye.x, 35.0f, camera.eye.z), camera.eye, new Vector3D(0, 0, -1));
                    shader.setViewMatrix(orthoCam.getViewMatrix());
                    shader.setProjectionMatrix(orthoCam.getProjectionMatrix());
                    drawFloor();
                }

                shader.setShininess(10);
                ModelMatrix.main.loadIdentityMatrix();

                shader.setMaterialDiffuse(1.0f, 0, 0, 1.0f);
                maze.display(shader, deltaTime);


                player.score.display(shader, deltaTime);

                // Mini-map
                if (viewNum == 1) {
                    player.displayMap(shader);
                }
            }
        } else {
            player.camera.look(new Point3D(0.5f, 0.08f, 0.5f), new Point3D(1.5f,0.0f,0.5f), new Vector3D(0, 0.8f, 0));
            player.camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
            shader.setViewMatrix(player.camera.getViewMatrix());
            shader.setProjectionMatrix(player.camera.getProjectionMatrix());
            Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }
    }

    @Override
    public void render() {
        input();
        //put the code inside the update and display methods, depending on the nature of the code
        update();
        display();

    }

    public void drawFloor() {
        shader.setMaterialDiffuse(0.13333f, 0.133333f, 0.183333f, 1.0f);
        shader.setMaterialSpecular(0.013333f, 0.013333f, 0.133333f, 1.0f);
        shader.setShininess(30);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(10.0f, -0.5f, 10.0f);
        ModelMatrix.main.addScale(23.0f, 0.01f, 23.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube(shader, floor, null);
        ModelMatrix.main.popMatrix();
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

    public void loseThings() {
        for (int i = 0; i < thingsLostWhenDeathOccurs; i++) {
            if (player.score.numScore > 0) {
                player.score.removething();
            }
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