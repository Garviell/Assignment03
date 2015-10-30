package com.ru.tgra.mazerunner.graphics.objects;

import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.shapes.BoxGraphic;

public class Wall {
	public enum Sides{south,west, north, east}
    public Sides side;
	float posX;
	float posZ;
	float widthX;
	float widthZ;
	float distanceX;
	float distanceZ;
	
	public Wall(float x, float z, int side)
	{
        switch (side){
            case 0: //south
                this.posX = x;
                this.posZ = z + 0.5f;
                this.widthX = 0.1f;
                this.widthZ = 1.1f;
                break;
            case 1: //west
                this.posX = x + 0.5f;
                this.posZ = z;
                this.widthX =1.1f;
                this.widthZ =0.1f;
                break;
            case 2: //north
                this.posX = x + 1.0f;
                this.posZ = z + 0.5f;
                this.widthX = 0.1f;
                this.widthZ = 1.1f;
                break;
            case 3: //east
                this.posX = x + 0.5f;
                this.posZ = z + 1.0f;
                this.widthX = 1.1f;
                this.widthZ = 0.1f;
                break;
        }
		this.distanceX = Float.MAX_VALUE;
		this.distanceZ = Float.MAX_VALUE;
	}


	public void draw(Shader shader){
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
        shader.setMaterialDiffuse(0.5f, 0.5f, 0.7f, 1.0f);
        shader.setShininess(2000);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, 0, posZ);
        ModelMatrix.main.addScale(widthX, 1.0f, widthZ);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube(shader);
        ModelMatrix.main.popMatrix();
    }

    public boolean intersects(Player player){
        distanceX = Math.abs(player.camera.eye.x - posX);
        distanceZ = Math.abs(player.camera.eye.z - posZ);
        float buffer = 0.01f;

        if (distanceX > (widthX / 2 + player.body - buffer) ||
                distanceZ > (widthZ / 2 + player.body - buffer)) return false;
        buffer = 0.11f;

        distanceX = distanceX - (widthX / 2 + player.body - buffer);
        distanceZ = distanceZ - (widthZ / 2 + player.body - buffer);

        return distanceX <= 0 || distanceZ <= 0;

        // Delete later if no trouble
        /*return distanceX <= (widthX / 2 + player.body) ||
                distanceZ <= (widthZ / 2 + player.body);*/
    }
}
