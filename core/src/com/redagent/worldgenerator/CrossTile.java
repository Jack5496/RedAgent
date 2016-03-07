package com.redagent.worldgenerator;

import com.redagent.world.Coord;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class CrossTile extends MapTile implements StreetTile{

	public CrossTile(Coord coord, boolean solid) {
		super(coord, solid, DIRECTION_NORTH);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canBeGenerated(TileWorld world) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesFitTo(MapTile tile) {
		// TODO Auto-generated method stub
		return false;
	}
	
    
}