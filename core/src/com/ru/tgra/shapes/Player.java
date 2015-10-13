package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
    public Camera camera;
    private boolean alive;
    private float fov;

    Player(int viewMatrixLoc, int projectionMatrixLoc, float fov){
        camera = new Camera(viewMatrixLoc, projectionMatrixLoc);
        camera.look(new Point3D(-1.0f, 0.08f, -1.0f), new Point3D(0,0.0f,0), new Vector3D(0,0.8f,0));
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        this.fov = fov;
        alive = true;
    }


    public boolean isAlive(){
        return alive;
    }

    public void flipAlive(){
        alive = !alive;
    }

    private void changeFov(float fov, float deltaTime){
        this.fov += fov * deltaTime;
    }

    public float getFov(){
        return fov;
    }

    public void update(float deltaTime){
        //do all updates to the game

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.yaw(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.yaw(90.0f * deltaTime);
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
            camera.slide(0, 0, -2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.slide(0, 0, 2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            camera.slide(0, 3.0f * deltaTime, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F)) {
            camera.slide(0, -3.0f * deltaTime, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.roll(-45.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.roll(45.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            changeFov(-30.0f, deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.G)) {
            changeFov(30.0f, deltaTime);
        }

    }

    public void display(){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        camera.setShaderMatrices();
    }

    public void displayMap(int colorLoc){
        Gdx.gl.glUniform4f(colorLoc, 1.0f, 0.3f, 0.1f, 1.0f);

        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(camera.eye.x, camera.eye.y, camera.eye.z);
        ModelMatrix.main.addScale(0.25f, 0.25f, 0.25f);
        ModelMatrix.main.setShaderMatrix();
        SphereGraphic.drawSolidSphere();
        ModelMatrix.main.addTranslation(camera.v.x, camera.v.y, camera.v.z);
        ModelMatrix.main.addScale(0.55f, 0.55f, 0.55f);
        ModelMatrix.main.setShaderMatrix();
        SphereGraphic.drawSolidSphere();
        ModelMatrix.main.popMatrix();

    }
}
