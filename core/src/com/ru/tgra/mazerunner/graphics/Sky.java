package com.ru.tgra.mazerunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.mazerunner.graphics.shapes.SkyGraphic;
import com.ru.tgra.mazerunner.graphics.shapes.SphereGraphic;

/**
 * Created by andri on 11/1/15.
 */
public class Sky {
    private Texture sky;
    private Texture moon;
    private float x, y, z;
    public ModelMatrix skyOrien;
    public ModelMatrix moonOrien;

    public Sky(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        sky =   new Texture(Gdx.files.internal("textures/space5.jpg"));
        moon =   new Texture(Gdx.files.internal("textures/phobos2k.png"));
        skyOrien = new ModelMatrix();
        skyOrien.loadIdentityMatrix();
        moonOrien = new ModelMatrix();
        moonOrien.loadIdentityMatrix();
        skyOrien.addTransformation(skyOrien.matrix);
        moonOrien.addTransformation(moonOrien.matrix);
        skyOrien.addTranslation(x, y, z);
        skyOrien.addRotationZ(90.0f);
        skyOrien.addRotationX(90.0f);
        skyOrien.addScale(35.0f, 28.0f, 35.0f);
    }

    public void draw(Shader shader, float deltatime){
        updateLights(shader, deltatime);
        drawSky(shader, deltatime);
        drawMoon(shader, deltatime);
    }

    private void drawSky(Shader shader, float deltatime){
        ModelMatrix.main.pushMatrix();
        skyOrien.addRotationX(deltatime * 0.001f);
        ModelMatrix.main.addTransformation(skyOrien.matrix);
//        shader.setMaterialEmission(0.1f, 0.1f, 0.1f, 1);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SkyGraphic.drawSolidSphere(shader, sky);
        ModelMatrix.main.popMatrix();
    }


    private void drawMoon(Shader shader, float deltatime){
        moonOrien.addRotationX(deltatime * 0.001f);
        shader.setMaterialEmission(0.6f, 0.6f, 0.5f, 1);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTransformation(moonOrien.matrix);
        ModelMatrix.main.addTranslation(-20, 20, 5);
        ModelMatrix.main.addScale(3.0f, 3.3f, 3.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere(shader, moon);
        shader.setMaterialEmission(0, 0, 0, 1);
        ModelMatrix.main.popMatrix();

    }

    private void updateLights(Shader shader, float deltatime){

    }
}
