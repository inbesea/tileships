package com.ncrosby.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.awt.*;
import java.util.Random;

import static com.ncrosby.game.main.legacyGame.HEIGHT;
import static com.ncrosby.game.main.legacyGame.WIDTH;

public class Player extends GameObject {

	Random r = new Random();
	private tileShipGame game;
	private OrthographicCamera cam;
	private int index[] = {0, 0};
	private int lookAhead = 155;
	public boolean godMode = false;
	public ShipTile shipTile;
	
	public Player(int x, int y, ID id, OrthographicCamera cam, tileShipGame game) {
		super(x, y, id);
		
		this.cam = cam;
		this.game = game;
		//shiphandler.addTileByCoord(x, y, ID.ShipTile, Color.cyan, cam);
		//velX = r.nextInt(5);
		//velY = r.nextInt(5);
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle(x, y, 32, 32);
		//r.intersects
		return r;
		
	}

	public void tick() {
		
		y += velY;
		x += velX;
		
		// Checking middle of player ( - 16)
		// Checking for each dimension separately. 
		
		int oldX = x + 16 - velX - cam.x;
		int yLookaheadCam = y + 16  - cam.y;
		
		if(shiphandler.returnTile(oldX, yLookaheadCam) == null) {
			//System.out.println("Y out of bounds");
			y -= velY;
		} 
		if (shiphandler.returnTile(x + 16 - cam.x, y + 16 - velY  - cam.y) == null) {
			//System.out.println("X out of bounds");
			x -= velX;
		}
		
		// Build camera lookahead here
		// Need to check distance from edge of screen. 
		// This means the x,y of the player never goes off the screen. 
		// The camera is the thing that changes. 
		if(x <= (lookAhead + cam.x)) {
			cam.x += velX;
		}
		if(y <= (lookAhead + cam.y - 35)) {
			// Velocity is negative when moving up lol 
			cam.y += velY;
		}
		if(x >= WIDTH - lookAhead - 45 + cam.x) {
			cam.x += velX;
		}
		if(y >= HEIGHT - lookAhead - 45 + cam.y) {
			cam.y += velY;
		}
		
		/*x = game.clamp(x, 0, game.WIDTH - 48);
		y = game.clamp(y, 0, game.HEIGHT - 69);*/
		
	}

	public void render() {
		//shiphandler.render(g);
//		g.setColor(Color.white);
//		g.fillRect(x - cam.x, y - cam.y, 32, 32);
//		if(this.shipTile != null) {
//			g.setColor(shipTile.getTexture());
//			g.fillRect(x + 5 - cam.x, y + 5 - cam.y, 10, 10);
//		}
		//g.drawRect(index[0], index[1], ShipTile.TILESIZE, ShipTile.TILESIZE);
	}
	
	public void mouseover(int[] index) {
		this.index = index;
	}
	
	public Ship getPlayerShip() {
		return shiphandler;
	}
	
	/**
	 * Switches god mode to opposite state when called. 
	 */
	public void toggleGodMode() {
		System.out.println("godMode is set to : " + godMode);
		if (this.godMode == false) {
			this.godMode = true;
		}
		else this.godMode = false;
	}
	
	/**
	 * Sets the shipTile for the player to a passed in ShipTile
	 * @param st
	 */
	public void pickupTile(ShipTile st) {
		this.shipTile = st;
	}
	
	/**
	 * Method that returns the currently held tile and removes it from the player. 
	 * @return
	 */
	public ShipTile placeTile() {
		ShipTile st = this.shipTile;
		this.shipTile = null;
		return st;
	}
	
}
