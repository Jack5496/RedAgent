package com.redagent.world;

import java.util.ArrayList;
import java.util.List;

import com.redagent.game.Main;
import com.redagent.materials.Grass;
import com.redagent.materials.Material;
import com.redagent.materials.Sand;
import com.redagent.worldgenerator.GeneratorInterface;
import com.redagent.worldgenerator.NatureGenerator;

public class TileWorld {

	private static TileWorld instance;
	private GeneratorInterface generator;

	// Number of cells
	public static int worldSize = 100;

	public static Chunk chunks[][];

	public TileWorld() {
		instance = this;
		chunks = new Chunk[worldSize][worldSize];
		setGenerator(new NatureGenerator(this));
	}

	public static TileWorld getInstance() {
		return instance;
	}

	public List<MapTile> getArea(int xs, int ys, int xe, int ye) {

		int cxs = globalPosToChunkPos(xs);
		int cys = globalPosToChunkPos(ys);

		int cxe = globalPosToChunkPos(xe);
		int cye = globalPosToChunkPos(ye);

		int xLeft = cxs;
		int xRight = cxe;
		if(xLeft>xRight){
			int h = xLeft;
			xLeft = xRight;
			xRight = h;
		}
		
		int yBottom = cys;
		int yTop = cye;
		if(yBottom>yTop){
			int h = yBottom;
			yBottom = yTop;
			yTop = h;
		}
		
		List<MapTile> back = new ArrayList<MapTile>();
		for (int y = yTop; y > yBottom - 1; y--) {
			for (int x = xLeft; x < xRight + 1; x++) {
				if (!exsistChunkTile(x, y)) {
					generator.generateChunkAt(x, y);
					Chunk ch = getChunk(x, y);
					ch.smooth();
				}
				Chunk ch = getChunk(x, y);
				List<MapTile> chunkBack = ch.getMapTilesFromGlobalPos(xs, ys, xe, ye);
				back.addAll(chunkBack);
			}
		}
		
		return back;
	}

	public static int globalPosToChunkPos(int gx) {
		return gx / Chunk.chunkSize;
	}

	public boolean exsistMapTile(int gx, int gy) {
		return getChunk(globalPosToChunkPos(gx), globalPosToChunkPos(gy)) != null;
	}

	public boolean exsistChunkTile(int cx, int cy) {
		return getChunk(cx, cy) != null;
	}

	public Chunk getChunkGlobalPos(int gx, int gy) {
		return getChunk(globalPosToChunkPos(gx), globalPosToChunkPos(gy));
	}

	public Chunk getChunk(int cx, int cy) {
		return chunks[cx][cy];
	}

	public Chunk setChunk(Chunk c) {
		return chunks[c.x][c.y] = c;
	}

	public MapTile getMapTile(Chunk c, int tx, int ty) {
		return getChunk(c.x, c.y).getMapTileFromLocalPos(tx, ty);
	}

	public GeneratorInterface getGenerator() {
		return generator;
	}

	public void setGenerator(GeneratorInterface generator) {
		this.generator = generator;
	}

}