package com.redagent.worldgenerator;

import com.redagent.world.Coord;
import com.redagent.world.MapTile;

public interface GeneratorInterface {

	public MapTile generateTileAt(Coord at);
	public boolean canBeGenerated(MapTile tile);
	
}