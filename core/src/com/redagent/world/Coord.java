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

	public boolean isNeumann(Coord check) {
		return getNeumann().contains(check);
	}
		
	public Coord getDifference(Vector2 rel){
		Vector2 s1 = rel.cpy();
		Vector2 s2 = this.cpy();
		
		return new Coord(s1.sub(s2));
	}

	public List<Coord> getNeumann() {
		List<Coord> back = new ArrayList<Coord>();
		back.add(abouve());
		back.add(under());
		back.add(left());
		back.add(right());
		return back;
	}

	public boolean isAbouve(Coord check) {
		return check == abouve();
	}

	public boolean isUnder(Coord check) {
		return check == under();
	}

	public boolean isLeft(Coord check) {
		return check == left();
	}

	public boolean isRight(Coord check) {
		return check == right();
	}

	public Coord abouve() {
		return new Coord(x, y + 1);
	}

	public Coord under() {
		return new Coord(x, y - 1);
	}

	public Coord left() {
		return new Coord(x - 1, y);
	}

	public Coord right() {
		return new Coord(x + 1, y);
	}
	
	public String toString(){
		return x+"|"+y;
	}
}
