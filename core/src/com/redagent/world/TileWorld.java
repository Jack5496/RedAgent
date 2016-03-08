package com.redagent.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.redagent.game.Main;
import com.redagent.worldgenerator.GeneratorInterface;
import com.redagent.worldgenerator.NatureGenerator;

public class TileWorld {

	private static TileWorld instance;
	private GeneratorInterface generator;

	private static ConcurrentHashMap<Coord, MapTile> map;

	public TileWorld() {
		instance = this;
		map = new ConcurrentHashMap<Coord, MapTile>();
		generator = new NatureGenerator(this);
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

		xmin += xmin % 1;
		xmax += xmax % 1;
		ymin += ymin % 1;
		ymax += ymax % 1;
		

		Coord beginNew = new Coord(xmin, ymin);
		Coord endNew = new Coord(xmax, ymax);


		return getAreaCoordsRight(beginNew, endNew);
	}

	private List<MapTile> getAreaCoordsRight(Coord begin, Coord end) {
		
		List<MapTile> back = new ArrayList<MapTile>();
		for (int y = (int) begin.y; y < end.y + 1 ; y ++) {
			for (int x = (int) begin.x; x < end.x + 1 ; x ++) {
				Coord c = new Coord(x, y);				
				if (exsistMapTile(c)) {
					back.add(getMapTile(c));
				} else {
					
					back.add(generator.generateTileAt(c));
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