package com.ru.tgra.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;

public class Cell {
	public Wall northWall;
	public Wall eastWall;
	public boolean door = false;
	public Pill pill;
	float wallWidth= 0.1f;
	int row;
	int col;
	public Random random = new Random();
	
	public Cell(int i, int j)
	{
		row = i;
		col = j;
		if(random.nextBoolean()) northWall = new Wall(i+1.0f, j+0.5f, 1.1f, 0.1f);
		if(random.nextBoolean()) eastWall = new Wall(i+0.5f, j+1.0f, 0.1f, 1.1f);
		pill = null;
	}
	
	public void draw()
	{
		if (northWall != null)
		{
			ModelMatrix.main.pushMatrix();
			
			ModelMatrix.main.addTranslation(1.0f, 0, 0.5f);
			ModelMatrix.main.addScale(wallWidth, 1.0f, 1.1f);
			ModelMatrix.main.setShaderMatrix();
			BoxGraphic.drawSolidCube();
			
			ModelMatrix.main.popMatrix();
		}
		if (eastWall != null)
		{
			ModelMatrix.main.pushMatrix();
			
			ModelMatrix.main.addTranslation(0.5f, 0, 1.0f);
			ModelMatrix.main.addScale(1.1f, 1.0f, wallWidth);
			ModelMatrix.main.setShaderMatrix();
			BoxGraphic.drawSolidCube();
			
			ModelMatrix.main.popMatrix();
		}
	}
}
