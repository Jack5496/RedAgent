package com.redagent.entitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.redagent.game.CameraController;
import com.redagent.game.Main;
import com.redagent.physics.Direction;
import com.redagent.world.Chunk;

public class CloudHandler {

	List<Cloud> clouds;

	public int windDirection = Direction.EAST;

	public CloudHandler() {
		clouds = new ArrayList<Cloud>();
	}

	public List<Cloud> getClouds() {
		return new ArrayList<Cloud>(clouds);
	}

	public void updateClouds() {
		LocalPlayer[] players = Main.getInstance().playerHandler.getPlayers();

		List<Cloud> toFar = new ArrayList<Cloud>();
		for (Cloud c : getClouds()) {
			Main.log(getClass(), "CloudPos: "+c.getCoord().toString());
			float minDis = Float.MAX_VALUE;
			for (LocalPlayer p : players) {
				float dist = c.getCoord().dst(p.getCoord());
				if (dist < minDis)
					minDis = dist;
			}
			if (minDis > CameraController.xAmount/2) {
				Main.log(getClass(), "A cloud is to Far");
				toFar.add(c);
			} else {
				Main.log(getClass(), "Cloud Distance: " + minDis);
			}
		}
		for (Cloud c : toFar) {
			Vector2 pos = c.getCoord();
			clouds.remove(c);
			spawnCloud(pos.add(Direction.getVectorFromDirection(windDirection).scl(-1).scl(CameraController.xAmount/2)));
			Main.log(getClass(), "Cloud to Far away");
		}

		moveClouds();
	}

	private void moveClouds() {
		for (Cloud c : getClouds()) {
			c.move(Direction.getVectorFromDirection(windDirection));
		}
	}

	public void spawnCloud(Vector2 pos) {
		clouds.add(new Cloud(pos));
	}

}
