package com.redagent.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.redagent.game.Main;

public class DesktopLauncher {
	
	public static String title = "RedAgent";

	static final String iphone = "iphone";	
	static final String svga = "svga";
	static final String xga = "xga";
	static final String hd = "hd";
	static final String fullHD = "fullHD";
	
	static String resolution = xga;
	

	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		switch(resolution){
		case iphone: config.width = 568;
		config.height = 300;
		break;
		case svga: config.width = 800;
		config.height = 600;
		break;
		case xga: config.width = 1024;
		config.height = 768;
		break;
		case hd: config.width = 1360;
		config.height = 768;
		break;
		case fullHD: config.width = 1920;
		config.height = 1080;
		break;
		}
		config.title = title;
		new LwjglApplication(new Main(), config);
	}
}
