package com.redagent.entitys;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.redagent.game.CameraController;
import com.redagent.game.Main;
import com.redagent.helper.VectorHelper;
import com.redagent.materials.Water;
import com.redagent.menu.MenuHandler;
import com.redagent.physics.Direction;
import com.redagent.physics.Speed;
import com.redagent.world.MapTile;
import com.redagent.world.TileWorld;

public class LocalPlayer extends Entity {

	public String name;

	/**
	 * InputVariables
	 */
	public Vector2 stickLeft;
	public boolean stickLeftDown;

	public Vector2 stickRight;

	public CameraController cameraController;
	public MenuHandler menuHandler;

	private float speed;
	
	private boolean sneaking;

	public int direction;

	public LocalPlayer(String name) {
		speed = Speed.runSpeed;
		this.name = name;
		menuHandler = new MenuHandler(this);
		direction = Direction.SOUTH;
		body = Main.getInstance().spawnPlayer();
		this.lastPos = body.getPosition().cpy();
		sneaking = false;
		initCamera();
		resetInputVariables();
	}

	public void sneak(boolean sneak){
		sneaking = sneak;
		if(sneak){
			speed = Speed.sneakSpeed;
		}
	}

	public void run(boolean run) {
		// true : false
		if (!sneaking) {
			speed = run ? Speed.runSpeed : Speed.walkSpeed;
		}
	}

	public void initCamera() {
		cameraController = new CameraController(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cameraController.setTrack(this);
	}

	public void move(Vector2 dir) {
		if (dir.len() != 0) {
			this.direction = VectorHelper.getDirFromVector(dir);
		}

		dir.scl(speed);
		body.setLinearVelocity(dir);

	}

	public void resetInputVariables() {
		stickLeft = new Vector2();
		stickRight = new Vector2();
	}

	@Override
	public List<Sprite> getSprite() {
		return PlayerSpriteCreator.getPlayerSprite(this);
	}

	public void updateMyGameObjects() {
		checkValidPosition();
	}

	private void checkValidPosition() {
		Vector2 pos = body.getPosition().cpy();

		if (!validPosition(pos)) {
			Vector2 diff = pos.cpy();
			diff = diff.sub(lastPos.cpy());

			Vector2 dx = diff.cpy();
			dx.x = 0;

			Vector2 dy = diff.cpy();
			dy.y = 0;

			Vector2 op1 = lastPos.cpy().add(dx);
			Vector2 op2 = lastPos.cpy().add(dy);

			Vector2 move = lastPos.cpy();

			if (validPosition(op1)) {
				move = op1.cpy();
			} else if (validPosition(op2)) {
				move = op2.cpy();
			}

			body.setTransform(move, body.getAngle());
		}

		lastPos = body.getPosition().cpy();
	}

	private boolean validPosition(Vector2 pos) {
		Vector2 v = pos.cpy();		
		
		try {
			MapTile my = TileWorld.getInstance().getMapTileFromGlobalPos((int) v.x, (int) v.y);
			if (my.material instanceof Water) {
				return false;
			}
			if (my.isSolid()) {
				return false;
			}
		} catch (NullPointerException e) {

		}

		return true;
	}

}
