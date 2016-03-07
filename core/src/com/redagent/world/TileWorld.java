package com.redagent.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.redagent.game.Main;
import com.redagent.game.TextureNames;

public class TileWorld {

	private static TileWorld instance;

	private static ConcurrentHashMap<Coord, MapTile> map;

	public TileWorld() {
		instance = this;
		map = new ConcurrentHashMap<Coord, MapTile>();
		initTestWorld();	
	}

	private void initTestWorld() {
		Coord begin = new Coord(-100*MapTile.tileSize, -100*MapTile.tileSize);
		Coord end = new Coord(100*MapTile.tileSize, 100*MapTile.tileSize);
		
		Random random = new Random();

		for (int y = (int) begin.y; y < end.y; y+=MapTile.tileSize) {
			for (int x = (int) begin.x; x < end.x; x+=MapTile.tileSize) {
				Coord c = new Coord(x, y);
				
				int randomNumber = random.nextInt(TextureNames.tiles.length - 0) + 0;
				
				MapTile t = new MapTile(c, false, 0, TextureNames.tiles[randomNumber]);
				setMapTile(t);
			}
		}
	}

	public static TileWorld getInstance() {
		return instance;
	}

	public List<MapTile> getArea(int xb, int yb, int xe, int ye) {
		return getArea(new Coord(xb, yb), new Coord(xe, ye));
	}
	
	public List<MapTile> getArea(float xb, float yb, float xe, float ye) {
		return getArea(new Coord(xb, yb), new Coord(xe, ye));
	}

	public List<MapTile> getArea(Coord begin, Coord end) {
		
		int xmin = (int) begin.x;
		int xmax = (int) begin.x;
		if (begin.x < end.x) {
			xmax = (int) end.x;
		} else {
			xmin = (int) end.x;
		}

		int ymin = (int) begin.y;
		int ymax = (int) begin.y;
		if (begin.y < end.y) {
			ymax = (int) end.y;
		} else {
			ymin = (int) end.y;
		}
		
		xmin+=xmin%MapTile.tileSize;
		xmax+=xmax%MapTile.tileSize;
		ymin+=ymin%MapTile.tileSize;
		ymax+=ymax%MapTile.tileSize;
		

		return getAreaCoordsRight(new Coord(xmin, ymin), new Coord(xmax, ymax));
	}

	private List<MapTile> getAreaCoordsRight(Coord begin, Coord end) {
		
		List<MapTile> back = new ArrayList<MapTile>();
		for (int y = (int) begin.y; y < end.y+1*MapTile.tileSize; y+=MapTile.tileSize) {
			for (int x = (int) begin.x; x < end.x+1*MapTile.tileSize; x+=MapTile.tileSize) {
				Coord c = new Coord(x, y);
				
				if (exsistMapTile(c)) {
					back.add(getMapTile(c));
				}
			}
		}

		 
		return back;
	}

	public void setMapTile(MapTile tile) {
		map.put(tile.getCoord(), tile);
	}

	public boolean exsistMapTile(Coord at) {
		return map.containsKey(at);
	}

	public MapTile getMapTile(Coord at) {
		return map.get(at);
	}

}