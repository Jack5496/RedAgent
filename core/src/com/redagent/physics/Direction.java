package com.redagent.physics;

import com.badlogic.gdx.math.Vector2;

public class Direction {

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	public static final Vector2 NORTHV = new Vector2(0, 1);
	public static final Vector2 EASTV = new Vector2(1, 0);
	public static final Vector2 SOUTHV = new Vector2(0, -1);
	public static final Vector2 WESTV = new Vector2(-1, 0);

	public static Vector2 getVectorFromDirection(int dir) {
		switch (dir) {
		case NORTH:
			return NORTHV.cpy();
		case EAST:
			return EASTV.cpy();
		case SOUTH:
			return SOUTHV.cpy();
		case WEST:
			return WESTV.cpy();
		}
		return new Vector2();
	}
}