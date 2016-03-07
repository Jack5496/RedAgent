package com.redagent.worldgenerator;

import com.redagent.world.Coord;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class StreetGenerator implements GeneratorInterface{

	TileWorld world;
	
	public StreetGenerator(TileWorld world){
		this.world = world;
	}

	@Override
	public MapTile generateTileAt(Coord at) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canBeGenerated(MapTile tile) {
		// TODO Auto-generated method stub
		return false;
	}
	
}