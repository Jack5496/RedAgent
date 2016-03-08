package com.redagent.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.redagent.player.Entity;
import com.redagent.world.Coord;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class CameraController {

	public OrthographicCamera camera;

	private int distance;
	private int distanceMax;
	private int distanceMin;

	private FrameBuffer fbo;
	private SpriteBatch fboBatch;

	private int width;
	private int height;

	private Entity track;

	private Coord cameraPosition;

	public CameraController(int width, int height) {
		setScreenSize(width, height);
		initCamera();
		initFrameBuffer();
		setCamera(0, 0);
	}

	public void moveCamera(float x, float y) {
		setCamera(cameraPosition.x + x, cameraPosition.y + y);
	}

	public void setCamera(float x, float y) {
		cameraPosition = new Coord(x, y);
	}

	private void setScreenSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private void initCamera() {
		camera = new OrthographicCamera(width, height);
	}

	private void initFrameBuffer() {
		fbo = new FrameBuffer(Format.RGBA8888, width, height, true);
		fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		if (fboBatch != null)
			fboBatch.dispose();
		fboBatch = new SpriteBatch();
	}

	public void renderToFrameBuffer() {
		camera.update();

		fbo.begin();

		Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		fboBatch.begin();

		if (track != null) {
			cameraPosition = new Coord(track.body.getPosition());
		}

		int s = 1;

		int xStart = (int) (cameraPosition.x / s) * s - 1 * s;
		int yStart = (int) (cameraPosition.y / s) * s - 1 * s;
		int xEnd = xStart + width/MapTile.tileSize + 1 * s;
		int yEnd = yStart + height/MapTile.tileSize + 1 * s;

		Coord begin = new Coord(xStart, yStart);
		Coord end = new Coord(xEnd, yEnd);
		
//		begin = new Coord(s*5, s*5);
//		end = new Coord(s*6, s*5);
		
		
		
		for (MapTile tile : TileWorld.getInstance().getArea(begin, end)) {
			Coord c = cameraPosition.getDifference(tile.getCoord());
			Texture t = tile.getTexture();
			fboBatch.draw(t, c.x*MapTile.tileSize, c.y*MapTile.tileSize, t.getWidth() / 2, t.getHeight() / 2, t.getWidth(), t.getHeight(), 1, 1,
					tile.getRotation(), 1, 1, t.getWidth(), t.getHeight(), false, false);
		}

		if (track != null) {
			Coord pos = cameraPosition.getDifference(track.getCoord());
			pos.add(width / 2, height / 2);

			Sprite sprite = Main.getInstance().sprite;
			sprite.setPosition((pos.x) - sprite.getWidth() / 2, (pos.y) - sprite.getHeight() / 2);

			Main.getInstance().sprite.setRotation((float) Math.toDegrees(track.body.getAngle()));

			fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
					sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(),
					sprite.getRotation());

		}

		fboBatch.end();
		fbo.end();
	}

	public void renderToScreen() {
		fboBatch.begin();
		fboBatch.draw(fbo.getColorBufferTexture(), 0, 0, width, height, 0, 0, 1, 1);
		fboBatch.end();
	}

	public void distanceIncrease() {
		changeDistance(1);
	}

	public void distanceDecrease() {
		changeDistance(-1);
	}

	public void dispose() {
		fbo.dispose();
		fboBatch.dispose();
	}

	private void changeDistance(int amount) {
		distance += amount;
		if (distance < distanceMin)
			distance = distanceMin;
		if (distance > distanceMax)
			distance = distanceMax;
	}

	public void setTrack(Entity body) {
		this.track = body;
	}
}
