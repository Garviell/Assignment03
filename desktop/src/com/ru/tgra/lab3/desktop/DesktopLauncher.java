package com.ru.tgra.lab3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ru.tgra.mazerunner.LabFirst3DGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Lab3"; // or whatever you like
		//config.width = 1024;  //experiment with
		//config.height = 780;  //the window size
		config.x = 250;
		config.y = 50;
		//config.fullscreen= true;

		new LwjglApplication(new LabFirst3DGame(), config);
	}
}