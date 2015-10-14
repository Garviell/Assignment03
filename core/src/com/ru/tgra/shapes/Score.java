	package com.ru.tgra.shapes;

import java.util.ArrayList;

public class Score {
	public ThingOne theThing;
	public int numScore;
	
	Score(Camera camera)
	{
		theThing = new ThingOne();
		numScore = 0;
	}
	
	public void addThing()
	{
		numScore++;
	}
	
	public void removething()
	{
		numScore--;
	}
	
	public void display(Shader shader, float deltaTime, Camera camera)
	{
		theThing.display(shader, deltaTime, numScore);
	}

	
}
