package com.redagent.worldgenerator;

import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public interface StreetTile{	

	boolean canBeGenerated(TileWorld world);
	boolean doesFitTo(MapTile tile);
	
}