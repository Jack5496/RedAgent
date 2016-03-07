package com.redagent.worldgenerator;

import com.redagent.world.Coord;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class NatureTile extends MapTile implements StreetTile {

	public NatureTile(Coord coord, boolean solid, int direction) {
		super(coord, solid, direction);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canBeGenerated(TileWorld world) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesFitTo(MapTile tile) {
		if (tile.getClass() == NatureTile.class) {
			
		}
		return false;
	}

}