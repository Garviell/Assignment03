package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class DeadlyFloor {
	public float posX = 0.5f;
	public float posZ = 0.5f;
	public float color;
	float k;
	
	public DeadlyFloor(Cell cell)
	{
		color = 0.1f;
		k = 0.0001f;
	}
	
	public void display(int colorLoc)
	{
		if(color == 1.1f) { k = -0.0001f; }
		if(color == 0.1f) { k = 0.0001f; }
		
		color += k;
		color = 1.0f;
		
		Gdx.gl.glUniform4f(colorLoc, color, 0.333333f, 0.333333f, 1.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(posX, -0.5f, posZ);
		ModelMatrix.main.addScale(1.0f, 0.02f, 1.0f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
	
}
