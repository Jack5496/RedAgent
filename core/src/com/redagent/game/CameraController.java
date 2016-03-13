package com.redagent.game;

import java.awt.Font;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.redagent.player.Entity;
import com.redagent.world.Chunk;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class CameraController {

	public OrthographicCamera camera;

	private FrameBuffer fbo;
	private FrameBuffer fbUI;

	private SpriteBatch fboBatch;
	private BitmapFont font;

	private int width;
	private int height;

	private float xAmount = 16;
	private int distanceMax = 100;
	private int distanceMin = 1;

	private Entity track;

	public CameraController(int width, int height) {
		setScreenSize(width, height);
		initCamera();
		initFrameBuffer();
		// setCamera(Chunk.chunkSize * TileWorld.worldSize / 2, Chunk.chunkSize
		// * TileWorld.worldSize / 2);
		setCamera(51715, 50831);

		// xAmount = this.width / MapTile.tileSize;
		// yAmount = this.height / MapTile.tileSize;

		font = new BitmapFont();
		font.setColor(Color.BLACK);
	}

	public float getXAmount() {
		return xAmount;
	}

	public float getTileSizeByScreenSize() {
		return this.width / xAmount;
	}

	public float getYAmount() {
		return this.height / getTileSizeByScreenSize();
	}

	public void moveCamera(float x, float y) {
		setCamera(camera.position.x + x, camera.position.y + y);
	}
	
	public void moveCamera(Vector2 v){
		moveCamera(v.x,v.y);
	}
	
	public void setCamera(Vector2 v) {
		setCamera(v.x,v.y); 
	}

	public void setCamera(float x, float y) {
		camera.position.x = x;
		camera.position.y = y;
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

		fbUI = new FrameBuffer(Format.RGBA8888, width, height, true);
		fbUI.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

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

//		if (track != null) {
//			setCamera(track.body.getPosition());
//		}

		int s = 1;

		int camX = (int) (camera.position.x);
		int camY = (int) (camera.position.y);

		int xStart = camX;
		int yStart = camY-1;
		int xEnd = (int) (xStart + getXAmount() +1);
		int yEnd = (int) (yStart + getYAmount()+2);

		// int xEnd = xStart +1;
		// int yEnd = yStart +2;

		List<MapTile> area = TileWorld.getInstance().getArea(xStart, yStart, xEnd, yEnd);

		float size = getTileSizeByScreenSize();

		for (MapTile tile : area) {
			Texture t = tile.getTexture();
			float x = tile.getGlobalX() - camera.position.x;
			float y = tile.getGlobalY() - camera.position.y;

			fboBatch.draw(t, x * size, y * size, size / 2, size / 2, size, size, 1, 1, tile.getRotation(), 0, 0,
					t.getWidth(), t.getHeight(), false, false);
		}

		// if (track != null) {
		// Coord pos = cameraPosition.getDifference(track.getCoord());
		// pos.add(width / 2, height / 2);
		//
		// Sprite sprite = Main.getInstance().sprite;
		// sprite.setPosition((pos.x) - sprite.getWidth() / 2, (pos.y) -
		// sprite.getHeight() / 2);
		//
		// Main.getInstance().sprite.setRotation((float)
		// Math.toDegrees(track.body.getAngle()));
		//
		// fboBatch.draw(sprite, sprite.getX(), sprite.getY(),
		// sprite.getOriginX(), sprite.getOriginY(),
		// sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
		// sprite.getScaleY(),
		// sprite.getRotation());
		//
		// }

		fboBatch.end();
		fbo.end();
	}

	public void renderToUIBuffer() {
		fbo.begin();

		Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());

		// nicht clearen
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		fboBatch.enableBlending();
		fboBatch.begin();

		float z = font.getLineHeight();

		int x = (int) camera.position.x;
		int y = (int) camera.position.y;

		font.draw(fboBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, height - 1 * z);
		font.draw(fboBatch, "Camera Position: " + x + "|" + y, 10, height - 2 * z);
		font.draw(fboBatch, "Zoom: xAmount:" + getXAmount(), 10, height - 3 * z);
		fboBatch.end();
		fbo.end();
	}

	public void renderUI() {

	}

	public void renderToScreen() {
		fboBatch.begin();
		fboBatch.draw(fbo.getColorBufferTexture(), 0, 0, width, height, 0, 0, 1, 1);
		fboBatch.draw(fbUI.getColorBufferTexture(), 0, 0, width, height, 0, 0, 1, 1);
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

	public void changeDistance(float amount) {
		xAmount += amount;
		if (xAmount < 1)
			xAmount = 1;
		if (xAmount > 100)
			xAmount = 100;
	}

	public void setTrack(Entity body) {
		this.track = body;
	}
}
