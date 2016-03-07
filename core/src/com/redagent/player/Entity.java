package com.redagent.player;

import com.badlogic.gdx.physics.box2d.Body;
import com.redagent.world.Coord;

public class Entity {

	public Body body;

	public Coord getCoord(){
		return new Coord(body.getPosition());
	}
}
