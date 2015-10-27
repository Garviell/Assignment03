package com.ru.tgra.shapes;

import java.awt.geom.Point2D;

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
                this.posX = x - 0.5f;
                this.posZ = z;
                this.widthX = 1;
                this.widthZ = 0.1f;
                break;
            case 1: //west
                this.posX = x;
                this.posZ = z - 0.5f;
                this.widthX =0.1f;
                this.widthZ =1;
                break;
            case 2: //north
                this.posX = x + 0.5f;
                this.posZ = z;
                this.widthX = 1;
                this.widthZ = 0.1f;
                break;
            case 3: //east
                this.posX = x;
                this.posZ = z + 0.5f;
                this.widthX = 0.1f;
                this.widthZ = 1;
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
        ModelMatrix.main.addScale(widthZ, 1.0f, widthX);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}
