package com.ncrosby.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Player extends GameObject {

    Random r = new Random();
    private tileShipGame game;
    private OrthographicCamera cam;
    private int index[] = {0, 0};
    private int lookAhead = 155;
    public boolean godMode = false;

    // Tiles the player holds - This should be reflected on the player's sprite
    public Array<ShipTile> heldShipTiles = new Array<>();
    private int heldTileLimit;
    // Circle to render held tiles on.
    private Circle heldTileDisplay;
    private int INIT_HELD_RADIUS = 3;

    public Player(Vector2 position, ID id, OrthographicCamera cam, tileShipGame game) {
        super(position, new Vector2(64, 64), id);

        heldTileDisplay = new Circle(position.x, position.y, INIT_HELD_RADIUS);
        this.cam = cam;
        this.game = game;
        this.heldTileLimit = 1;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle((int) playerPosition.x, (int) playerPosition.y, 32, 32);
        //r.intersects
        return r;

    }

    public void tick() {

//		y += velY;
//		x += velX;
//
//		// Checking middle of player ( - 16)
//		// Checking for each dimension separately.
//
//		int oldX = x + 16 - velX - cam.x;
//		int yLookaheadCam = y + 16  - cam.y;
//
//		if(shiphandler.returnTile(oldX, yLookaheadCam) == null) {
//			//System.out.println("Y out of bounds");
//			y -= velY;
//		}
//		if (shiphandler.returnTile(x + 16 - cam.x, y + 16 - velY  - cam.y) == null) {
//			//System.out.println("X out of bounds");
//			x -= velX;
//		}
//
//		// Build camera lookahead here
//		// Need to check distance from edge of screen.
//		// This means the x,y of the player never goes off the screen.
//		// The camera is the thing that changes.
//		if(x <= (lookAhead + cam.x)) {
//			cam.x += velX;
//		}
//		if(y <= (lookAhead + cam.y - 35)) {
//			// Velocity is negative when moving up lol
//			cam.y += velY;
//		}
//		if(x >= WIDTH - lookAhead - 45 + cam.x) {
//			cam.x += velX;
//		}
//		if(y >= HEIGHT - lookAhead - 45 + cam.y) {
//			cam.y += velY;
//		}
		
		/*x = game.clamp(x, 0, game.WIDTH - 48);
		y = game.clamp(y, 0, game.HEIGHT - 69);*/

    }

    /**
     * Render more complex features of the player
     *
     * @param game - reference to the game context
     */
    public void render(tileShipGame game) {
        // Update radius and position of circle
        adjustHeldTileDisplay(0.1f);
        heldTileDisplay.setPosition(playerPosition);
//        System.out.println("Held tiles : " + this.heldShipTiles.size);
        // Draw tiny tiles from player stack
        for (int i = 0; i < heldShipTiles.size; i++) {

        }
    }

    private void adjustHeldTileDisplay(float lerp) {
        heldTileDisplay.radius += (
                // need to fiddle with this exp. so the value stablizes and goes to 0.
                (heldTileDisplay.radius - heldShipTiles.size) * // display radius changes based on the size of the hand of tiles.
                        lerp * Gdx.graphics.getDeltaTime()
        );
    }

    public void mouseover(int[] index) {
        this.index = index;
    }

    /**
     * This may get reworked so we can get the ship currently used by the player.
     * <p>
     * It doesn't work currently
     *
     * @return
     */
    public Ship getPlayerShip() {
//		return shiphandler;
        return null;
    }

    public Array<ShipTile> getHeldShipTiles() {
        return heldShipTiles;
    }

    public void setHeldShipTiles(Array<ShipTile> heldShipTiles) {
        this.heldShipTiles = heldShipTiles;
    }

    /**
     * Switches god mode to opposite state when called.
     */
    public void toggleGodMode() {
        System.out.println("godMode is set to : " + godMode);
        if (this.godMode == false) {
            this.godMode = true;
        } else this.godMode = false;
    }

    /**
     * Adds a shipTile to the players' stack
     *
     * @param st
     * @return
     */
    public boolean pickupTile(ShipTile st) {
        if (heldShipTiles.size == heldTileLimit) {
            System.out.println("Held tiles at limit!");
            return false;
        } else if (heldShipTiles.size > heldTileLimit) {
            throw new RuntimeException("Holding more tiles than is possible to hold : " +
                    Arrays.toString(Thread.currentThread().getStackTrace()));
        } else {
            heldShipTiles.add(st);
            return true;
        }
    }

    /**
     * Method that returns the currently held tile and removes it from the player.
     *
     * @return - Returns tile from stack if holding tile, else return NULL
     */
    public ShipTile popTile() {
        if (heldShipTiles.size == 0) {
            System.out.println("Holding (" + heldShipTiles.size + ") tiles!");
            return null;
        } else if (heldShipTiles.size > 0) {
            System.out.println("Holding (" + heldShipTiles.size + ") tiles!\n" +
                    " Returning tile with ID : " + heldShipTiles.peek().getID());
            return heldShipTiles.pop();
        } else {
            throw new RuntimeException("Error with placeTile" + Arrays.toString(Thread.currentThread().getStackTrace()));
        }
    }

}
