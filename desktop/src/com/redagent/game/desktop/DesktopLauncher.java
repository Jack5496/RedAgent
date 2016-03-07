package com.redagent.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.redagent.game.Main;

public class DesktopLauncher {
	
	public static String title = "RedAgent";
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		config.title = title;
		new LwjglApplication(new Main(), config);
	}
}
