package com.ncrosby.game;

import java.awt.*;

public class ShipTile extends GameObject{

	
	private ShipTile Up;
	private ShipTile Left;
	private ShipTile Right;
	private ShipTile Down;
	
	private final int xLoc;
	private final int yLoc;

	private Color color;
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private long placed = System.currentTimeMillis();
	private int cool = 0;
	//private Timer timer;
	private final Camera cam;
	public final static int TILESIZE = 64;

	/**
	 *  These tiles will all need health, and a way to relate to tiles next to them..?
	 *  But they will need to be stored in a 2d array. 
	 *  So when the game initializes there will need to be an array of tiles built out. 
	 *  @param x - The x index of this tile (will be rendered at x*TILESIZE)
	 *  @param y - The y index of this tile (will be rendered at y*TILESIZE)
	*/
	public ShipTile(int x, int y, ID id, Camera cam) {
		super(x, y, id);
		this.Up = null;
		this.Left = null;
		this.Right = null;
		this.Down = null;
		
		this.xLoc = x * TILESIZE;
		this.yLoc = y * TILESIZE;
		
		this.cam = cam;
		// This is given to the tile now, but probably needs to be changed so the 
		// appearance is determined by the type instead. 
		if(id == ID.ShipTile) {
			this.color = Color.blue;
		}else if(id == ID.CoreTile) {
			this.color = Color.orange;
		}
		


		// this.render = render;
		// Is this necessary? 
		// If the element in the 2d array is null then we can just skip it? 
		// That will result in fewer elements == better runtime. 
		// Idea : The renderer for the 2d array with tiles for the ship can have a list of existing ship squares. 
		
	}

	public void tick() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * render creates the square of the tile on the Graphics context. 
	 * @param g - Context for rendering images
	 */
	public void render(Graphics g) {
		// Draw Tile
		g.setColor(color);
		g.fillRect(xLoc - cam.x, yLoc - cam.y, TILESIZE - 2, TILESIZE - 2);

		coolPlacedBlock(g);
	}

	/**
	 * Method to cool a placed shipTile over time.
	 *
	 * @param g - Graphics context
	 */
	private void coolPlacedBlock(Graphics g){
		long deltaTime = System.currentTimeMillis() - placed;
		if(deltaTime  >= 150) {
			//System.out.println( "r : " + (255 - cool) + "g : " + (0 + cool) + "b : " + (0 + cool));

			if ( cool <= 200 ) {
				cool = cool + 5;
				placed = System.currentTimeMillis();
			}
		}

		g.setColor(new Color(255 - cool, cool, cool));
		g.drawRect(xLoc - cam.x, yLoc - cam.y, TILESIZE, TILESIZE);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public ShipTile getUp() {
		return Up;
	}

	public void setUp(ShipTile up) {
		Up = up;
	}

	public ShipTile getLeft() {
		return Left;
	}

	public void setLeft(ShipTile left) {
		Left = left;
	}

	public ShipTile getRight() {
		return Right;
	}

	public void setRight(ShipTile right) {
		Right = right;
	}

	public ShipTile getDown() {
		return Down;
	}

	public void setDown(ShipTile down) {
		Down = down;
	}

}
