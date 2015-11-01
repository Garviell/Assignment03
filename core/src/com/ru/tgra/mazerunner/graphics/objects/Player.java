package com.ru.tgra.mazerunner.graphics.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.shapes.SphereGraphic;
import com.ru.tgra.mazerunner.logic.Camera;
import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.logic.Score;
import com.ru.tgra.mazerunner.util.Point3D;
import com.ru.tgra.mazerunner.util.Vector3D;

public class Player {
    public Camera camera;
    private boolean alive;
    private boolean flashlight;
    private float fov;
    public Score score;
    public float body;
    private boolean jump;
    private boolean up;
    private int jumpCount;
    private int hang;
    

    public Player(float fov){
        camera = new Camera();
        camera.look(new Point3D(0.5f, 0.08f, 0.5f), new Point3D(1.5f,0.0f,0.5f), new Vector3D(0,0.8f,0));
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        this.fov = fov;
        flashlight = false;
        alive = true;
        body = 0.2f;
        score = new Score(camera);
        jump = false;
        up = true;
        jumpCount = 0;
        hang = 0;
    }


    public boolean isAlive(){
        return alive;
    }

    public void flipAlive(){
        alive = !alive;
    }

    private void changeFov(float fov, float deltaTime){
    	if(fov < 0 && this.fov > 10.0f){ this.fov += fov * deltaTime; }
    	if(fov > 0 && this.fov < 80.0f){ this.fov += fov * deltaTime; }
    }

    public float getFov(){
        return fov;
    }

    public void update(float deltaTime){
        //do all updates to the game

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.rotateY(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.rotateY(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.pitch(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.pitch(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.slide(-2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.slide(2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.walk(2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.walk(-2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println(camera.eye.y);
            if (camera.eye.y == 0.08f) {
                jump = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            changeFov(-20.0f, deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.G)) {
            changeFov(20.0f, deltaTime);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            flashlight = !flashlight;
        }

        if (jump) {
            if (up) {
                if ( hang == 0) {
                    camera.jump(0, 2.0f * deltaTime, 0);
                    jumpCount++;
                }
                if (jumpCount == 20) {
                    if ( hang == 5) { up = false; }
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
                    camera.eye.y = 0.08f;
                    jump = false;
                    up = true;
                }
            }
        }

    }

    public void display(Shader shader){
        if (flashlight){
            shader.setLightDiffuse(1.0f, 1.0f,1.0f,1.0f, 1);
        } else {
            shader.setLightDiffuse(0.0f, 0.0f,0.0f,1.0f, 1);
        }
        displayPlayer(shader);
        flashlight(shader);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setEyePosition(camera.eye.x, camera.eye.y, camera.eye.z, 1.0f);
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        shader.setViewMatrix(camera.getViewMatrix());
        shader.setProjectionMatrix(camera.getProjectionMatrix());
    }

    public void displayPlayer(Shader shader) {
        shader.setMaterialDiffuse(1.0f, 0.3f, 0.1f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(camera.eye.x , camera.eye.y + 0.2f, camera.eye.z);
        ModelMatrix.main.addScale(0.15f, 0.15f, 0.15f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, null);
        ModelMatrix.main.popMatrix();
    }

    public void flashlight(Shader shader){
        shader.setLightPosition(camera.eye.x, camera.eye.y, camera.eye.z, 1.0f, 1);
        shader.setLightDirection(-camera.n.x, -camera.n.y, -camera.n.z, 1, 1);
    }

    public void displayMap(Shader shader){
        shader.setMaterialDiffuse(1.0f, 0.3f, 0.1f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(camera.eye.x, camera.eye.y, camera.eye.z);
        ModelMatrix.main.addScale(0.25f, 0.25f, 0.25f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, null);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(-camera.u.x, 0, -camera.u.z);
        ModelMatrix.main.addScale(0.55f, 0.55f, 0.55f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, null);
        ModelMatrix.main.popMatrix();
        ModelMatrix.main.addTranslation(camera.u.x, 0, camera.u.z);
        ModelMatrix.main.addScale(0.55f, 0.55f, 0.55f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, null);
        ModelMatrix.main.popMatrix();

    }
}
