package com.redagent.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.redagent.helper.ArrayHelper;
import com.redagent.helper.SpriteEntity;
import com.redagent.materials.Material;
import com.redagent.nature.Nature;
import com.redagent.physics.Direction;

public class MapTile extends SpriteEntity{

	public final static int tileSize = 64;

	public int direction;
	private boolean solid;

	public Material material;
	public Nature nature;

	public Chunk chunk;

	public MapTile(Chunk c, int x, int y, boolean solid, int direction, Material m) {
		lastPos = new Vector2(x,y);
		this.chunk = c;
		setDirection(direction);
		setSolid(solid);
		this.material = m;
	}

	private MapTile getOffset(int xi, int yi) {
		xi = (int) (lastPos.x + xi);
		yi = (int) (lastPos.y + yi);

		if (xi < 0 || xi > Chunk.chunkSize || yi < 0 || yi > Chunk.chunkSize) {
			return null;
		}
		return chunk.getMapTileFromLocalPos(xi, yi);
	}

	public int getRotation() {
		return direction * 90;
	}

	public int getGlobalX() {
		return (int) (chunk.x * Chunk.chunkSize + lastPos.x);
	}

	public int getGlobalY() {
		return (int) (chunk.y * Chunk.chunkSize + lastPos.y);
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public Texture getMaterialTexture() {
		return material.getTexture();
	}

	public Texture getNatureTexture() {
		if (nature == null) {
			return null;
		}
		return nature.getTexture();
	}

	public boolean isSolid() {
		return this.solid;
	}

	public void setDirection(int dir) {
		this.direction = dir;
	}

	public MapTile getTileInDirection(int direction) {
		int x = 0;
		int y = 0;

		switch (direction) {
		case Direction.NORTH:
			y++;
			break;
		case Direction.EAST:
			x++;
			break;
		case Direction.SOUTH:
			y--;
			break;
		case Direction.WEST:
			x--;
			break;
		default:
			return null;
		}

		return getOffset(x, y);
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
		return isInnerCorner(Direction.NORTH);
	}

	public boolean isInnerCornerEast() {
		return isInnerCorner(Direction.EAST);
	}

	public boolean isInnerCornerSouth() {
		return isInnerCorner(Direction.SOUTH);
	}

	public boolean isInnerCornerWest() {
		return isInnerCorner(Direction.WEST);
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
		return isOuterCorner(Direction.NORTH);
	}

	public boolean isOuterCornerSouth() {
		return isOuterCorner(Direction.SOUTH);
	}

	public boolean isOuterCornerEast() {
		return isOuterCorner(Direction.EAST);
	}

	public boolean isOuterCornerWest() {
		return isOuterCorner(Direction.WEST);
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
		return checkStraightBorder(Direction.NORTH);
	}

	public boolean isStraightBorderEast() {
		return checkStraightBorder(Direction.EAST);
	}

	public boolean isStraightBorderSouth() {
		return checkStraightBorder(Direction.SOUTH);
	}

	public boolean isStraightBorderWest() {
		return checkStraightBorder(Direction.WEST);
	}

}