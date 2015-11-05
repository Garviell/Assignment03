	package com.ru.tgra.mazerunner.logic;

	import com.ru.tgra.mazerunner.graphics.Shader;
	import com.ru.tgra.mazerunner.graphics.objects.ThingOne;

	public class Score {
	public ThingOne theThing;
	public int numScore;
    public boolean win;

	
	public Score(Camera camera)
	{
		theThing = new ThingOne();
		numScore = 0;
        win = false;
	}
	
	public void addThing()
	{
		if (numScore < 2) {
            numScore++;
        }
        else {
            win = true;
        }
	}
	
	public void removething()
	{
        if (numScore > 0) {
            numScore--;
        }
	}
	
	public void display(Shader shader, float deltaTime)
	{
		theThing.display(shader, deltaTime, numScore);
	}

	
}
