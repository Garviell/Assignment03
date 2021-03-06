package com.ru.tgra.mazerunner.graphics.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.mazerunner.graphics.DFSMaze;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.objects.g3djmodel.G3DJModelLoader;
import com.ru.tgra.mazerunner.graphics.objects.g3djmodel.MeshModel;
import com.ru.tgra.mazerunner.graphics.shapes.BoxGraphic;
import com.ru.tgra.mazerunner.graphics.shapes.SphereGraphic;
import com.ru.tgra.mazerunner.logic.Camera;
import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.logic.Score;
import com.ru.tgra.mazerunner.util.Point3D;
import com.ru.tgra.mazerunner.util.Vector3D;

public class Player {
    public Camera camera;
    public Camera orthoCam;
    private boolean alive;
    public boolean flashlight;
    private float fov;
    public int viewXstart;
    public int viewXend;
    public Point3D eye;
    public Point3D center;
    public Score score;
    public boolean player1;
    public float count;
    public float body;
    public boolean jump;
    private boolean up;
    private int jumpCount;
    private int hang;
    private MeshModel model;
    private Texture moon;
    

    public Player(float fov, Point3D eye, Point3D center, int sizeX, int sizeZ, boolean player1){
        camera = new Camera();
        camera.look(eye, center, new Vector3D(0,0.8f,0));
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        orthoCam = new Camera();
        orthoCam.orthographicProjection(-4, 4, -4, 4, 3.0f, 100);
        this.fov = fov;
        this.eye = eye;
        this.center = center;
        model = G3DJModelLoader.loadG3DJFromFile("monkeybot.g3dj");
        flashlight = false;
        alive = true;
        body = 0.2f;
        score = new Score(player1, sizeX, sizeZ);
        jump = false;
        up = true;
        jumpCount = 0;
        hang = 0;
        count = 0;
        moon =   new Texture(Gdx.files.internal("textures/phobos2k.png"));
    }


    public boolean isAlive(){
        return alive;
    }

    public void flipAlive(){
        alive = !alive;
    }

    public void changeFov(float fov, float deltaTime){
    	if(fov < 0 && this.fov > 10.0f){ this.fov += fov * deltaTime; }
    	if(fov > 0 && this.fov < 80.0f){ this.fov += fov * deltaTime; }
    }

    public float getFov(){
        return fov;
    }

    public void update(float deltaTime){
        //do all updates to the game

        if (jump) {
            if (up) {
                if ( hang == 0) {
                    camera.jump(0, 2.0f * deltaTime, 0);
                    jumpCount++;
                }
                if (jumpCount == 10) {
                    if ( hang == 3) { up = false; }
                    camera.jump(0, 0.2f * deltaTime, 0);
                    hang++;
                }
            }
            else {
                if (hang == 0) {
                    camera.jump(0, -2.0f * deltaTime, 0);
                    jumpCount--;
                }
                else {
                    camera.jump(0, -0.2f * deltaTime, 0);
                    hang--;
                }
                if (jumpCount == 0) {
                    camera.eye.y = 0.07f;
                    jump = false;
                    up = true;
                }
            }
        }

    }

    public void display(Shader shader, int viewXstart, int viewXend, float deltaTime, DFSMaze maze, Pill pill1, Pill pill2){
        if (isAlive()) {

            //for (int viewNum = 0; viewNum < 2; viewNum++) {
                displayMoon(shader);

                //if (viewNum == 0) {
                    this.viewXstart = viewXstart;
                    this.viewXend = viewXend;
                    if (flashlight){
                        shader.setLightDiffuse(1.0f, 1.0f,1.0f,1.0f, 1);
                    } else {
                        shader.setLightDiffuse(0.0f, 0.0f,0.0f,1.0f, 1);
                    }
                    flashlight(shader);
                    Gdx.gl.glViewport(viewXstart, 0, viewXend, Gdx.graphics.getHeight());
                    shader.setEyePosition(camera.eye.x, camera.eye.y, camera.eye.z, 1.0f);
                    if(player1)camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
                    shader.setViewMatrix(camera.getViewMatrix());
                    shader.setProjectionMatrix(camera.getProjectionMatrix());

                    pill1.display(shader, deltaTime, true);
                    pill2.display(shader, deltaTime, false);
                    score.display(shader, deltaTime);
                    drawFloor(shader);

              //  }

                shader.setShininess(10);
                ModelMatrix.main.loadIdentityMatrix();

                shader.setMaterialDiffuse(1.0f, 0, 0, 1.0f);
                maze.display(shader, camera.eye.x, camera.eye.z, deltaTime);


                score.display(shader, deltaTime);
          // }
        } else {
            setPlayerHome(shader, viewXstart, viewXend);
        }

    }

