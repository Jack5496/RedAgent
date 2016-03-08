package com.redagent.worldgenerator;

import java.util.Random;

import com.redagent.game.Main;
import com.redagent.world.Coord;
import com.redagent.world.DiamondSquareGenerator;
import com.redagent.world.MapTile;
import com.redagent.world.PerlinNoiseGenerator;
import com.redagent.world.TextureNames;
import com.redagent.world.TileWorld;

public class NatureGenerator implements GeneratorInterface {

	TileWorld world;
	DiamondSquareGenerator gen;
	int chunkSize = 8+1;
	Random random;

	public NatureGenerator(TileWorld world) {
		this.world = world;

		random = new Random();
		gen = new DiamondSquareGenerator(chunkSize, 50, 5, 2, random.nextLong());
	}

	private void generate(Coord begin, Coord end, float topLeft, float topRight, float bottomLeft, float bottomRight) {
//		float map[][] = gen.generate(topLeft, topRight, bottomLeft, bottomRight);
		
		Main.log(getClass(), "generate: "+begin.toString()+" to "+end.toString());

		int xm = 0;
		int ym = 0;
		for (int y = (int) begin.y; y <= end.y; y ++) {
			xm = 0;
			for (int x = (int) begin.x; x <= end.x; x ++) {
				Coord c = new Coord(x, y);

//				int num = (int) map[xm][ym];
				float num = PerlinNoiseGenerator.perlin(xm, ym);
				
				int div = 100/(TextureNames.tiles.length-1);
				
				Main.log(getClass(), ""+div+" t: "+num/div);
				int text = (int) (num/div);
				
				
//				Main.log(getClass(), ""+num);

				MapTile t = new MapTile(c, false, 0, TextureNames.tiles[text], num);
				
				world.setMapTile(t);
				xm++;
			}
			ym++;
			
		}
	}

	@Override
	public MapTile generateTileAt(Coord at) {

		if (world.exsistMapTile(at))
			return world.getMapTile(at);
		
		Coord bottomLeft = getChunkStart(at,0,0);
		Coord bottomRight = getChunkStart(at,1,0);
		Coord topLeft = getChunkStart(at,0,1);
		Coord topRight = getChunkStart(at,1,1);
		
		generate(bottomLeft, topRight, getDSH(topLeft), getDSH(topRight), getDSH(bottomLeft), getDSH(bottomRight));
		
		return world.getMapTile(at);
	}
	
	public Coord getChunkStart(Coord at, int xo, int yo){
		int x = (int) (at.x);
		int y = (int) (at.y);
		
		x=x-x%chunkSize;
		y=y-y%chunkSize;
		
		x=x+(chunkSize-1)*xo;
		y=y+(chunkSize-1)*yo;
		
		if(at.x<0) x-=(chunkSize-1);
		if(at.y<0) y-=(chunkSize-1);
		
		return new Coord(x,y);
	}

	public float getDSH(Coord c) {
		if (world.exsistMapTile(c))
			return world.getMapTile(c).dsh;
		return random.nextFloat()*20*(random.nextInt(2)-1)+20;
	}

	@Override
	public boolean canBeGenerated(MapTile tile) {
		// TODO Auto-generated method stub
		return false;
	}

}