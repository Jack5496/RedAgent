package com.redagent.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Entity {

	public Body body;

	public Vector2 getCoord(){
		return body.getPosition();
	}
}
