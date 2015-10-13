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

	public void draw(Shader shader){
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
        shader.setMaterialDiffuse(0.5f, 0.5f, 0.7f, 1.0f);
        shader.setShininess(2000);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, 0, posZ);
        ModelMatrix.main.addScale(height, 1.0f, width);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}
