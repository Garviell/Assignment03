package com.ru.tgra.shapes;

import java.util.ArrayList;

public class Score {
	public ThingOne[] things = new ThingOne[10];
	public int numberOfThings;
	
	Score()
	{
		numberOfThings = 0;
	}
	
	public void addThing()
	{
		numberOfThings++;
		things[numberOfThings] = new ThingOne();
	}
	
	public void removething()
	{
		things[numberOfThings] = null;
		numberOfThings--;
	}
	
	public void display(Shader shader, float deltaTime)
	{
		for(int i = 0; i < numberOfThings; i++)
		{
			things[numberOfThings].display(shader, deltaTime);
		}
	}

	
}
