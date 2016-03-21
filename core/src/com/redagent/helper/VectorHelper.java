package com.redagent.helper;

import com.badlogic.gdx.math.Vector2;
import com.redagent.physics.Direction;

public class VectorHelper {

	public static int getDirFromVector(Vector2 v) {
		float a = v.angle();

		if (a >= 45 && a <= 180 - 45)
			return Direction.NORTH;
		if (a > 180 - 45 && a < 180 + 45)
			return Direction.WEST;
		if (a >= 180 + 45 && a <= 360 - 45)
			return Direction.SOUTH;
		return Direction.EAST;
	}

}
