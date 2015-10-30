	package com.ru.tgra.mazerunner.logic;

	import com.ru.tgra.mazerunner.graphics.Shader;
	import com.ru.tgra.mazerunner.graphics.objects.ThingOne;

	public class Score {
	public ThingOne theThing;
	public int numScore;
	
	public Score(Camera camera)
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
	
	public void display(Shader shader, float deltaTime)
	{
		theThing.display(shader, deltaTime, numScore);
	}

	
}
