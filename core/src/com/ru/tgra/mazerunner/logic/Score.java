	package com.ru.tgra.mazerunner.logic;

	import com.ru.tgra.mazerunner.graphics.Shader;
	import com.ru.tgra.mazerunner.graphics.objects.ThingOne;

	public class Score {
	public ThingOne theThing;
	public int numScore;
    public boolean win;
    public boolean player1;

	
	public Score(Boolean player1, int sizeX, int sizeZ)
	{
        this.player1 = player1;
		theThing = new ThingOne(player1, sizeX, sizeZ);
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
