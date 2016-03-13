package com.redagent.worldgenerator;

import java.util.Random;

import com.redagent.game.Main;
import com.redagent.world.Amortized2DNoise;
import com.redagent.world.Chunk;
import com.redagent.world.TileWorld;

public class NatureGenerator implements GeneratorInterface {

	TileWorld world;

	// Altering will make it look more like continents or islands.
	public static float seaLevel = 0.1f;

	// How much sand in the shore is created
	public static float sandAmount = 0.1f;
	public static Random random;

	// Alter this value for different results
	public static int octave0 = 2;
	public static int octave1 = 8;
	public static Amortized2DNoise noise;

	public NatureGenerator(TileWorld world) {
		this.world = world;
		random = new Random();
		noise = new Amortized2DNoise(Amortized2DNoise.CELLSIZE2D);
	}

	@Override
	public void generateChunkAt(int cx, int cy) {
		Main.log(getClass(), "Chunk("+cx+"|"+cy+"): generating");
		Chunk c = new Chunk();
		c.create(cx, cy, noise);
		world.setChunk(c);
		Main.log(getClass(), "Chunk("+cx+"|"+cy+"): ready");
	}

}