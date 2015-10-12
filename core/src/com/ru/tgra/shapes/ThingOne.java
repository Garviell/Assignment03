package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class ThingOne {
	public ModelMatrix orientation;
	
	public ThingOne()
	{
		this.orientation = new ModelMatrix();
		this.orientation.loadIdentityMatrix();
		this.orientation.addTransformation(orientation.matrix);
		this.orientation.addTranslation(10, 4, 10);
	}
	
	public void display(int colorLoc,float deltaTime)
	{

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(orientation.matrix);

		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
		
		ModelMatrix.main.pushMatrix();
		Gdx.gl.glUniform4f(colorLoc, 0, 0.9f, 0, 1.0f);
		orientation.addRotationY(0.3f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.setShaderMatrix();
		SphereGraphic.drawOutlineSphere();
		ModelMatrix.main.popMatrix();


		core(colorLoc, deltaTime);
		moon(colorLoc, deltaTime, 1.8f, 0, 0);
		moon(colorLoc, deltaTime, 0, 0, 1.8f);
		moon(colorLoc, deltaTime, -1.1f, 0, -1.1f);
		
		
		
		
		
		ModelMatrix.main.popMatrix();
		
	}
	
	public void core(int colorLoc, float deltaTime)
	{
		ModelMatrix.main.pushMatrix();

		Gdx.gl.glUniform4f(colorLoc, 1.0f, 0, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(0, 0.5f, 0);
		orientation.addRotationX(-0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		
		ModelMatrix.main.popMatrix();
	}
	
	public void moon(int colorLoc, float deltaTime, float x, float y, float z)
	{
		ModelMatrix.main.pushMatrix();
		Gdx.gl.glUniform4f(colorLoc, 0, 0, 1.0f, 1.0f);
		orientation.addRotationY(0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addTranslation(x, y, z);
		ModelMatrix.main.addScale(0.4f, 0.4f, 0.4f);
		ModelMatrix.main.setShaderMatrix();
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
}
