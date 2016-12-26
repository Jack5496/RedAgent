package com.redagent.entitys;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redagent.physics.Body;

public class Entity extends Body{
	
	public Entity(float x, float y){
		this(new Vector2(x,y));
	}
	
	public Entity(Vector2 position){
		super(position);
	}


	public List<Sprite> getSprite() {
		return null;
	}
	
	
}
