package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

import java.util.Random;

public class DeadlyFloor {
    private float posX;
    private float posZ;
    private float color;
    private float deathAt;
    private float changeRate;
    private boolean fading;

    public DeadlyFloor(float deathAt, float changeRate) {
        Random rand = new Random();
        this.deathAt = deathAt;
        this.changeRate = changeRate;
        color = rand.nextFloat();
        fading = rand.nextBoolean();
        posX = 0.5f;
        posZ = 0.5f;
    }

    private void update(float deltatime){
        if(fading){
            color -= (deltatime * changeRate);
        } else {
            color += (deltatime * changeRate);
        }
        if (color < 0 || color > 1.0){
            fading = !fading;
        }
    }

    public void display(Shader shader, float deltatime) {
        update(deltatime);
        shader.setColor(Math.max(1 * color, 0.333333f), 0.333333f * (1-color), 0.333333f * (1-color), color);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, -0.5f, posZ);
        ModelMatrix.main.addScale(1.0f, 0.02f, 1.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }

    public boolean isDeadly(){
        return color >= deathAt;
    }

}
