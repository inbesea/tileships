package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.AdjacentTiles;
import com.shipGame.ncrosby.tileShipGame;

import java.awt.*;

public class ShipTile extends GameObject {

	private final AdjacentTiles neighbors = new AdjacentTiles();
	private final int xIndex, yIndex;

	private long placed = System.currentTimeMillis();
	private int cool = 0;
	public boolean isEdge;
	public final static int TILESIZE = 64;
	private com.badlogic.gdx.math.Rectangle rectangle;
	/**
	 *  These tiles will all need health, and a way to relate to tiles next to them..?
	 *  But they will need to be stored in a 2d array. 
	 *  So when the game initializes there will need to be an array of tiles built out.
	*/
	public ShipTile(Vector2 position, ID id) {
		super(position, new Vector2(64,64), id);
		
		this.xIndex = (int) (position.x / TILESIZE);
		this.yIndex = (int) (position.y / TILESIZE);

		rectangle = new com.badlogic.gdx.math.Rectangle(position.x, position.y ,ShipTile.TILESIZE, ShipTile.TILESIZE);
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
		return rectangle;
	}

	@Override
	public void collision(GameObject gameObject) {

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

	public boolean isEdge() {
		return isEdge;
	}

	public void setIsEdge(boolean isEdge){
		this.isEdge = isEdge;
	}

	/**
	 * Delegate method that handles setting the neighbors of a newly placed tile.
	 * Keeps the edge calculations down in the tiles instead of on the ship level to keep the logic cleaner on the ship level
	 *
	 * Ship still determines the context by passing the adjacent tiles
	 * @param up
	 * @param right
	 * @param down
	 * @param left
	 */
	public int setNeighbors(ShipTile up, ShipTile right, ShipTile down, ShipTile left) {

		// Set tile in neighbors - Stitch newTile to neighbor tiles
		if(up != null){
			up.setDown(this); // Set up neighbor to this (down)
			up.setIsEdge(up.calculateIsEdge()); // Since new tile has been placed, check and see if tile is edge.
		}
		if(right != null){
			right.setLeft(this);
			right.setIsEdge(right.calculateIsEdge());
		}
		if(down != null){
			down.setUp(this);
			down.setIsEdge(down.calculateIsEdge());
		}
		if(left != null){
			left.setRight(this);
			left.setIsEdge(left.calculateIsEdge());
		}

		// set this's each direction with appropriate relative tile.
		this.neighbors.setUp(up);
		this.neighbors.setRight(right);
		this.neighbors.setDown(down);
		this.neighbors.setLeft(left);

		setIsEdge(calculateIsEdge());// Check if self is edge now.

		return neighbors.numberOfNeighbors();
	}


	public void setUp(ShipTile up){
		neighbors.setUp(up);
	}

	public void setRight(ShipTile right){
		neighbors.setRight(right);
	}

	public void setDown(ShipTile down){
		neighbors.setDown(down);
	}

	public void setLeft(ShipTile left){
		neighbors.setLeft(left);
	}

	/**
	 * Checks if there are 4 neighbors.
	 * If there are return false, else return true
	 * @return - Returns  false if all sides have tiles, else returns false.
	 */
	private boolean calculateIsEdge(){
		if(numberOfNeighbors() == 4){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns number of neighbors based on method in AdjacentTiles
	 *
	 * @return
	 */
	public int numberOfNeighbors(){
		return neighbors.numberOfNeighbors();
	}

	/**
	 * Returns a list of Vectors corresponding to the null sides.
	 *
	 * @return - array of vectors corresponding to null sides
	 */
	public Array<Vector2> emptySideVectors() {
		Array<Vector2> results = new Array<>();

		// Get each
		if(up() == null)results.add(new Vector2(getX() + (ShipTile.TILESIZE/2.0f) , getY() + ShipTile.TILESIZE));
		if(right() == null)results.add(new Vector2(getX() + ShipTile.TILESIZE , getY() + (ShipTile.TILESIZE/2.0f)));
		if(down() == null)results.add(new Vector2(getX() + (ShipTile.TILESIZE/2.0f) , getY() - 1));
		if(left() == null)results.add(new Vector2(getX() - 1 , getY() + (ShipTile.TILESIZE/2.0f)));

		return results;
	}

	public ShipTile up(){
		return neighbors.getUp();
	}

	public ShipTile right(){
		return neighbors.getRight();
	}

	public ShipTile down(){
		return neighbors.getDown();
	}

	public ShipTile left(){
		return neighbors.getLeft();
	}

}
