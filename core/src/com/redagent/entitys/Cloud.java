package com.redagent.entitys;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.redagent.game.Main;
import com.redagent.game.ResourceLoader;
import com.redagent.helper.SpriteEntity;
import com.redagent.helper.VectorHelper;
import com.redagent.physics.Direction;
import com.redagent.physics.Speed;

public class Cloud extends Entity {

	public Vector2 stickLeft;

	private float speed;
	
	public int direction;

	public Cloud(Vector2 pos) {
		speed = Speed.cloudSpeed;
		direction = Direction.EAST;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(pos.cpy());

		body = Main.getInstance().world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		Sprite s = new Sprite(ResourceLoader.getInstance().getCloudShaddow());
		shape.setAsBox(s.getWidth() / 2, s.getHeight() / 2);

		// Sprite1
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.5f;

		body.createFixture(fixtureDef);
		
		this.lastPos = body.getPosition().cpy();
		resetInputVariables();
	}

	public void move(Vector2 dir) {
		if (dir.len() != 0) {
			this.direction = VectorHelper.getDirFromVector(dir);
		}

		dir.nor();
		dir.scl(speed);
		body.setLinearVelocity(dir);
	}

	public void resetInputVariables() {
		stickLeft = new Vector2();
	}

	@Override
	public List<Sprite> getSprite() {
		List<Sprite> back = new ArrayList<Sprite>();
		back.add(new Sprite(ResourceLoader.getInstance().getCloudShaddow()));
		return back;
	}

//	@Override
//	public int compareTo(SpriteEntity o) {
//
//		if (o instanceof Cloud) {
//			if (o.lastPos.y < lastPos.y)
//				return -1;
//			if (o.lastPos.y > lastPos.y)
//				return 1;
//			if (o.lastPos.x < lastPos.x)
//				return -1;
//			if (o.lastPos.x > lastPos.x)
//				return 1;
//			return 0;
//		}
//		return 1;
//	}

}
