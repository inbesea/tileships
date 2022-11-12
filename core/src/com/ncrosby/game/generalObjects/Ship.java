package com.ncrosby.game.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ncrosby.game.ID;
import com.ncrosby.game.tileShipGame;
import com.ncrosby.game.tiles.AdjacentTiles;
import com.ncrosby.game.tiles.ShipTile;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class Ship extends GameObject {

	/*
	 * Ship has many methods to manage tiles internally.
	 * It should be able to place, remove and keep track of ShipTiles.
	 */
	private LinkedList<ShipTile> existingTiles = new LinkedList<ShipTile>();
//	private Camera cam;
	private OrthographicCamera cam;
	private ShipTile mouseLocation;
	private ShipTile draggedTile;

	private int pointLocation[] = new int[2];

	/**
	 * ShipHandler keeps track of the tiles of the ship and has methods for
	 * managing removing and adding tiles.
	 */
	public Ship (Vector2 position, ID id, OrthographicCamera cam) {
		super(position, new Vector2(0,0), id);
		this.cam = cam;
//		this.camera = camera;

		// Give new ship default tiles.
		/* TODO : Create more flexible init tile placements. Possibly a setInitTiles(<ShipTiles> st)
		*   that creates tiles based on a list of tile instances */
		addTileByCoord(position.x, position.y, ID.CoreTile);
		addTileByCoord(position.x + ShipTile.TILESIZE, position.y, ID.ShipTile);
		addTileByCoord(position.x + ShipTile.TILESIZE, position.y + ShipTile.TILESIZE, ID.ShipTile);
		addTileByCoord(position.x, position.y + ShipTile.TILESIZE, ID.ShipTile);
	}

	/**
	 *  Loops the list of existing tiles and renders them
	 *  TODO : Scale tile locations by the ship position to allow ship movement.
	 */
	public void render(tileShipGame game) {

		for(int i = 0; i < existingTiles.size(); i++) {
			ShipTile tempTile = existingTiles.get(i);
			game.batch.draw(new Texture(Gdx.files.internal(tempTile.getTexture())),
					tempTile.getX(), tempTile.getY(),
					tempTile.getSize().x, tempTile.getSize().y);
		}
		if(draggedTile != null){
			game.batch.draw(new Texture(Gdx.files.internal(draggedTile.getTexture())),
					draggedTile.getX(),draggedTile.getyIndex(),draggedTile.getSize().x,draggedTile.getSize().y);
		}
	}

	/**
	 * This Method adds a tile to the ship with a reference to the tile's x and y
	 * positions (without camera adjustment), the color, ID, and camera object for updating
	 * the render location
	 *
	 * @param x - The x coordinate this tile will be added to on the canvas (can go negative)
	 * @param y - The y coordinate this tile will be added to on the canvas (can go negative)
	 * @param id - The ID of the GameObject
	 * @return shipTile - this will be null if the space added to is not occupied, else will return the tile blocking
	 */
	public ShipTile addTileByCoord(float x, float y, ID id) {

//		cam = legacyGame.getCam();
		// Use vector to set new tile
		Vector3 tileLocation = new Vector3();
		tileLocation.set(x,y,0);
		cam.unproject(tileLocation);

		// returnTile handles camera location
		ShipTile testTile = returnTile(x, y);
		float indexXY[] = returnIndex(x, y);

		if(testTile == null) { // x, y is vacant
			/*Dividing by tilesize is meant to get an index.
			 * by subtracting cam from the location we can get an index.
			 * However, it might be better to subtract cam first and then divide...
			 * This is a serious lack of encapsulation on display here. I need to be
			 * Able to add tiles by coord and index separately.. Or just
			 * encapsulate the indexes entirely.*/
			System.out.println("Create tile at " + x + "," + y);
			System.out.println("Create tile at " + returnIndex(x, y)[0] + ", " + returnIndex(x, y)[1]);

			ShipTile tempTile = new ShipTile(new Vector2 ((int) indexXY[0] * ShipTile.TILESIZE, (int) indexXY[1] * ShipTile.TILESIZE), id);
			setNeighbors(tempTile); // Setting tile neighbors within ship
			this.existingTiles.add(tempTile);
			return null;
		} else { // x, y is not vacant
			System.out.println("Already a tile at (x, y) = (" + x + ", " + y + ") -> index: (" + indexXY[0] + ", " + indexXY[1] + ")");
			return testTile;
		}
	}

	/**
	 * Sets tile neighbors equal to adjacent tiles, or to null if they don't exist
	 * @param tile - tile to initialize
	 * @return - number of neighbor tiles
	 */
	private int setNeighbors(ShipTile tile){
		AdjacentTiles neighbors = tile.getNeighbors();

		float x = tile.getX();
		float y = tile.getY();
		// Get references
		ShipTile up = returnTile(x,y + ShipTile.TILESIZE);
		ShipTile right = returnTile(x + ShipTile.TILESIZE, y);
		ShipTile down = returnTile(x,y - ShipTile.TILESIZE);
		ShipTile left = returnTile(x - ShipTile.TILESIZE, y);

		// Set tile in neighbors
		if(up != null)up.getNeighbors().setDown(tile);
		if(right != null)right.getNeighbors().setLeft(tile);
		if(down != null)down.getNeighbors().setUp(tile);
		if(left != null)left.getNeighbors().setRight(tile);

		// set each direction with getTile();
		neighbors.setUp(up);
		neighbors.setRight(right);
		neighbors.setDown(down);
		neighbors.setLeft(left);

		return neighbors.numberOfNeighbors();
	}


	/**
	 * removeTile gets a reference to a tile object and removes it from the linked list.
	 * Would be cool for a non-tile space to play a BLERT sound effect lol
	 */
	public void removeTileFromShip(float x, float y) {
		ShipTile tileToRemove = returnTile(x, y);

		if(tileToRemove == null) {
			System.out.println("No tile found at " + x + ", " + y);
		}
		else {
			logRemovedTile(tileToRemove);
			removeNeighbors(tileToRemove); // Remove references from adjacent tiles
			this.existingTiles.remove(tileToRemove);
		}
		//this.existingTiles.remove(tempTile);
	}

	/**
	 * Decouples the passed tile from the adjacent tiles
	 * @param shipTile
	 */
	private void removeNeighbors(ShipTile shipTile){
		AdjacentTiles neighbors = shipTile.getNeighbors();
		if(neighbors.numberOfNeighbors() == 0)return;

		float x = shipTile.getX();
		float y = shipTile.getY();

		// Get references
		ShipTile up = returnTile(x,y + ShipTile.TILESIZE);
		ShipTile right = returnTile(x + ShipTile.TILESIZE, y);
		ShipTile down = returnTile(x,y - ShipTile.TILESIZE);
		ShipTile left = returnTile(x - ShipTile.TILESIZE, y);

		// Remove reference to removed tile with null
		if(up != null)up.getNeighbors().setDown(null);
		if(right != null)right.getNeighbors().setLeft(null);
		if(down != null)down.getNeighbors().setUp(null);
		if(left != null)left.getNeighbors().setRight(null);
	}

	/**
	 * Removes a tile by reference to the tile instance
	 * Should only be used when a tile instance is found.
	 * @param tile
	 */
	public void removeTileFromShip(ShipTile tile){
		if(!this.existingTiles.remove(tile)){
			throw new RuntimeException("Error: Tile was not present in ship - \n" + Thread.currentThread().getStackTrace());
		} else {
			logRemovedTile(tile);
		}
	}

	/**
	 * Prints removed tile data to console
	 */
	private void logRemovedTile(ShipTile tile) {
		System.out.println("Removing tile (" + tile.getxIndex() + ", " +
				tile.getyIndex() + ") of type " + tile.getID().name() +  " from ship : " + this.getID());
	}

	/**
	 * Lazy bad way to get a tile with a different passed in value, but I don't feel like creating another method for both of these to call.
	 * @param position
	 * @return
	 */
	public ShipTile returnTile(Vector2 position){
		return returnTile(position.x, position.y);
	}

	/**
	 * Returns reference to a tile
	 * @param x - horizontal position of tile
	 * @param y - vertical position of tile
	 * @return - tile found, if no tile is found it returns null
	 */
	public ShipTile returnTile(float x, float y) {
		return findTile(new Vector2(x,y));
	}

	/**
	 * Returns reference to a tile based on x, y of Vector2
	 *
	 * @param position - click coordinates
	 * @return  - Can return a ShipTile object. Will return null on spaces without tiles.
	 */
	private ShipTile findTile(Vector2 position){
		float indexXY[] = returnIndex(position.x, position.y);

	 	ShipTile temp;
	 	Stack<ShipTile> resultTiles = new Stack<>();

		 // Checking ship tiles for index matches
	 	for(int i = 0; i < existingTiles.size(); i++) {
	 		//Assign current tile to temp
	 		temp = existingTiles.get(i);
	 		// Check if x matches
			if(temp.getxIndex() == indexXY[0]) {
				// If yes then check y as well
				if(temp.getyIndex() == indexXY[1]) {
					resultTiles.push(temp);
				}
			}
		}

		 // Handle result array
	 	if(resultTiles.size() > 1) {
	 		throw new ArithmeticException("Multiple Tiles Found in ReturnTile() for " + position.x + "," + position.y);
	 	}
	 	else if (resultTiles.size() <= 0) {
	 		return null;
	 	}
	 	else {
	 		return resultTiles.pop();
	 	}
	}

	/**
	 * returnIndex returns the index of a tilelocation from an x,y location.
	 * Can translates negative coords to negative indexes for tile init.
	 *
	 * @param x - The x location in the jframe to adjust by cam
	 * @param y - The y location in the jframe to adjust by cam
	 * @return
	 */
	public float[] returnIndex(float x, float y) {

		boolean yNegative = (y  + cam.direction.y <= -1);
		boolean xNegative = (x  + cam.direction.x <= -1);


		float XYresult[] = new float[2];
		if (xNegative) {
			if(yNegative) {
				// x, y negative
				// get index and subtract one.
				XYresult[0] = (int) (((x + cam.direction.x) / ShipTile.TILESIZE) - 1);
				XYresult[1] = (int) (((y + cam.direction.y) / ShipTile.TILESIZE) - 1);
			}
			else {
				// only x negative
				XYresult[0] = (int) (((x + cam.direction.x) / ShipTile.TILESIZE) - 1);
				XYresult[1] = (int) ((y + cam.direction.y) / ShipTile.TILESIZE);
			}
		}
		else if (yNegative) {
			// only Y negative
			XYresult[0] = (int) ((x + cam.direction.x) / ShipTile.TILESIZE);
			XYresult[1] = (int) (((y + cam.direction.y) / ShipTile.TILESIZE) - 1);
		}
		else {
			XYresult[0] = (int) ((x + cam.direction.x) / ShipTile.TILESIZE);
			XYresult[1] = (int) ((y + cam.direction.y) / ShipTile.TILESIZE);
		}

		if(XYresult.length == 2) {
			return XYresult;
		}
		else{
			throw new ArithmeticException("Unexpected number of indexes : " + XYresult.length );
		}
	}

	public void setPointLocation(int[] points) {
		pointLocation = points;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}


	/**
	 * Method to find the closest tile to a point
	 *
	 * @param location
	 * @return
	 */
	public ShipTile closestTile(Vector2 location) {
		if(existingTiles.size() == 0)return null;
		if(existingTiles.size() == 1)return existingTiles.getFirst();

		double minDistance = Double.POSITIVE_INFINITY; // First check will always be true
		ShipTile tempT;
		ShipTile result = null;
		Vector3 location3 = new Vector3(location.x, location.y, 0);
		Vector3 tileP;

		//Loop through ship to find closest tile
		for (int i = 0 ; i < existingTiles.size() ; i++){
			tempT = existingTiles.get(i);
			tileP = new Vector3(tempT.getPosition().x, tempT.getPosition().y , 0);
			Float distance = location3.dst(tileP);

			// Check if distance between position and current tile is shorter
			if(distance < minDistance){
				minDistance = distance;
				result = tempT;
			}
		}
		return result;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Takes in a tile, location, and closest tile and determines which side to place the tile.
	 *
	 * @param placedTile
	 * @param closestTile
	 * @param mousePosition
	 */
	public void setTileOnClosestSide(ShipTile placedTile, ShipTile closestTile, Vector3 mousePosition) {

		float closeX = closestTile.getX();
		float closeY = closestTile.getY();

		float x =
				(closeX) -
						mousePosition.x;
		float y =
				mousePosition.y -
				(closeY +
						(ShipTile.TILESIZE/2.0f));

		// Set vector such that the center of closestTile is equal to (0,0) and mouse position is a point relative to that
		Vector2 normalizedMousePosition = new Vector2(
				(mousePosition.x -
				(closestTile.getX() +
						(ShipTile.TILESIZE/2.0f))),
				(mousePosition.y -
						(closestTile.getY() +
								(ShipTile.TILESIZE/2.0f)))); // Find center of block by adding to the x,y

		// the point is above y = x if the y is larger than x
		boolean abovexEy = normalizedMousePosition.y > normalizedMousePosition.x;
		// the point is above y = -x if the y is larger than the negation of x
		boolean aboveNxEy = normalizedMousePosition.y > (-normalizedMousePosition.x);

		// We can conceptualize this as as a four triangles converging in the center of the "closest tile"
		// We can use this framing to decide the side to place the tile.
		if(abovexEy){ // Check at halfway point of tile
			if(aboveNxEy && closestTile.getNeighbors().getUp() == null){ // Top
					addTileByCoord(closestTile.getX(), closestTile.getY() + ShipTile.TILESIZE, placedTile.getID());
			} else if (closestTile.getNeighbors().getLeft() == null){ // Left
				addTileByCoord(closestTile.getX() - ShipTile.TILESIZE, closestTile.getY(), placedTile.getID());
			} else {
				throw new RuntimeException("Tried placing a tile adjacent to a tile without empty spaces :" +
						Arrays.toString(Thread.currentThread().getStackTrace()));
			}
		} else { // location is to the right of the closest tile
			if(aboveNxEy && closestTile.getNeighbors().getRight() == null){ // Right
				addTileByCoord(closestTile.getX() + ShipTile.TILESIZE, closestTile.getY(), placedTile.getID());
			} else if (closestTile.getNeighbors().getDown() == null) { // Bottom
				addTileByCoord(closestTile.getX(), closestTile.getY() - ShipTile.TILESIZE, placedTile.getID());
			} else {
				throw new RuntimeException("Tried placing a tile adjacent to a tile without empty spaces :" +
						Arrays.toString(Thread.currentThread().getStackTrace()));
			}
		}
	}

	/**
	 * Opposite of find ClosestTile,
	 * expected to be used within the ship's tiles to find a vector on the nearest vacancy
	 *
	 * @param vector2
	 * @return
	 */
	public Vector2 closestVacancy(Vector2 vector2) {
		return  null;
	}

	public void setDragged(ShipTile draggedTile) {
		this.draggedTile = draggedTile;
	}
}
