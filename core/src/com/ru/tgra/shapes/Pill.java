package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class Pill {
	public ModelMatrix orientation;
	public float posX = 0.5f;
	public float posZ = 0.5f;
	
	public Pill(float posX, float posZ)
	{
		this.posX += posX;
		this.posZ += posZ;
		this.orientation = new ModelMatrix();
		this.orientation.loadIdentityMatrix();
		this.orientation.addTransformation(orientation.matrix);
	}
	
	public void display(int colorLoc, float deltaTime)
	{

		
		ModelMatrix.main.pushMatrix();
		Gdx.gl.glUniform4f(colorLoc, 0, 0.75f, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(0.5f, -0.1f, 0.5f);
		ModelMatrix.main.addScale(1.0f, 3.0f, 1.0f);
		ModelMatrix.main.addScale(0.03f, 0.03f, 0.03f);
		orientation.addRotationY(0.3f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.setShaderMatrix();
		SphereGraphic.drawSolidSphere();
		Gdx.gl.glUniform4f(colorLoc, 0, 0.6f, 0.8f, 1.0f);
		ModelMatrix.main.addScale(1.01f, 1.01f, 1.01f);
		ModelMatrix.main.setShaderMatrix();
		SphereGraphic.drawOutlineSphere();
		ModelMatrix.main.popMatrix();
	}

}
