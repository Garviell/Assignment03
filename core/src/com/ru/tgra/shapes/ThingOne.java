package com.ru.tgra.shapes;

import java.util.Random;

public class ThingOne {
	public ModelMatrix orientation;
	public float posX = 10.f;
	public float posY = 5.0f;
	public float posZ = 10.f;
	private Point3D[] pos = new Point3D[16];
	
	public ThingOne()
	{
		orientation = new ModelMatrix();
		orientation.loadIdentityMatrix();
		orientation.addTransformation(orientation.matrix);
		orientation.addTranslation(posX, posY, posZ);
		
		setPos();
	}
	
	public void display(Shader shader, float deltaTime, int numScore)
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
		
		for(int i = 0; i < numScore; i++)
		{
			moon(shader, deltaTime, pos[i].x, pos[i].y, pos[i].z);
		}
		
		
		ModelMatrix.main.popMatrix();
		
	}
	
	public void core(Shader shader, float deltaTime)
	{
		ModelMatrix.main.pushMatrix();

        shader.setMaterialDiffuse(1.0f, 0, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(0, 0.5f, 0);
//		orientation.addRotationX(-0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		
		ModelMatrix.main.popMatrix();
	}
	
	public void moon(Shader shader, float deltaTime, float x, float y, float z)
	{
		ModelMatrix.main.pushMatrix();
        shader.setMaterialDiffuse(0, 0, 1.0f, 1.0f);
//		orientation.addRotationY(0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addTranslation(x, y, z);
		ModelMatrix.main.addScale(0.4f, 0.4f, 0.4f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
	
	private void setPos()
	{
		pos[0] = new Point3D(2.5f, 0, 0);
		pos[1] = new Point3D(0, 0, 2.5f);
		pos[2] = new Point3D(-1.5f, 0, -1.5f);
		pos[3] = new Point3D(-2.5f, 0, 0);
		pos[4] = new Point3D(0, 0, -2.5f);
		pos[5] = new Point3D(1.5f, 0, 1.5f);
		pos[6] = new Point3D(1.8f, 0.0f, -1.8f);
		pos[7] = new Point3D(-1.8f, 0.0f, 1.8f);
		pos[8] = new Point3D(5.0f, 0, 0);
		pos[9] = new Point3D(-5.0f, 0, 0);
		pos[10] = new Point3D(0, 0, 5.0f);
		pos[11] = new Point3D(0, 0, -5.0f);
		pos[12] = new Point3D(4.5f, 0, 4.5f);
		pos[13] = new Point3D(-4.5f, 0, -4.5f);
		pos[14] = new Point3D(4.8f, 0.0f, -4.8f);
		pos[15] = new Point3D(-4.8f, 0.0f, 4.8f);
	}
}
