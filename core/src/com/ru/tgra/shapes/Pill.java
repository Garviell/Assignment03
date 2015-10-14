package com.ru.tgra.shapes;

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


        shader.setMaterialSpecular(0.4f, 0.4f, 0.4f, 1.0f);
        shader.setMaterialDiffuse(0, 0.35f, 0.6f, 1.0f);
        shader.setShininess(130);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(posX, -0.1f, posZ);
		ModelMatrix.main.addScale(1.0f, 3.0f, 1.0f);
		ModelMatrix.main.addScale(0.03f, 0.03f, 0.03f);
		orientation.addRotationY(0.3f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
        shader.setMaterialDiffuse(0, 0.6f, 0.8f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setShininess(30);
		ModelMatrix.main.addScale(1.01f, 1.01f, 1.01f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawOutlineSphere();
		ModelMatrix.main.popMatrix();
	}

}
