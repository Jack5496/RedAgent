package com.redagent.worldgenerator;

import com.redagent.world.Coord;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class NatureWalkTile extends MapTile implements StreetTile {

	public NatureWalkTile(Coord coord, boolean solid, int direction) {
		super(coord, solid, direction);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isVertical() {
		return this.direction==DIRECTION_NORTH || this.direction==DIRECTION_SOUTH;
	}

	public boolean isHorizontal() {
		return this.direction==DIRECTION_EAST || this.direction==DIRECTION_WEST;
	}

	@Override
	public boolean canBeGenerated(TileWorld world) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesFitTo(MapTile tile) {
		if (tile.getClass() == NatureWalkTile.class) {
			
		}
		return false;
	}

}