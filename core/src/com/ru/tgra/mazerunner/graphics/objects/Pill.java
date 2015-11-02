package com.ru.tgra.mazerunner.graphics.objects;

import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.graphics.shapes.SphereGraphic;

public class Pill {
	public ModelMatrix orientation;
	public float posX;
	public float posZ;
    public float body;
    public boolean player1;
	
	public Pill(float posX, float posZ, boolean player1)
	{
		this.posX = posX;
		this.posZ = posZ;
		this.orientation = new ModelMatrix();
		this.orientation.loadIdentityMatrix();
		this.orientation.addTransformation(orientation.matrix);
        this.player1 = player1;
        body = 0.1f;
	}
	
	public void display(Shader shader, float deltaTime, boolean player1)
	{
		if (player1) {
            shader.setMaterialDiffuse(0, 0.35f, 0.6f, 1.0f);
        } else {
            shader.setMaterialDiffuse(0.6f, 0.35f, 0.0f, 1.0f);
        }
        shader.setMaterialSpecular(0.4f, 0.4f, 0.4f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.01f, 0.01f, 1.0f);
        shader.setShininess(130);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(posX, -0.1f, posZ);
		ModelMatrix.main.addScale(1.0f, 3.0f, 1.0f);
		ModelMatrix.main.addScale(0.03f, 0.03f, 0.03f);
		orientation.addRotationY(0.3f * deltaTime);
		ModelMatrix.main.addTransformation(orientation.matrix);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, null);
        if (player1) {
            shader.setMaterialDiffuse(0, 0.6f, 0.8f, 1.0f);
        } else {
            shader.setMaterialDiffuse(0.8f, 0.6f, 0.0f, 1.0f);
        }
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setShininess(30);
		ModelMatrix.main.addScale(1.01f, 1.01f, 1.01f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawOutlineSphere(shader);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1.0f);
		ModelMatrix.main.popMatrix();
	}

}
