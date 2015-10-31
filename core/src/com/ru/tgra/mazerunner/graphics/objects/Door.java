package com.ru.tgra.mazerunner.graphics.objects;

import com.ru.tgra.mazerunner.graphics.ModelMatrix;
import com.ru.tgra.mazerunner.graphics.Shader;
import com.ru.tgra.mazerunner.logic.Camera;
import com.ru.tgra.mazerunner.graphics.shapes.BoxGraphic;

public class Door {
	public float posX;
	public float posZ;
	public float scale;
	public float add;
	public float endX;
	public float endZ;
    private float c;
	float distanceX = Float.MAX_VALUE;
	float distanceZ = Float.MAX_VALUE;
	boolean left;
	float widthX = 1.0f;
	float widthZ;
	float body = 0.2f;
	
	public Door(int x, int z)
	{
		posX = x + 0.5f;
		posZ = z + 0.05f;
		left = true;
		add = 0.1f;

	}
	
	public void update(float deltaTime)
	{
        c = 0.15f * deltaTime;
		if(add < 0.89f && left)
		{
			add += c;
			posZ += c / 2;
		}
		else if(add > 0.15f && !left)
		{
			add -= c;
			posZ -= c / 2;
		}
		else if(add >= 0.89f && left)
		{
			left = false;
		}
		else if(add <= 0.15f && !left)
		{
			left = true;
		}

		if(left)
		{
			scale += c;
		}
		else 
		{
			scale -= c;
		}
		
		endZ = posZ - c + (add/2);
		widthZ = 2 * (endZ - posZ);
		
	}
	
	public void display(Shader shader)
	{
        shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
        shader.setMaterialDiffuse(0.5f, 0.5f, 0.7f, 1.0f);
        shader.setShininess(2000);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(posX, 0, posZ);
		ModelMatrix.main.addScale(widthX, 1.0f, scale);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		ModelMatrix.main.popMatrix();
	}
	
	public boolean intersects(Camera cam)
	{
		distanceX = Math.abs(cam.eye.x - posX);
		distanceZ = Math.abs(cam.eye.z - posZ);

	    if (distanceX > (widthX /2 + body)) return false;
	    if (distanceZ > (widthZ /2 + body)) return false;
	    float checkX = distanceX;
	    float checkZ = distanceZ;

    	distanceX = (widthX /2 + body) - distanceX;
    	distanceZ = (widthZ /2 + body) - distanceZ;

        return checkZ <= (widthZ / 2 + body) || checkX <= (widthX /2 + body);
		/*
	    if (checkX <= (widthX/2 + body))
	    {
	    	return true;
	    }
		return checkZ <= (widthZ / 2 + body);*/

	}

    //Door that kills you,  pretty basic
    public void collision(Player player) {
        float distanceZ = Math.abs(player.camera.eye.z - endZ);

        if (distanceZ < distanceX) {
            if (widthZ > 0.4f && distanceZ <= 0.21f) {
                player.score.numScore = 0;
                player.flipAlive();
            } else {
                if (player.camera.eye.z < posZ) {
                    player.camera.eye.z = posZ - widthZ / 2 - body;
                } else {
                    player.camera.eye.z = posZ + widthZ / 2 + body;
                }
            }
        } else {
            if (player.camera.eye.x < posX) {
                player.camera.eye.x = posX - widthX / 2 - body;
            } else {
                player.camera.eye.x = posX + widthX / 2 + body;
            }
        }

    }
	
	
}
