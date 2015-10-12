package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class Maze {
	private int row = 20;
	private int col = 20;
	float body = 0.2f;
	private Cell[][] cell = new Cell[row][col];
	int numDoors = 20;
	private Door[] doors = new Door[numDoors];
	Random random= new Random();

	
	public Maze()
	{
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cell[i][j] = new Cell(i, j);
			}
		}
//		cell[0][0].eastWall = null;
//		cell[0][0].northWall = null;

		for(int i = 0; i < numDoors; i++)
		{
			int x = random.nextInt(20);
			int z = random.nextInt(20);
			if(cell[x][z].northWall == null && !cell[x][z].door)
			{
				doors[i] = new Door(cell[x][z]);
				cell[x][z].door = true;
			}
		}
		
	}
	
	public void draw()
	{
		ModelMatrix.main.pushMatrix();
		for ( int i = 0; i < row; i++) {
			ModelMatrix.main.pushMatrix();
			for (int j = 0; j < col; j++) {
				cell[i][j].draw();
				ModelMatrix.main.addTranslation(0, 0, 1.0f);
			}
			ModelMatrix.main.popMatrix();
			ModelMatrix.main.addTranslation(1.0f, 0, 0);
		}
		ModelMatrix.main.popMatrix();
		
	}
	
	public void displayDoors(int colorLoc, float deltaTime)
	{
		for(int i = 0; i < numDoors; i++)
		{
			if(doors[i] != null) 
				{
					doors[i].update(deltaTime);
					doors[i].display(colorLoc);
				}
		}
			
	}
		
	public void checkCollision(Camera cam, float deltaTime)
	{	
		ArrayList<Wall> check = new ArrayList<Wall>();
		ArrayList<Wall> hit = new ArrayList<Wall>();
		
		findClosestWalls(cam, check);
	
		// Find walls that intersects
		boolean goOn = true;
		while(goOn)
		{
			for(Wall wall : check)
			{
				if(intersects(cam, wall, deltaTime)) hit.add(wall);
			}
			
			if(!hit.isEmpty())
			{
				float minHit = Float.MAX_VALUE;
				Wall minHitWall = null;	
				String xz = "";
				
				// Find who is nearest
				for(Wall wall : hit)
				{
					if(wall.distanceX < minHit)
					{
						minHit = wall.distanceX;
						minHitWall = wall;
						xz = "X";
					}
					if(wall.distanceZ < minHit)
					{
						minHit = wall.distanceZ;
						minHitWall = wall;
						xz = "Z";
					}
				}
				moveCamera(cam, minHitWall, xz);
				check.clear();
				check = (ArrayList<Wall>) hit.clone();
				hit.clear();
			}
			else
			{
				goOn = false;
			}
		}
		
		for(int i = 0; i < numDoors; i++)
		{
			if(doors[i] != null)
			{
				if(doors[i].intersects(cam))
				{
					doorCollision(cam, doors[i]);
				}
			}
		}
		
	}
	
	public void findClosestWalls(Camera cam, ArrayList<Wall> check )
	{
		int eyex = (int) Math.floor(cam.eye.x);
		int eyez = (int) Math.floor(cam.eye.z);
		
		if (cam.eye.x >= 0 && cam.eye.x < 20 && cam.eye.z >= 0 && cam.eye.z < 20)
		{
			for(int i = Math.max(0, eyex-1); i < Math.min(20, eyex+2); i++)
			{
				for(int j = Math.max(0, eyez-1); j < Math.min(20, eyez+2); j++)
				{
					if(cell[i][j].northWall != null) check.add(cell[i][j].northWall);
					if(cell[i][j].eastWall != null) check.add(cell[i][j].eastWall);
				}
			}
		}
		// Deal with the corners
		else if(cam.eye.x <= 0 && cam.eye.x > -1 || cam.eye.x >= 20 && cam.eye.x < 21
				|| cam.eye.z <= 0 && cam.eye.z > -1 || cam.eye.z >= 20 && cam.eye.z < 21)
		{
			for(int i = eyex-1; i < eyex+2; i++)
			{
				for(int j = eyez-1; j < eyez+2; j++)
				{
					try
					{
						if(cell[i][j].northWall != null) check.add(cell[i][j].northWall);
					} catch (ArrayIndexOutOfBoundsException aioobe) {
					    // It's fine, just carry on
					}
					
					try
					{
						if(cell[i][j].eastWall != null) check.add(cell[i][j].eastWall);
					} catch (ArrayIndexOutOfBoundsException aioobe) {
					    // It's fine, just carry on
					}
					
				}
			}
		}
	}
	
	public boolean intersects(Camera cam, Wall wall, float deltaTime)
	{
		wall.distanceX = Math.abs(cam.eye.x - wall.posX);
		wall.distanceZ = Math.abs(cam.eye.z - wall.posZ);

	    if (wall.distanceX > (wall.height/2 + body - deltaTime)) return false;
	    if (wall.distanceZ > (wall.width/2 + body - deltaTime)) return false;
	    float checkX = wall.distanceX;
	    float checkZ = wall.distanceZ;

    	wall.distanceX = (wall.height/2 + body - deltaTime) - wall.distanceX;
    	wall.distanceZ = (wall.width/2 + body - deltaTime) - wall.distanceZ;

	    if (checkX <= (wall.height/2 + body - deltaTime)) 
	    { 
	    	return true;
	    } 
	    if (checkZ <= (wall.width/2 + body - deltaTime)) 
	    {
	    	return true;
	    }
	    
		return false;
	}
	
	public void moveCamera(Camera cam, Wall minHitWall, String xz)
	{
		if(xz.equals("X"))
		{
			if(cam.eye.x < minHitWall.posX)
            {
                    cam.eye.x = minHitWall.posX - minHitWall.height/2 - body;
            }
            else
            {
                    cam.eye.x = minHitWall.posX + minHitWall.height/2 + body;
            }
		}
		if(xz.equals("Z"))
		{
			if(cam.eye.z < minHitWall.posZ)
            {
                    cam.eye.z = minHitWall.posZ - minHitWall.width/2 - body;
            }
            else
            {
                    cam.eye.z = minHitWall.posZ + minHitWall.width/2 + body;
            }

		}
	}
	
	public void doorCollision(Camera cam, Door door)
	{
		if(cam.eye.x < door.posX)
        {
                cam.eye.x = door.posX - door.height/2 - body;
        }
        else
        {
                cam.eye.x = door.posX + door.height/2 + body;
        }
	}
}
