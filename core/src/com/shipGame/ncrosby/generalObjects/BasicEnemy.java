package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.Camera;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.tileShipGame;

import java.awt.*;

import static com.shipGame.ncrosby.util.generalUtil.getRandomlyNegativeNumber;

public class BasicEnemy extends GameObject {

	int maxSpeed = 5;
	int minSpeed = 1;

	public BasicEnemy(Vector2 position, Vector2 size , ID id) {
		super(position, size, id);

		velX = (int)getRandomlyNegativeNumber(1,5);
		velY = (int)getRandomlyNegativeNumber(1,5);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) position.x, (int) position.y, 16, 16);
	}

	// TODO : burn this to the ground
	// This method is totally wrong. There's a lot of inappropriate intimacy going on here.
	// Each Game Object should not need a reference to the camera held inside it. We need to give the
	// Camera to the update method.
	public void tick() {
//		x += velX;
//		y += velY;
//
//		if(y <= 0 + cam.y || y >= legacyGame.HEIGHT - 55 + cam.y) {
//			y -= velY;
//			velY *= -1;
//		}
//		if(x <= 0 + cam.x || x >= legacyGame.WIDTH - 32 + cam.x) {
//			x -= velX;
//			velX *= -1;
//		}
//
//		// Extra stuff to make the enemy bounce off the ship
//		if(shiphandler.returnTile(x - velX - cam.x, y - cam.y) != null) {
//			y -= velY;
//			velY *= -1;
//		}
//		if(shiphandler.returnTile(x - cam.x, y - velX - cam.y) != null) {
//			x -= velX;
//			velX *= -1;
//		}
	}

	/**
	 * Draws basic enemy on screen relative to the camera
	 */
	public void render(tileShipGame game) {

//		generalUtil.render(position.x, position.y, new Texture("asteroid_purple.png"));
		
	}

}
