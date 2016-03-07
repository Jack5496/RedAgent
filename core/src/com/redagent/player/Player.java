package com.redagent.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.redagent.game.CameraController;
import com.redagent.game.Main;
import com.redagent.menu.MenuHandler;

public class Player extends Entity{

	public String name;

	/**
	 * InputVariables
	 */
	public Vector3 stickLeft;
	public boolean stickLeftDown;

	public Vector3 stickRight;	
	
	public CameraController cameraController;
	public MenuHandler menuHandler;

	public Player(String name) {
		this.name = name;
		menuHandler = new MenuHandler(this);
		body = Main.getInstance().spawnPlayer();
		initCamera();
		resetInputVariables();
	}

	public void initCamera() {
		cameraController = new CameraController(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cameraController.setTrack(this);
	}
	
	public void move(Vector2 dir){
		body.setLinearVelocity(dir);
	}

	
//	public Vector3 getObjPos() {
//		return testPos;
//	}

	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickRight = new Vector3();
	}


	public void updateMyGameObjects() {
		
	}

}
