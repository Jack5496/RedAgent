package com.redagent.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Coord extends Vector2{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8753944505281449821L;
	
	public Coord(Vector2 vec){
		super(vec.x,vec.y);
	}

	public Coord(int x, int y) {
		super(x,y);
	}

	public Coord(float x, float y) {
		super(x,y);
	}

	public boolean isNeumann(Coord check, int tileSize) {
		return getNeumann(tileSize).contains(check);
	}
		
	public Coord getDifference(Vector2 rel){
		Vector2 s1 = rel.cpy();
		Vector2 s2 = this.cpy();
		
		return new Coord(s1.sub(s2));
	}

	public List<Coord> getNeumann(int tileSize) {
		List<Coord> back = new ArrayList<Coord>();
		back.add(abouve(tileSize));
		back.add(under(tileSize));
		back.add(left(tileSize));
		back.add(right(tileSize));
		return back;
	}

	public boolean isAbouve(Coord check, int tileSize) {
		return check == abouve(tileSize);
	}

	public boolean isUnder(Coord check, int tileSize) {
		return check == under(tileSize);
	}

	public boolean isLeft(Coord check, int tileSize) {
		return check == left(tileSize);
	}

	public boolean isRight(Coord check, int tileSize) {
		return check == right(tileSize);
	}

	public Coord abouve(int tileSize) {
		return new Coord(x, y + tileSize);
	}

	public Coord under(int tileSize) {
		return new Coord(x, y - tileSize);
	}

	public Coord left(int tileSize) {
		return new Coord(x - tileSize, y);
	}

	public Coord right(int tileSize) {
		return new Coord(x + tileSize, y);
	}
	
	public String toString(){
		return x+"|"+y;
	}
}
