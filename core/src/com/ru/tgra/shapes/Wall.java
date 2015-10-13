package com.ru.tgra.shapes;

import java.awt.geom.Point2D;

public class Wall {
	float posX;
	float posZ;
	float width;
	float height;
	float distanceX;
	float distanceZ;
	
	public Wall(float x, float z, float width, float height)
	{
		this.posX = x;
		this.posZ = z;
		this.width = width;
		this.height = height;
		this.distanceX = Float.MAX_VALUE;
		this.distanceZ = Float.MAX_VALUE;
	}

	public void draw(float biwi, float bihi){
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, 0, posZ);
        ModelMatrix.main.addScale(height+bihi, 1.0f, width + biwi);
        ModelMatrix.main.setShaderMatrix();
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}
