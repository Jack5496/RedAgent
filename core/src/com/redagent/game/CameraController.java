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
import com.redagent.helper.SpriteEntity;
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

	public static float xAmount = 16;
	// private int xAmountMax = 100;
	public static int xAmountMax = 250;
	public static int xAmountMin = 1;

	public boolean showInformations = true;

	private Entity track;

	public CameraController(int width, int height) {
		resize(width, height);
		setCamera(0,0);

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
		return getTileSizeByScreenSize() / MapTile.tileSize;
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
			setCamera(track.body.getPosition());
		}

		int xStart = (int) (camera.position.x - getXAmount() / 2) - 1;
		int yStart = (int) (camera.position.y - getYAmount() / 2) - 6;
		int xEnd = (int) (xStart + getXAmount() + 3);
		int yEnd = (int) (yStart + getYAmount() + 7);

		List<MapTile> area = TileWorld.getInstance().getArea(xStart, yStart, xEnd, yEnd);

		float size = getTileSizeByScreenSize();
		float scale = getOnePixelSize();

		drawGround(area);

		List<SpriteEntity> spriteEntitys = new ArrayList<SpriteEntity>();
		spriteEntitys.addAll(area);
		spriteEntitys.addAll(Main.getInstance().cloudHandler.getClouds());
		
		
		if (track != null) {
			spriteEntitys.add(track);
		}
		Collections.sort(spriteEntitys);

		for (SpriteEntity e : spriteEntitys) {
			if (e instanceof MapTile) {
				MapTile tile = (MapTile) e;
				float x = tile.getGlobalX() - camera.position.x + getXAmount() / 2;
				float y = tile.getGlobalY() - camera.position.y + getYAmount() / 2;

				Texture n = tile.getNatureTexture();
				if (n != null) {
					Sprite sprite = new Sprite(n);
					sprite.setPosition((x * size) + size/2-n.getWidth()/2*scale, (y * size));

					// fboBatch.draw(n, x * size, y * size, size / 2, size / 2,
					// n.getWidth() * scale,
					// n.getHeight() * scale, 1, 1, 0, 0, 0, n.getWidth(),
					// n.getHeight(), false, false);
					
					fboBatch.draw(sprite, sprite.getX(), sprite.getY(), size / 2, size / 2,
							sprite.getWidth() * scale, sprite.getHeight() * scale, sprite.getScaleX(),
							sprite.getScaleY(), sprite.getRotation());
					
//					Sprite test = new Sprite(sprite);
//					Color back = fboBatch.getColor();
//					fboBatch.setColor(back.r/2, back.g/2, back.b/2, 64);
//					
//					fboBatch.draw(test, sprite.getX(), sprite.getY(), sprite.getOriginX(),sprite.getOriginY(),
//							sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
//							sprite.getScaleY(), tile.getRotation());
//					
//					fboBatch.setColor(back);
					
				}
			}
			else if (e instanceof Entity) {
				Entity t = (Entity) e;

				Vector2 trackPos = t.getCoord();
				Vector2 pos = new Vector2(trackPos.x - camera.position.x, trackPos.y - camera.position.y);
				pos.add(getXAmount() / 2, getYAmount() / 2);

				for (Sprite sprite : t.getSprite()) {
					sprite.setPosition((pos.x * size) - sprite.getWidth() / 2 * scale,
							(pos.y * size) - sprite.getHeight() / 2 * scale);

					// sprite.setRotation((float)
					// Math.toDegrees(track.body.getAngle()));

					fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
							sprite.getWidth() * scale, sprite.getHeight() * scale, sprite.getScaleX(),
							sprite.getScaleY(), sprite.getRotation());
					
				

				}
			}
			else if(e instanceof Cloud){
				Cloud c = (Cloud) e;
				
				for (Sprite sprite : c.getSprite()) {
					Vector2 trackPos = c.getCoord();
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

		for (MapTile tile : area) {
			Sprite sprite = new Sprite(tile.getMaterialTexture());
			float x = globalPosToScreenPosX(tile.getGlobalX());
			float y = globalPosToScreenPosY(tile.getGlobalY());
			
			sprite.setPosition(x, y);
			sprite.setOrigin(size/2, size/2);
			sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
			
			fboBatch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),sprite.getOriginY(),
					sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
					sprite.getScaleY(), tile.getRotation());
			
			
		}
	}

	private float globalPosToScreenPosX(int globalX) {
		float size = getTileSizeByScreenSize();
		return (globalX - camera.position.x + getXAmount() / 2) * size;
	}

	private int screenPosToGlobalPosX(float screenX) {
		float size = getTileSizeByScreenSize();
		return (int) (screenX / size + camera.position.x - getXAmount() / 2);
	}

	private float globalPosToScreenPosY(int globalY) {
		float size = getTileSizeByScreenSize();
		return (globalY - camera.position.y + getYAmount() / 2) * size;
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

			Vector2 bodyPos = track.body.getPosition().cpy();

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
			drawInformationLine("Dir: " + track.body.getLinearVelocity().toString());

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
		xAmount += amount;
		if (xAmount < xAmountMin)
			xAmount = xAmountMin;
		if (xAmount > xAmountMax)
			xAmount = xAmountMax;
	}

	public void setTrack(Entity body) {
		this.track = body;
	}
}
