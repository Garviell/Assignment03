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
	float distanceX = Float.MAX_VALUE;
	float distanceZ = Float.MAX_VALUE;
	boolean left;
	float height = 0.1f;
	float width;
	float body = 0.2f;
	
//	public Door(Cell cell)
//	{
//		posX = cell.row + 1.0f;
//		posZ = cell.col + 0.05f;
//		left = true;
//		add = 0.1f;
//	}
	
	public void update(float deltaTime)
	{
		if(add < 0.89f && left)
		{
			add += 0.005f;
			posZ += 0.0025f;
		}
		else if(add > 0.15f && !left)
		{
			add -= 0.005f;
			posZ -= 0.0025f;
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
			scale += 0.005f;
		}
		else 
		{
			scale -= 0.005f;
		}
		
		endZ = posZ - 0.05f + (add/2);
		width = 2 * (endZ - posZ);
		
	}
	
	public void display(Shader shader)
	{
        shader.setMaterialDiffuse(1.0f, 0, 0, 1.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(posX, 0, posZ);
		ModelMatrix.main.addScale(0.1f, 1.0f, scale);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		ModelMatrix.main.popMatrix();
	}
	
	public boolean intersects(Camera cam)
	{
		distanceX = Math.abs(cam.eye.x - posX);
		distanceZ = Math.abs(cam.eye.z - posZ);

	    if (distanceX > (height/2 + body)) return false;
	    if (distanceZ > (width/2 + body)) return false;
	    float checkX = distanceX;
	    float checkZ = distanceZ;

    	distanceX = (height/2 + body) - distanceX;
    	distanceZ = (width/2 + body) - distanceZ;

	    if (checkX <= (height/2 + body)) 
	    {
	    	return true;
	    }
		return checkZ <= (width / 2 + body);

	}
	
	
}
