package com.redagent.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.redagent.Inputs.InputHandler;
import com.redagent.entitys.CloudHandler;
import com.redagent.entitys.LocalPlayer;
import com.redagent.entitys.LocalPlayerHandler;
import com.redagent.world.Chunk;
import com.redagent.world.TileWorld;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	ResourceLoader resourceLoader;
	TileWorld tileWorld;

	public static void log(Class<?> c, String log) {
		System.out.println(c.getSimpleName() + ": " + log);
	}

	public Sprite sprite, sprite2;
	public World world;
	Body body, body2;
	Body bodyEdgeScreen;

	Matrix4 debugMatrix;
	OrthographicCamera camera;
	


	final static float PIXELS_TO_METERS = 100f;

	public final short PHYSICS_ENTITY = 0x1; // 0001
	public final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
	
	
	private static Main instance;
	public LocalPlayerHandler playerHandler;
	public InputHandler inputHandler;
	public CloudHandler cloudHandler;

	public void initResourceLoader() {
		resourceLoader = new ResourceLoader();
	}

	public static Main getInstance() {
		return instance;
	}

	public void initTileWorld() {
		tileWorld = new TileWorld();
	}
	
	public void initPlayerHandler() {
		playerHandler = new LocalPlayerHandler();
	}
	
	public void initCloudHandler() {
		cloudHandler = new CloudHandler();
	}

	public void initInputHandler() {
		inputHandler = new InputHandler();
	}

	public float gravityV = 10;

	public Body spawnPlayer() {
		// Sprite1's Physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(51721, 50811);

		body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		Sprite s = new Sprite(ResourceLoader.getInstance().getShaddow("entity"));
		shape.setAsBox(s.getWidth()/2, s.getHeight()/2);

		// Sprite1
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.5f;
		fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
		fixtureDef.filter.maskBits = WORLD_ENTITY;

		body.createFixture(fixtureDef);

		return body;
	}

	@Override
	public void create() {
		instance = this;
		initResourceLoader();
		initTileWorld();
		initPlayerHandler();
		initCloudHandler();
		initInputHandler();

		batch = new SpriteBatch();
		world = new World(new Vector2(0, -gravityV), true);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void resize(int width, int height) {
//	    viewport.update(width, height);
	}

	@Override
	public void render() {
		inputHandler.updateInputLogic();
		
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f / 60f, 6, 2);
		updateEntitysInputs();
		updateCloudPositions();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderForPlayers();
	}
	
	public void updateCloudPositions(){
		cloudHandler.updateClouds();
	}
	
	public void updateEntitysInputs() {
		LocalPlayer[] players = playerHandler.getPlayers();

		for (LocalPlayer p : players) {
			p.updateMyGameObjects();
		}
	}

	public void renderForPlayers() {
		LocalPlayer[] players = playerHandler.getPlayers();

		for (LocalPlayer p : players) {
			p.cameraController.renderToFrameBuffer();
			p.cameraController.renderToInformationBuffer();
			p.cameraController.renderToScreen();
		}
	}

	@Override
	public void dispose() {
		LocalPlayer[] players = playerHandler.getPlayers();

		for (LocalPlayer p : players) {
			p.cameraController.dispose();
		}

		world.dispose();
	}
}
