package com.ru.tgra.mazerunner.graphics.objects;

import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.objects.g3djmodel.G3DJModelLoader;
import com.ru.tgra.mazerunner.graphics.objects.g3djmodel.MeshModel;
import com.ru.tgra.mazerunner.graphics.shapes.BoxGraphic;

import java.util.Random;

public class DeadlyFloor {
    private float posX;
    private float posZ;
    private float color;
    private float deathAt;
    private float changeRate;
    private boolean fading;

    public DeadlyFloor(float posX, float posZ, float deathAt, float changeRate) {
        Random rand = new Random();
        this.deathAt = deathAt;
        this.changeRate = changeRate;
        color = rand.nextFloat();
        fading = rand.nextBoolean();
        this.posX = posX + 0.5f;
        this.posZ = posZ + 0.5f;
    }

    private void update(float deltatime){
        if(fading){
            color -= (deltatime * changeRate);
        } else {
            color += (deltatime * changeRate);
        }
        if (color < 0 || color > 1.0f){
            fading = !fading;
        }
        if (color < 0) { color = 0; }
        if (color > 1.0f) { color = 1.0f; }
    }

    public void display(Shader shader, float deltatime) {
        update(deltatime);
        ModelMatrix.main.pushMatrix();
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        shader.setMaterialDiffuse(Math.max(1 * color, 0.333333f), 0.333333f * (1 - color), 0.333333f * (1 - color), 1);
        shader.setMaterialSpecular(1.0f, 0.9f, 0.9f, 1.0f);
        shader.setShininess(1030);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, -0.503f, posZ);
        ModelMatrix.main.addScale(1.0f, 0.02f, 1.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube(shader, null, null);
        ModelMatrix.main.popMatrix();
    }

    public void onFloor(Player player) {
        if (color >= deathAt && player.camera.eye.y <= 0.08f) {
            player.flipAlive();
        }
    }

}
