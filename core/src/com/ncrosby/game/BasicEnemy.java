package com.ncrosby.game;

import com.ncrosby.game.main.legacyGame;

import java.awt.*;

public class BasicEnemy extends GameObject {

	private Ship shiphandler;
	private Camera cam;

	public BasicEnemy(int x, int y, ID id, Ship shiphandler, Camera cam) {
		super(x, y, id);
		
		// Cam is part of the game object.
		this.cam = cam;
		this.shiphandler = shiphandler;
		velX = 5; 
		velY = 5;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 16, 16);
	}
	
	public void tick() {
		x += velX;
		y += velY;
		
		if(y <= 0 + cam.y || y >= legacyGame.HEIGHT - 55 + cam.y) {
			y -= velY;
			velY *= -1;
		}
		if(x <= 0 + cam.x || x >= legacyGame.WIDTH - 32 + cam.x) {
			x -= velX;
			velX *= -1;
		}
		
		// Extra stuff to make the enemy bounce off the ship
		if(shiphandler.returnTile(x - velX - cam.x, y - cam.y) != null) {
			y -= velY;
			velY *= -1;
		}
		if(shiphandler.returnTile(x - cam.x, y - velX - cam.y) != null) {
			x -= velX;
			velX *= -1;
		}
	}

	/**
	 * Draws basic enemy on screen relative to the camera
	 */
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x - cam.x, y - cam.y, 16, 16);
		
	}

}
