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
import com.redagent.helper.VectorHelper;
import com.redagent.physics.Direction;
import com.redagent.physics.Speed;

public class Cloud extends Entity {

	public Cloud(Vector2 pos) {
		super(pos);
		Sprite s = new Sprite(ResourceLoader.getInstance().getCloudShaddow());
//		shape.setAsBox(s.getWidth() / 2, s.getHeight() / 2);

	}

	public void move(Vector2 dir) {

	}

	public void resetInputVariables() {
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
