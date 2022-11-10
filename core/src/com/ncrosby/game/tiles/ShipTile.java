package com.ncrosby.game.tiles;

import com.badlogic.gdx.math.Vector2;
import com.ncrosby.game.GameObject;
import com.ncrosby.game.ID;
import com.ncrosby.game.tileShipGame;

import java.awt.*;

public class ShipTile extends GameObject {

	private AdjacentTiles neighbors = new AdjacentTiles();
	private final int xIndex, yIndex;

	private long placed = System.currentTimeMillis();
	private int cool = 0;
	//private Timer timer;
	public final static int TILESIZE = 64;

	/**
	 *  These tiles will all need health, and a way to relate to tiles next to them..?
	 *  But they will need to be stored in a 2d array. 
	 *  So when the game initializes there will need to be an array of tiles built out.
	*/
	public ShipTile(Vector2 position, ID id) {
		super(position, new Vector2(64,64), id);
		
		this.xIndex = (int) (position.x / TILESIZE);
		this.yIndex = (int) (position.y / TILESIZE);

		// Need to knit together the shiptile to adjacent tiles connectAdjacent();
	}

	public void tick() {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * render creates the square of the tile on the Graphics context.
//	 * @param g - Context for rendering images
//	 */
	public void render(tileShipGame game) {
		// This function is not needed anymore I think...
		// The ship is responsible for rendering the tiles it has
//		// Draw Tile
//		g.setColor(color);
//		g.fillRect(xLoc - cam.x, yLoc - cam.y, TILESIZE - 2, TILESIZE - 2);
//
//		coolPlacedBlock(g);
	}

	/**
	 * Method to cool a placed shipTile over time.
	 *
	 * @param g - Graphics context
	 */
	private void coolPlacedBlock(Graphics g){
//		long deltaTime = System.currentTimeMillis() - placed;
//		if(deltaTime  >= 150) {
//			//System.out.println( "r : " + (255 - cool) + "g : " + (0 + cool) + "b : " + (0 + cool));
//
//			if ( cool <= 200 ) {
//				cool = cool + 5;
//				placed = System.currentTimeMillis();
//			}
//		}
//
//		g.setColor(new Color(255 - cool, cool, cool));
//		g.drawRect(xLoc - cam.x, yLoc - cam.y, TILESIZE, TILESIZE);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getxIndex() {
		return xIndex;
	}

	public int getyIndex() {
		return yIndex;
	}

	/**
	 * Returns neighbors of ShipTile
	 * @return - AdjacentTile object holding references to neighbors
	 */
	public AdjacentTiles getNeighbors() {
		return neighbors;
	}
}
