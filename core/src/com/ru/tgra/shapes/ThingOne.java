package com.ru.tgra.shapes;


public class ThingOne {
	public ModelMatrix orientation;
	
	public ThingOne()
	{
		this.orientation = new ModelMatrix();
		this.orientation.loadIdentityMatrix();
		this.orientation.addTransformation(orientation.matrix);
		this.orientation.addTranslation(10, 4, 10);
	}
	
	public void display(Shader shader, float deltaTime)
	{

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTransformation(orientation.matrix);

		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
		
		ModelMatrix.main.pushMatrix();
        shader.setColor(0, 0.9f, 0, 1.0f);
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

        shader.setColor(1.0f, 0, 1.0f, 1.0f);
		ModelMatrix.main.addTranslation(0, 0.5f, 0);
		orientation.addRotationX(-0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addScale(0.3f, 0.3f, 0.3f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		
		ModelMatrix.main.popMatrix();
	}
	
	public void moon(Shader shader, float deltaTime, float x, float y, float z)
	{
		ModelMatrix.main.pushMatrix();
        shader.setColor(0, 0, 1.0f, 1.0f);
		orientation.addRotationY(0.1f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
		ModelMatrix.main.addTranslation(x, y, z);
		ModelMatrix.main.addScale(0.4f, 0.4f, 0.4f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
	}
}
