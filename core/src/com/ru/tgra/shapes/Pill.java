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
	
	public void display(Shader shader, float deltaTime)
	{

		
		ModelMatrix.main.pushMatrix();
		shader.setMaterialDiffuse(0, 0.75f, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(posX, -0.1f, posZ);
		ModelMatrix.main.addScale(1.0f, 3.0f, 1.0f);
		ModelMatrix.main.addScale(0.03f, 0.03f, 0.03f);
		orientation.addRotationY(0.3f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
        shader.setMaterialDiffuse(0, 0.6f, 0.8f, 1.0f);
		ModelMatrix.main.addScale(1.01f, 1.01f, 1.01f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawOutlineSphere();
		ModelMatrix.main.popMatrix();
	}

}
