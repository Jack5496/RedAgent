package com.redagent.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.redagent.game.Main;
import com.redagent.game.ResourceLoader;
import com.redagent.helper.ArrayHelper;
import com.redagent.materials.Material;
import com.redagent.materials.Tree;

public class MapTile {

	public final static int tileSize = 64;

	public static int DIRECTION_SOUTH = 0;
	public static int DIRECTION_EAST = 1;
	public static int DIRECTION_NORTH = 2;
	public static int DIRECTION_WEST = 3;

	public int direction;
	private boolean solid;
	BodyDef body;

	public Material material;

	public Chunk chunk;
	public int x;
	public int y;

	public MapTile(Chunk c, int x, int y, boolean solid, int direction, Material m) {
		body = new BodyDef();
		this.x = x;
		this.y = y;
		this.chunk = c;
		setDirection(direction);
		setSolid(solid);
		this.material = m;
	}

	private MapTile getOffset(int xi, int yi) {
		xi = x + xi;
		yi = y + yi;

		if (xi < 0 || xi > Chunk.chunkSize || yi < 0 || yi > Chunk.chunkSize) {
			return null;
		}
		return chunk.getMapTileFromLocalPos(xi, yi);
	}

	public int getRotation() {
		return direction * 90;
	}

	public int getGlobalX() {
		return chunk.x * Chunk.chunkSize + x;
	}

	public int getGlobalY() {
		return chunk.y * Chunk.chunkSize + y;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
		if (solid) {
			body.type = BodyDef.BodyType.StaticBody;
		} else {
			// body.t
		}
	}

	public Texture getTexture() {
		return material.getTexture();
	}

	public boolean isSolid() {
		return this.solid;
	}

	public void setDirection(int dir) {
		this.direction = dir;
	}

	public MapTile getLeft() {
		return getOffset(-1, 0);
	}

	public MapTile getRight() {
		return getOffset(1, 0);
	}

	public MapTile getAbouve() {
		return getOffset(0, 1);
	}

	public MapTile getUnder() {
		return getOffset(0, -1);
	}

	public List<MapTile> getNeumann() {
		List<MapTile> back = new ArrayList<MapTile>();
		back.add(getLeft());
		back.add(getRight());
		back.add(getAbouve());
		back.add(getUnder());
		return back;
	}

	public List<MapTile> getMoore() {
		List<MapTile> back = getNeumann();
		back.add(getOffset(-1, -1));
		back.add(getOffset(-1, 1));
		back.add(getOffset(1, -1));
		back.add(getOffset(1, 1));
		return back;
	}

	// Bordering

	private boolean isSameMaterial(boolean[][] sameMaterial) {
		boolean back = true;
		int off = sameMaterial.length / 2;
		for (int x = 0; x < sameMaterial.length; x++) {
			for (int y = 0; y < sameMaterial[x].length; y++) {
				if (getOffset(x - off, y - off).material.isSame(material) != sameMaterial[x][y]) {
					back = false;
				}
			}
		}
		return back;
	}

	private boolean isSameMaterial(boolean[][]... checks) {
		for (boolean[][] check : checks) {
			if (isSameMaterial(check))
				return true;
		}
		return false;
	}

	// 2 5 8
	// 1 4 7
	// 0 3 6

	private List<boolean[][]> getInnerCorner() {
		boolean[][] all = { { false, true, true }, { true, true, true }, { true, true, true } };

		List<boolean[][]> back = new ArrayList<boolean[][]>();
		back.add(all);

		return back;
	}

	private boolean isInnerCorner(int d) {
		List<boolean[][]> checks = getInnerCorner();
		for (int i = 0; i < d; i++) {
			for (int x = 0; x < checks.size(); x++) {
				checks.set(x, ArrayHelper.rotateCounter(checks.get(x)));
			}
		}
		return isSameMaterial(checks.get(0));
	}

	public boolean isInnerCornerNorth() {
		return isInnerCorner(DIRECTION_NORTH);
	}

	public boolean isInnerCornerEast() {
		return isInnerCorner(DIRECTION_EAST);
	}

	public boolean isInnerCornerSouth() {
		return isInnerCorner(DIRECTION_SOUTH);
	}

	public boolean isInnerCornerWest() {
		return isInnerCorner(DIRECTION_WEST);
	}

	private List<boolean[][]> getOuterCornerTopRight() {
		boolean[][] three = { { false, false, true }, { false, true, true }, { true, true, true } };
		boolean[][] up = { { false, false, false }, { false, true, true }, { true, true, true } };
		boolean[][] down = { { false, false, true }, { false, true, true }, { false, true, true } };
		boolean[][] all = { { false, false, false }, { false, true, true }, { false, true, true } };
		
		List<boolean[][]> back = new ArrayList<boolean[][]>();
		back.add(three);
		back.add(all);
		back.add(up);
		back.add(down);


		return back;
	}

	private boolean isOuterCorner(int d) {
		List<boolean[][]> checks = getOuterCornerTopRight();
		for (int i = 0; i < d; i++) {
			for (int x = 0; x < checks.size(); x++) {
				checks.set(x, ArrayHelper.rotateCounter(checks.get(x)));
			}
		}
		return isSameMaterial(checks.get(0), checks.get(1), checks.get(2), checks.get(3));
	}

	public boolean isOuterCornerNorth() {
		return isOuterCorner(DIRECTION_NORTH);
	}

	public boolean isOuterCornerSouth() {
		return isOuterCorner(DIRECTION_SOUTH);
	}

	public boolean isOuterCornerEast() {
		return isOuterCorner(DIRECTION_EAST);
	}

	public boolean isOuterCornerWest() {
		return isOuterCorner(DIRECTION_WEST);
	}

	private List<boolean[][]> getStraightBorderSouth() {
		boolean[][] all = { { false, true, true }, { false, true, true, }, { false, true, true } };
		boolean[][] left = { { false, true, true }, { false, true, true }, { true, true, true } };
		boolean[][] right = { { true, true, true }, { false, true, true }, { false, true, true } };
		boolean[][] middle = { { true, true, true }, { false, true, true }, { true, true, true } };

		List<boolean[][]> back = new ArrayList<boolean[][]>();
		back.add(all);
		back.add(left);
		back.add(right);
		back.add(middle);
		return back;
	}

	private boolean checkStraightBorder(int d) {
		List<boolean[][]> checks = getStraightBorderSouth();
		for (int i = 0; i < d; i++) {
			for (int x = 0; x < checks.size(); x++) {
				checks.set(x, ArrayHelper.rotateCounter(checks.get(x)));
			}
		}
		return isSameMaterial(checks.get(0), checks.get(1), checks.get(2), checks.get(3));
	}

	public boolean isStraightBorderNorth() {
		return checkStraightBorder(DIRECTION_NORTH);
	}

	public boolean isStraightBorderEast() {
		return checkStraightBorder(DIRECTION_EAST);
	}

	public boolean isStraightBorderSouth() {
		return checkStraightBorder(DIRECTION_SOUTH);
	}

	public boolean isStraightBorderWest() {
		return checkStraightBorder(DIRECTION_WEST);
	}

}