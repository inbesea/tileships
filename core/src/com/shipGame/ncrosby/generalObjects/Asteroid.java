package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.StrongTile;
import com.shipGame.ncrosby.tileShipGame;

import java.awt.*;

import static com.shipGame.ncrosby.util.generalUtil.getRandomlyNegativeNumber;

public class Asteroid extends GameObject {

	int maxSpeed = 5;
	int minSpeed = 1;
	Circle circle;
	float radius;
	public Asteroid(Vector2 position, Vector2 size , ID id) {
		super(position, size, id);

		velX = getRandomlyNegativeNumber(0.5f,5);
		velY = getRandomlyNegativeNumber(0.5f,5);
		radius = size.y * 0.5f;
		circle = new Circle(position.x + radius, position.y + radius, radius);
	}

	/**
	 * Create asteroid with specific velocity
	 * @param position
	 * @param size
	 * @param velocity
	 * @param id
	 */
	public Asteroid(Vector2 position, Vector2 size, Vector2 velocity, ID id) {
		super(position, size, id);

		velX = velocity.x;
		velY = velocity.y;
		radius = size.y * 0.5f;
		circle = new Circle(position.x + radius, position.y + radius, radius);
	}

	public Rectangle getBounds(){
		return null;
	}

	public Circle getCircleBounds() {
		return circle;
	}

	@Override
	public void collision(GameObject gameObject) {
			bounce(gameObject);
	}

	private void bounce(GameObject gameObject) {
		if(gameObject instanceof StrongTile){
			StrongTile strongTile = (StrongTile) gameObject;
//			strongTile.
			// ToDO : We want the strong tile to bounce the asteroid correctly so it looks nice.
			// It bounces dumb right now.
		}
		velX *= -1;
		velY *= -1;
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

		position.x += velX;
		position.y += velY;
		circle.x += velX;
		circle.y += velY;
//		generalUtil.render(position.x, position.y, new Texture("asteroid_purple.png"));
		
	}

}
