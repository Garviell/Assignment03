package com.ru.tgra.shapes;

import java.util.Random;

public class Cell {
	public Wall northWall;
	public Wall eastWall;
	public boolean door = false;
	public Pill pill;
	public DeadlyFloor deadly;
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
		deadly = null;
	}
	
	public void display(Shader shader)
	{
		if (northWall != null) {
			northWall.draw(shader);
		}
		if (eastWall != null) {
			eastWall.draw(shader);
		}
	}

	public void randomize(){
		northWall = null;
		eastWall = null;
		if(random.nextBoolean()) northWall = new Wall(row+1.0f, col+0.5f, 1.1f, 0.1f);
		if(random.nextBoolean()) eastWall = new Wall(row+0.5f, col+1.0f, 0.1f, 1.1f);
	}
}
