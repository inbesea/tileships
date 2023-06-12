package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.AdjacentTiles;
import com.shipGame.ncrosby.tileShipGame;

public abstract class ShipTile extends GameObject{

	private final AdjacentTiles neighbors = new AdjacentTiles();
	private final int xIndex, yIndex;

	private long placed = System.currentTimeMillis();
	private int cool = 0;
	public boolean isEdge;
	public final static float TILESIZE = 1f;
	private com.badlogic.gdx.math.Rectangle collider;
	private TileTypeData typeData; // Need for unique platonic form data
	/**
	 *  These tiles will all need health, and a way to relate to tiles next to them..?
	 *  But they will need to be stored in a 2d array. 
	 *  So when the game initializes there will need to be an array of tiles built out.
	*/
	public ShipTile(Vector2 position, ID id, TileTypeData typeData) {
		// vector is not adjusted, so tiles can be independently created anywhere
		super(position, new Vector2(TILESIZE,TILESIZE), id);

		this.xIndex = determineIndex(position.x);
		this.yIndex = determineIndex(position.y);

		this.typeData = typeData;

		collider = new com.badlogic.gdx.math.Rectangle(position.x, position.y ,ShipTile.TILESIZE, ShipTile.TILESIZE);
		// Need to knit together the shiptile to adjacent tiles connectAdjacent();
	}

	/**
	 * TODO : This index determination needs to be updated to allow for ship displacement/rotation.
	 * Determines an index value for the ShipTile based on the float passed in.
	 * @param position
	 * @return
	 */
	private int determineIndex(float position) {
		return (int) (position / TILESIZE);
	}

	public void tick() {
		// TODO Auto-generated method stub
	}

	/**
	 * Returns the indices of the ShipTile as a string
	 * @return - String of ShipTile location
	 */
	public String getPositionAsString(){
		return "(" + xIndex + ", " + yIndex + ")";
	}

	/**
	 * Gets specific shiptile Abbreviation from unique tile type asserted during construction
	 */
	public String getAbbreviation(){
		return typeData.getAbbreviation();
	};

	/**
	 * Renders information specific to the ShipTiles
	 *
	 * @param game
	 */
	public void render(tileShipGame game) {
		if(this.debugMode){
			game.font.draw(game.batch, getxIndex() + ", " + getyIndex(), getX() + 2 , getY() + (size.y/4));
		}
	}

	/**
	 * Method to cool a placed shipTile over time.
	 *
	 */
	private void coolPlacedBlock(tileShipGame game){
		long deltaTime = System.currentTimeMillis() - placed;
		if(deltaTime  >= 150) {
			//System.out.println( "r : " + (255 - cool) + "g : " + (0 + cool) + "b : " + (0 + cool));

			if ( cool <= 200 ) {
				cool = cool + 5;
				placed = System.currentTimeMillis();
			}
		}
		// If still doing this we need to animate a cooling block possibly? Hmmm idk if LibGDX has good solution here
//		g.setColor(new Color(255 - cool, cool, cool));
//		g.drawRect(xLoc - cam.x, yLoc - cam.y, TILESIZE, TILESIZE);
	}

	/**
	 * Returns the collision box for the shipTile
	 * @return - Rectangle object representing the collision box
	 */
	@Override
	public Rectangle getBounds() {
		return collider;
	}

	/**
	 * Method to handle collisions
	 * @param gameObject
	 */
	@Override
	public void collision(GameObject gameObject) {

	}

	@Override
	public Circle getCircleBounds() {
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

	public boolean isEdge() {
		return isEdge;
	}

	public void setIsEdge(boolean isEdge){
		this.isEdge = isEdge;
	}

	/**
	 * Method that handles setting the neighbors of a newly placed tile.
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

	/**
	 * Delegate method to get the directly up neighbor
	 * @return
	 */
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

	/**
	 * Checks if possibleNeighbor is a neighbor.
	 * Returns true if is neighbor, else returns false
	 * @param possibleNeighbor - tile to check for adjacency
	 * @return -  Returns true if is neighbor, else returns false
	 */
	public boolean isNeighbor(ShipTile possibleNeighbor) {
		return getNeighbors().isNeighbor(possibleNeighbor);
	}

	/**
	 * Returns an int representing the relationship between the passed tile and this tile.
	 * @param tile
	 * @return An int showing where tile is in relationship to this tile.
	 * 	 * 0 = UP
	 * 	 * 1 = RIGHT
	 * 	 * 2 = DOWN
	 * 	 * 3 = LEFT
	 */
	public int getAdjacency(ShipTile tile) {
		return neighbors.isWhichNeighbor(tile);
	}

	public abstract boolean isInvulnerable();
}
