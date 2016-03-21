package com.redagent.helper;

import com.badlogic.gdx.math.Vector2;

public class SpriteEntity implements Comparable<SpriteEntity>{

	public Vector2 lastPos;

	@Override
	public int compareTo(SpriteEntity o) {
		if(o.lastPos.y<lastPos.y) return -1;
		if(o.lastPos.y>lastPos.y) return 1;
		if(o.lastPos.x<lastPos.x) return -1;
		if(o.lastPos.x>lastPos.x) return 1;
		return 0;
	}
	
	
}
