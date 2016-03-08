package com.redagent.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.redagent.game.ResourceLoader;

public class MapTile {
	
	public final static int tileSize = 64;
	public float dsh;
	
	public static int DIRECTION_NORTH = 0;
	public static int DIRECTION_EAST = 3;
	public static int DIRECTION_SOUTH = 2;
	public static int DIRECTION_WEST = 1;	
	
	public int direction;
	private boolean solid;
	private Coord coord;
	BodyDef body;
	
	public String textureName;
	
	public MapTile(Coord coord, boolean solid, int direction, String textureName, float dsh){
		this.coord = coord;
		body = new BodyDef();
		this.direction = direction;
		setSolid(solid);
		this.textureName = textureName;
		this.dsh = dsh;
	}
	
	public MapTile(Coord coord, boolean solid, int direction){
		this(coord,solid, direction, "grass", 50);
	}
	
	public int getRotation(){
		return direction*90;
	}
	
	public void setSolid(boolean solid){
		this.solid = solid;
		if(solid){
			body.type = BodyDef.BodyType.StaticBody;
		}
		else{
//			body.t
		}
	}
	
	public Texture getTexture(){
		return ResourceLoader.getInstance().getTile(textureName);
	}
	
	public boolean isSolid(){
		return this.solid;
	}
	
	public Coord getCoord(){
		return this.coord;
	}
    
}