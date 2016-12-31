package com.redagent.game;

import java.util.ArrayList;
import java.util.Collections;
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
import com.redagent.entitys.Cloud;
import com.redagent.entitys.Entity;
import com.redagent.entitys.LocalPlayer;
import com.redagent.materials.Grass;
import com.redagent.materials.Sand;
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

	public static int zoomLevel = 0;
	public static int zoomLevelmin = -2;
	public static int zoomLevelmax = 3;

	public static float xAmount = 8;
	// private int xAmountMax = 100;
	public static int xAmountMax = 250;
	public static int xAmountMin = 1;

	public boolean showInformations = true;

	private Entity track;

	public CameraController(int width, int height) {
		resize(width, height);
		setCamera(0, 0);

		font = new BitmapFont();
		font.setColor(Color.BLACK);
	}

	public void resize(int width, int height) {
		setScreenSize(width, height);
		initCamera();
		initFrameBuffer();
	}

	public float getXAmount() {
		return xAmount;
	}

	public float getOnePixelSize() {
		return getTileSizeByScreenSize() / MapTile.tileWidth;
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

	public void moveCamera(Vector2 v) {
		moveCamera(v.x, v.y);
	}

	public void setCamera(Vector2 v) {
		setCamera(v.x, v.y);
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

		if (track != null) {
			setCamera(track.getPosition());
		}

		int amountToShow = 2;

		int xStart = (int) (camera.position.x - amountToShow);
		int yStart = (int) (camera.position.y - amountToShow);
		int xEnd = (int) (xStart + amountToShow * 2);
		int yEnd = (int) (yStart + amountToShow * 2);

		List<MapTile> area = TileWorld.getInstance().getArea(xStart, yStart, xEnd, yEnd);

		area = new ArrayList<MapTile>();

		int xcenter = (int) (camera.position.x);
		int ycenter = (int) (camera.position.y);

		int safetytiles = 3;
		int breite = (int) (this.width / (MapTile.tileWidth * getZoomLevelScaleFactor())) + safetytiles;
		int h�he = (int) (this.height / (MapTile.tileHeight * getZoomLevelScaleFactor())) + safetytiles;

		for (int a = -h�he + 1; a < h�he; a++) {
			for (int b = -breite + 1; b < breite; b++) {
				if ((b & 1) != (a & 1))
					continue;
				int x = (a + b) / 2;
				int y = (a - b) / 2;
				area.add(TileWorld.getInstance().getMapTileFromGlobalPos(xcenter + x, ycenter + y));
			}
		}

		float size = getTileSizeByScreenSize();
		float scale = getOnePixelSize();

		Collections.sort(area);
		drawGround(area);

		List<Entity> spriteEntitys = new ArrayList<Entity>();
		// spriteEntitys.addAll(area);
		spriteEntitys.addAll(Main.getInstance().cloudHandler.getClouds());

		if (track != null) {
			spriteEntitys.add(track);
		}
		Collections.sort(spriteEntitys);

		for (Entity e : spriteEntitys) {
			if (e instanceof MapTile) {
				MapTile tile = (MapTile) e;
				float x = tile.getGlobalX() - camera.position.x + getXAmount() / 2;
				float y = tile.getGlobalY() - camera.position.y + getYAmount() / 2;

				Texture n = tile.getNatureTexture();
				if (n != null) {
					Sprite sprite = new Sprite(n);
					sprite.setPosition((x * size) + size / 2 - n.getWidth() / 2 * scale, (y * size));

					// fboBatch.draw(n, x * size, y * size, size / 2, size / 2,
					// n.getWidth() * scale,
					// n.getHeight() * scale, 1, 1, 0, 0, 0, n.getWidth(),
					// n.getHeight(), false, false);

					fboBatch.draw(sprite, sprite.getX(), sprite.getY(), size / 2, size / 2, sprite.getWidth() * scale,
							sprite.getHeight() * scale, sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());

					// Sprite test = new Sprite(sprite);
					// Color back = fboBatch.getColor();
					// fboBatch.setColor(back.r/2, back.g/2, back.b/2, 64);
					//
					// fboBatch.draw(test, sprite.getX(), sprite.getY(),
					// sprite.getOriginX(),sprite.getOriginY(),
					// sprite.getWidth(), sprite.getHeight(),
					// sprite.getScaleX(),
					// sprite.getScaleY(), tile.getRotation());
					//
					// fboBatch.setColor(back);

				}
			} else if (e instanceof Entity) {
				Entity t = (Entity) e;

				Vector2 trackPos = t.getPosition();
				Vector2 pos = new Vector2(trackPos.x - camera.position.x, trackPos.y - camera.position.y);
				pos.add(getXAmount() / 2, getYAmount() / 2);

				for (Sprite sprite : t.getSprite()) {

					float screenX = (pos.x - pos.y) * MapTile.tileWidth / 2;
					float screenY = (pos.x + pos.y) * MapTile.tileHeight / 2;

					sprite.setPosition((screenX), (screenY));

					// sprite.setRotation((float)
					// Math.toDegrees(track.body.getAngle()));

					fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
							sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(),
							sprite.getRotation());

				}
			} else if (e instanceof Cloud) {
				Cloud c = (Cloud) e;

				for (Sprite sprite : c.getSprite()) {
					Vector2 trackPos = c.getPosition();
					Vector2 pos = new Vector2(trackPos.x - camera.position.x, trackPos.y - camera.position.y);

					sprite.setPosition((pos.x * size) - sprite.getWidth() / 2 * scale,
							(pos.y * size) - sprite.getHeight() / 2 * scale);

					fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
							sprite.getWidth() * scale, sprite.getHeight() * scale, sprite.getScaleX(),
							sprite.getScaleY(), sprite.getRotation());

				}
			}

		}

		fboBatch.end();
		fbo.end();
	}

	private void drawGround(List<MapTile> area) {
		float size = getTileSizeByScreenSize();
		float scale = getOnePixelSize();

		float tileWidth = MapTile.tileWidth * getZoomLevelScaleFactor();
		float tileHeight = MapTile.tileHeight * getZoomLevelScaleFactor();
		float tileWidthHalf = tileWidth / 2.0f;
		float tileHeightHalf = tileHeight / 2.0f;

		MapTile bigger = area.get(area.size() / 2);

		for (MapTile tile : area) {
			Sprite sprite = new Sprite(tile.getMaterialTexture());
			float x = globalPosToScreenPosX(tile.getGlobalX(), tile.getGlobalY(), tileWidthHalf);
			float y = globalPosToScreenPosY(tile.getGlobalX(), tile.getGlobalY(), tileHeightHalf);

			if (tile == bigger) {
				Color save = fboBatch.getColor();

				fboBatch.setColor(save.cpy().add(-0.5f, -0.5f, -0.5f, 0));
				sprite = new Sprite(tile.getMaterialTexture());

				sprite.setPosition(x, y);
				sprite.setOrigin(tileWidthHalf, tileHeightHalf);
				sprite.setSize(sprite.getWidth() * getZoomLevelScaleFactor(),
						sprite.getHeight() * getZoomLevelScaleFactor());

				fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
						sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(),
						tile.getRotation());

				fboBatch.setColor(save);
			} else {
				sprite = new Sprite(tile.getMaterialTexture());

				sprite.setPosition(x, y);
				sprite.setOrigin(tileWidthHalf, tileHeightHalf);
				sprite.setSize(sprite.getWidth() * getZoomLevelScaleFactor(),
						sprite.getHeight() * getZoomLevelScaleFactor());

				fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
						sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(),
						tile.getRotation());
			}
		}
	}

	private float getZoomLevelScaleFactor() {
		return (float) Math.pow(2, zoomLevel);
	}

	private float globalPosToScreenPosX(int globalX, int globalY, float tileWidthHalf) {

		float oldY = (globalY - camera.position.y);
		float oldX = (globalX - camera.position.x);

		return (oldX - oldY) * tileWidthHalf + this.width / 2;
	}

	private int screenPosToGlobalPosX(float screenX) {
		float size = getTileSizeByScreenSize();
		return (int) (screenX / size + camera.position.x - getXAmount() / 2);
	}

	private float globalPosToScreenPosY(int globalX, int globalY, float tileHeightHalf) {

		float oldY = (globalY - camera.position.y);
		float oldX = (globalX - camera.position.x);

		return (oldX + oldY) * tileHeightHalf + this.height / 2;
	}

	private int screenPosToGlobalPosY(float screenY) {
		float size = getTileSizeByScreenSize();
		return (int) (screenY / size + camera.position.y - getYAmount() / 2);
	}

	static int line;

	public void renderToInformationBuffer() {
		if (showInformations) {
			fbo.begin();

			Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());

			// nicht clearen
			// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT |
			// GL20.GL_DEPTH_BUFFER_BIT);

			fboBatch.enableBlending();
			fboBatch.begin();

			// int x = (int) camera.position.x;
			// int y = (int) camera.position.y;

			Vector2 bodyPos = track.getPosition();

			MapTile standOn = TileWorld.getInstance().getMapTileFromGlobalPos((int) bodyPos.x, (int) bodyPos.y);

			line = 1;
			drawInformationLine("FPS: " + Gdx.graphics.getFramesPerSecond());

			if (track instanceof LocalPlayer) {
				LocalPlayer p = (LocalPlayer) track;
				drawInformationLine("Player: " + Main.getInstance().playerHandler.getPlayerNumber(p));
			}
			drawInformationLine("Zoom: xAmount:" + getXAmount());
			drawInformationLine("Body Chunk: " + standOn.chunk.x + "|" + standOn.chunk.y);
			drawInformationLine("Body Position: " + bodyPos.x + "|" + bodyPos.y);

			drawInformationLine("Stand On: " + standOn.material.texture);
			if (standOn.nature != null) {
				drawInformationLine("Nature: " + standOn.nature.texture);
			}
			// drawInformationLine("Dir: " +
			// track.body.getLinearVelocity().toString());

			Vector2 mousePos = Main.getInstance().inputHandler.keyboardHandler.mouse.pos.cpy();

			drawInformationLine("Mouse Pos: " + mousePos.toString());
			drawInformationLine(
					"Mouse Pos Gloabl: " + screenPosToGlobalPosX(mousePos.x) + "," + screenPosToGlobalPosY(mousePos.y));

			fboBatch.end();
			fbo.end();
		}
	}

	private void drawInformationLine(String s) {
		float z = font.getLineHeight();
		font.draw(fboBatch, s, 10, height - line * z);
		line++;
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
		zoomLevel += amount;
		if (zoomLevel < zoomLevelmin)
			zoomLevel = zoomLevelmin;
		if (zoomLevel > zoomLevelmax)
			zoomLevel = zoomLevelmax;
	}

	public void setTrack(Entity body) {
		this.track = body;
	}
}
