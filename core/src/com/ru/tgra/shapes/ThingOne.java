package com.ru.tgra.shapes;

import java.util.Random;

public class ThingOne {
	public ModelMatrix orientation;
	private Random random = new Random();
	private float posX;
	private float posY;
	private float posZ;
	
	public ThingOne()
	{
		posX = (random.nextFloat() * 25.0f);
		posY = (random.nextFloat() * 15.0f) + 3.0f;
		posZ = (random.nextFloat() * 15.0f) + 3.0f;
		
		this.orientation = new ModelMatrix();
		this.orientation.loadIdentityMatrix();
		this.orientation.addTransformation(orientation.matrix);
		this.orientation.addTranslation(posX, posY, posZ);
	}
	
	public void display(Shader shader, float deltaTime)
	{
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(orientation.matrix);

		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
		
		ModelMatrix.main.pushMatrix();
        shader.setMaterialDiffuse(0, 0.9f, 0, 1.0f);
		orientation.addRotationY(0.3f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawOutlineSphere();
		ModelMatrix.main.popMatrix();


		core(shader, deltaTime);
		moon(shader, deltaTime, 1.8f, 0, 0);
		moon(shader, deltaTime, 0, 0, 1.8f);
		moon(shader, deltaTime, -1.1f, 0, -1.1f);
		
		
		
		
		
		ModelMatrix.main.popMatrix();
		
	}
	
	public void core(Shader shader, float deltaTime)
	{
		ModelMatrix.main.pushMatrix();

//        shader.setMaterialDiffuse(1.0f, 0.8f, 0.8f, 1.0f);
//        shader.setMaterialEmission(1.0f, 1.0f, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(0, 0.5f, 0);
		orientation.addRotationX(-0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
        shader.setLightDiffuse(1.0f, 0.6f, 0.6f, 1.0f);
//        shader.setLightPosition(orientation.getA().x*4 + 5, orientation.getA().y + 4, orientation.getA().z*4 + 5, 1);
		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1.0f);
		BoxGraphic.drawSolidCube();

		ModelMatrix.main.popMatrix();
	}
	
	public void moon(Shader shader, float deltaTime, float x, float y, float z)
	{
		ModelMatrix.main.pushMatrix();
        shader.setMaterialDiffuse(0, 0, 1.0f, 1.0f);
		orientation.addRotationY(0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addTranslation(x, y, z);
		ModelMatrix.main.addScale(0.4f, 0.4f, 0.4f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
}