    public void setPlayerHome(Shader shader, int viewXstart, int viewXend) {
        Gdx.gl.glViewport(viewXstart, 0, viewXend, Gdx.graphics.getHeight());
        camera.look(eye, center, new Vector3D(0, 0.8f, 0));
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        shader.setViewMatrix(camera.getViewMatrix());
        shader.setProjectionMatrix(camera.getProjectionMatrix());
    }

    public void intersectWithPlayer(Player otherPlayer) {
        float x = Math.abs(camera.eye.x - otherPlayer.camera.eye.x);
        float z = Math.abs(camera.eye.z - otherPlayer.camera.eye.z);
        if (intersect(otherPlayer)) {
            if (x < z) {
                if (camera.eye.x < otherPlayer.camera.eye.x) {
                    camera.eye.x = otherPlayer.camera.eye.x - (body + otherPlayer.body);
                } else {
                    camera.eye.x = otherPlayer.camera.eye.x + (body + otherPlayer.body);
                }
            }
            else {
                if (camera.eye.z < otherPlayer.camera.eye.z) {
                    camera.eye.z = otherPlayer.camera.eye.z - (body + otherPlayer.body);
                } else {
                    camera.eye.z = otherPlayer.camera.eye.z + (body + otherPlayer.body);
                }
            }
        }
    }

    public boolean intersect(Player otherPlayer) {
        float x = Math.abs(camera.eye.x - otherPlayer.camera.eye.x);
        float z = Math.abs(camera.eye.z - otherPlayer.camera.eye.z);
        return x < body + otherPlayer.body && z < body + otherPlayer.body;
    }

    public void displayOtherPlayer(Shader shader, Player otherPlayer) {
        Gdx.gl.glViewport(viewXstart, 0, viewXend, Gdx.graphics.getHeight());
        shader.setViewMatrix(camera.getViewMatrix());
        shader.setMaterialDiffuse(1.0f, 0.3f, 0.1f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(otherPlayer.camera.eye.x , otherPlayer.camera.eye.y + 1.2f, otherPlayer.camera.eye.z);
        ModelMatrix.main.addScale(0.05f, 0.05f, 0.05f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, null);

        ModelMatrix.main.popMatrix();

        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(otherPlayer.camera.eye.x , otherPlayer.camera.eye.y - 0.3f, otherPlayer.camera.eye.z);
        ModelMatrix.main.addScale(0.15f, 0.15f, 0.15f);
        ModelMatrix.main.addRotationX(5.0f);
        ModelMatrix.main.addRotationZ(90.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        model.draw(shader);
        ModelMatrix.main.popMatrix();
    }

    public void flashlight(Shader shader){
        shader.setLightPosition(camera.eye.x, camera.eye.y, camera.eye.z, 1.0f, 1);
        shader.setLightDirection(-camera.n.x, -camera.n.y, -camera.n.z, 1, 1);
    }


    public void displayMoon(Shader shader) {
        Gdx.gl.glViewport(viewXstart, 0, viewXend, Gdx.graphics.getHeight());
        shader.setViewMatrix(camera.getViewMatrix());
        shader.setLightDiffuse(0.8f, 0.8f, 1.0f, 0.0f, 0);
        shader.setMaterialDiffuse(1, 1, 1, 1);
        shader.setMaterialSpecular(0, 0, 0, 1.0f);
        shader.setLightPosition(-15, 20, 1, 1, 0);
        shader.setMaterialEmission(0.6f, 0.2f, 0.05f, 1);
        shader.setLightDirection(15.2f, -10, 5, 1, 0);
        shader.setFocus(2, 0);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(-15, 20, -4);
        ModelMatrix.main.addRotationX(40);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, moon);
        shader.setMaterialEmission(0, 0, 0, 1);
        ModelMatrix.main.popMatrix();
    }

    public void drawFloor(Shader shader) {
        shader.setMaterialDiffuse(0.13333f, 0.133333f, 0.183333f, 1.0f);
        shader.setMaterialSpecular(0.013333f, 0.013333f, 0.133333f, 1.0f);
        shader.setShininess(30);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(10.0f, -0.5f, 10.0f);
        ModelMatrix.main.addScale(23.0f, 0.01f, 23.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube(shader, null, null);
        ModelMatrix.main.popMatrix();
    }
}
