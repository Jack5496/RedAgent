package com.redagent.entitys;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.redagent.helper.SpriteEntity;

public class Entity extends SpriteEntity{

	public Body body;
	public Sprite sprite;

	public Vector2 getCoord(){
		return body.getPosition().cpy();
	}

	public List<Sprite> getSprite() {
		return null;
	}
	
	
}
