package com.redagent.physics;

import com.badlogic.gdx.math.Vector2;
import com.redagent.game.Main;

public class Body implements Comparable<Body>{

	Vector2 position;
	Vector2 velocity;
	Vector2 acceleration;
	
	public Body(Vector2 position, Vector2 velocity, Vector2 acceleration){
		setPosition(position);
		setVelocity(velocity);
		setAcceleration(acceleration);
	}
	
	public Body(Vector2 position){
		this(position,new Vector2(0,0), new Vector2(0,0));
	}
	
	public Body(Vector2 position, Vector2 velocity){
		this(position,velocity, new Vector2(0,0));
	}

	public Vector2 getPosition() {
		return position.cpy();
	}
	
	public Body setPosition(Vector2 newpos){
		this.position = newpos.cpy();
		return this;
	}		
	
	public Body setVelocity(Vector2 velocity){
		this.velocity = velocity.cpy();
		return this;
	}		
	
	public Body setAcceleration(Vector2 acceleration){
		this.acceleration = acceleration.cpy();
		return this;
	}		
	
	@Override
	public int compareTo(Body o) {
		float me = this.position.x+this.position.y;
		float other = o.position.x+o.position.y;
		
		if(me>other){
			return -1;
		}
		if(me<other){
			return 1;
		}
		
		//both bodys same vertical height
		if(this.position.x<o.position.x){
			return 1;
		}
		if(this.position.x>o.position.x){
			return -1;
		}
		
		return 0;
	}
	
}
