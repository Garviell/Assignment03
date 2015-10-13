package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Maze {
	private int row = 20;
	private int col = 20;
	private float body = 0.2f;
	private Cell[][] cell = new Cell[row][col];
	private int numDoors = 20;
	private Door[] doors = new Door[numDoors];
	private int numPills = 40;
	private int numDeadlyFloors = 20;
	Random random= new Random();
	public int score = 0;

	
	public Maze()
	{
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				cell[i][j] = new Cell(i, j);
			}
		}

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
		
		for(int i = 0; i < numPills; i++)
		{
			int x = random.nextInt(20);
			int z = random.nextInt(20);
			if(cell[x][z].pill == null)
			{
				cell[x][z].pill = new Pill(x, z);
			}
		}
		
		for(int i = 0; i < numDeadlyFloors; i++)
		{
			int x = random.nextInt(20);
			int z = random.nextInt(20);
			if(cell[x][z].deadly == null)
			{
				cell[x][z].deadly = new DeadlyFloor(cell[x][z]);
			}
		}
		
	}
	
	public void draw(int colorLoc, float deltaTime)
	{
		ModelMatrix.main.pushMatrix();
		for ( int i = 0; i < row; i++) {
			ModelMatrix.main.pushMatrix();
			for (int j = 0; j < col; j++) {
				Gdx.gl.glUniform4f(colorLoc, 0.55f, 0.53f, 0.5f, 1.0f);
				cell[i][j].draw();
				if(cell[i][j].pill != null) cell[i][j].pill.display(colorLoc, deltaTime);
				if(cell[i][j].deadly != null) cell[i][j].deadly.display(colorLoc);
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
		outOfBoard(cam);
		
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
			if(cell[eyex][eyez].pill != null)
			{
				checkPill(cam, cell[eyex][eyez]);
			}
			
			for(int i = Math.max(0, eyex-1); i < Math.min(20, eyex+2); i++)
			{
				for(int j = Math.max(0, eyez-1); j < Math.min(20, eyez+2); j++)
				{
					if(cell[i][j].northWall != null) check.add(cell[i][j].northWall);
					if(cell[i][j].eastWall != null) check.add(cell[i][j].eastWall);
				}
			}
			for(int i = 0; i < numDeadlyFloors; i++)
			{
				if(cell[eyex][eyez].deadly != null)
				{
					if(cell[eyex][eyez].deadly.color > 0.9)
					{
						score = 0;
						ModelMatrix.main.alive = false;
						ModelMatrix.main.setShaderMatrix();
					}
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
		float distanceX = Math.abs(cam.eye.x - door.endX);
		float distanceZ = Math.abs(cam.eye.z - door.endZ);
		
		if(door.distanceZ < door.distanceX)
		{
			if(door.width > 0.4f && distanceZ <= 0.21f)
			{
				score = 0;
				ModelMatrix.main.alive = false;
				ModelMatrix.main.setShaderMatrix();
			}
			else
			{
				if(cam.eye.z < door.posZ)
	            {
	                    cam.eye.z = door.posZ - door.width/2 - body;
	            }
	            else
	            {
	                    cam.eye.z = door.posZ + door.width/2 + body;
	            }
			}
		}
		else
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
	
	public void checkPill(Camera cam, Cell cell)
	{
		float x0 = cam.eye.x;
		float z0 = cam.eye.z;
		float x1 = cell.pill.posX;
		float z1 = cell.pill.posZ;
		if(Math.pow(x0-x1 ,2) + Math.pow(z1-z0 ,2) <= Math.pow((body - 0.06f),2))
		{
			cell.pill = null;
			score++;
			System.out.println(score);
		}
			
	}
	
	public void outOfBoard(Camera cam)
	{
		if(cam.eye.x < -1.6f || cam.eye.x > 21.6f || cam.eye.z < -1.6f || cam.eye.z > 21.6f)
		{
			score = 0;
			ModelMatrix.main.alive = false;
			ModelMatrix.main.setShaderMatrix();
		}
	}
}
