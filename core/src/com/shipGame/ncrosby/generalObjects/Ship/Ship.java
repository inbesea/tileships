package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Asteroid;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.player.TileHoverIndicator;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.util.AsteroidManager;

import static com.shipGame.ncrosby.util.generalUtil.*;

import java.util.Stack;

public class Ship extends GameObject {

	/**
	 * Ship has many methods to manage tiles internally.
	 * It should be able to place, remove and keep track of ShipTiles.
	 *
	 * The in and out of tiles into this ship should be concentrated into one class, so that changing how the ship works
	 * is easy and in one location
	 *
	 * Edge management happens on I/O of tiles so we have a reference to the tile being updated.
	 *
	 * Ships manage shipTiles. Tiles don't know about their relationship with other tiles, the ship manages that.
	 */
	private Array<ShipTile> existingTiles = new Array<>();
	// Subset of existing tiles
	private Array<ShipTile> edgeTiles = new Array<>();
	private ShipTile draggedTile;
	private GameScreen screen;
	AsteroidManager asteroidManager;
	Array<GameObject> gameObjects;
	private int pointLocation[] = new int[2];
	public int destroyedTileCount = 0;
	private TileStackManager tileStackManager;
	private TileCondenser tileCondenser;

	/**
	 * ShipHandler keeps track of the tiles of the ship and has methods for
	 * managing removing and adding tiles.
	 */
	public Ship (Vector2 position, ID id, OrthographicCamera cam, Array<GameObject> gameObjects, AsteroidManager asteroidManager) {
		super(position, new Vector2(0,0), id);
		this.gameObjects = gameObjects;
		this.asteroidManager = asteroidManager;

		tileStackManager = new TileStackManager();
		tileCondenser = new TileCondenser();

		// Give new ship default tiles.
		/* TODO : Create more flexible init tile placements. Possibly a setInitTiles(<ShipTiles> st)
		*   that creates tiles based on a list of tile instances */
		initShipTiles();
	}

	public Ship (Vector2 position, ID id, OrthographicCamera cam, GameScreen screen){
		super(position, new Vector2(0,0), id);
		this.screen = screen;

		tileStackManager = new TileStackManager();
		tileCondenser = new TileCondenser();

		// Give new ship default tiles.
		/* TODO : Create more flexible init tile placements. Possibly a setInitTiles(<ShipTiles> st)
		 *   that creates tiles based on a list of tile instances */
		initShipTiles();
	}

	/**
	 *  Loops the list of existing tiles and renders them
	 *  TODO : Scale tile locations by the ship position to allow ship movement.
	 */
	public void render(tileShipGame game) {
		AssetManager assetManager = game.assetManager;

		for(int i = 0; i < existingTiles.size; i++) {
			ShipTile tempTile = existingTiles.get(i);
			game.batch.draw(assetManager.get(tempTile.getTexture(),Texture.class),
					tempTile.getX(), tempTile.getY(),
					tempTile.getSize().x, tempTile.getSize().y);
		}
		if(draggedTile != null){
			game.batch.draw(assetManager.get(draggedTile.getTexture(), Texture.class),
					draggedTile.getX(),draggedTile.getY(),draggedTile.getSize().x,draggedTile.getSize().y);
		}
		if(tileStackManager.isCollectingTiles()){
			Array<ShipTile> tiles = tileStackManager.getTileArray();
			for(int i = 0 ; tiles.size > i ; i++){
				ShipTile tile = tiles.get(i);
				game.batch.draw(assetManager.get("ToBeCollapsed.png", Texture.class),
						tile.getX(), tile.getY());
			}
		}
	}

	/**
	 * Sets the initial tiles.
	 */
	private void initShipTiles() {
		addTile(position.x, position.y, ID.CoreTile);
		addTile(position.x + ShipTile.TILESIZE, position.y, ID.ShipTile);
		addTile(position.x + ShipTile.TILESIZE, position.y + ShipTile.TILESIZE, ID.ShipTile);
		addTile(position.x, position.y + ShipTile.TILESIZE, ID.ShipTile);
		addTile(position.x  + ShipTile.TILESIZE * 2, position.y + ShipTile.TILESIZE, ID.ShipTile);
	}

	/**
	 * This Method adds a tile to the ship with a reference to the tile's x and y
	 * positions , the color, ID, and camera object for updating
	 * the render location
	 *
	 * @param x - The x coordinate this tile will be added to on the canvas (can go negative)
	 * @param y - The y coordinate this tile will be added to on the canvas (can go negative)
	 * @param id - The ID of the GameObject
	 * @return shipTile - this will be null if the space added to is not occupied, else will return the tile blocking
	 */
	public ShipTile addTile(float x, float y, ID id) {

		// Use vector to set new tile
		Vector3 tileLocation = new Vector3();
		tileLocation.set(x,y,0);
		Vector2 tileLocation2 = new Vector2(tileLocation.x, tileLocation.y);
		ShipTile closestTile;
		ShipTile tempTile;

		ShipTile destinationTile = returnTile(tileLocation2);
		if(destinationTile == null){ // Released on empty space
			if(existingTiles.size == 0) return placeTile(x, y , id);

			closestTile = closestTile(tileLocation2, getExistingTiles());
			Vector2 closestExternalVacancy = getVectorOfClosestSide(closestTile, tileLocation);
			tempTile = placeTile(closestExternalVacancy.x, closestExternalVacancy.y, id);
		} else { // Released on Shiptile
			Vector2 nearestEmptySpace = closestInternalVacancy(tileLocation2); // Get closest Vacancy on edge tiles
			tempTile = placeTile(nearestEmptySpace.x, nearestEmptySpace.y, id);
		}

		if(existingTiles.size < edgeTiles.size){
			throw new RuntimeException("More edgeTiles than existing!" +
					"\nexistingTiles.size : " + existingTiles.size +
					"\nedgeTiles.size : " + edgeTiles.size);
		}
		return tempTile;

	}
		/*
		This logic takes the tile, returns the index, and then scales out the x,y by the tile size alligning the
		tiles along the grid. THIS is the core logic keeping everything on the grid.
		 */

	/**
	 * Adds a tile expecting the x,y to be a valid placement location
	 */
	private ShipTile placeTile(float x, float y, ID id){
		int indexXY[];
		ShipTile tempTile;
		Sound tilePlacement;
		tilePlacement = Gdx.audio.newSound( Gdx.files.internal("Sound Effects/tilePlacementV2.wav"));

		indexXY = returnIndex(x, y); // Get index corresponding to that
		System.out.println("Create tile at " + x + "," + y);
		System.out.println("Create tile at " + returnIndex(x, y)[0] + ", " + returnIndex(x, y)[1]);
		System.out.println("Type of : " + id);
		tempTile = new ShipTile(new Vector2 (getGameSpacePositionFromIndex(indexXY[0]), getGameSpacePositionFromIndex(indexXY[1])), id);
		this.existingTiles.add(tempTile);
		setNeighbors(tempTile); // Setting tile neighbors within ship

		if(existingTiles.size < edgeTiles.size){
			throw new RuntimeException("More edgeTiles than existing! " +
					" existingTiles.size : " + existingTiles.size +
					"edgeTiles.size : " + edgeTiles.size);
		}
		tilePlacement.play();
		return tempTile;
	}

	/**
	 * Returns a Vector position with respect to the grid.
	 *
	 * TODO : Should update ship so the grid is alligned with the shipposition, allowing us to move the ship and keep grid.
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 getGridAlignedPosition(float x, float y){
		int[] indexes = returnIndex(x, y);
		Vector2 vector2 = new Vector2(getGameSpacePositionFromIndex(indexes[0]), getGameSpacePositionFromIndex(indexes[1]));
		return vector2;
	}

	/**
	 * Returns an index scaled up by ShipTile.TILESIZE
	 * @return
	 */
	public int getGameSpacePositionFromIndex(int index){
		return index * ShipTile.TILESIZE;
	}

	/**
	 * removeTile gets a reference to a tile object and removes it from the ship while decoupling neighbors
	 */
	public void removeTileFromShip(float x, float y) {
		ShipTile tileToRemove = returnTile(x, y);

		if(tileToRemove == null) {
			System.out.println("No tile found at " + x + ", " + y);
		}
		else {
			logRemovedTile(tileToRemove);
			removeNeighbors(tileToRemove); // Remove references from adjacent tiles
			if(!this.existingTiles.removeValue(tileToRemove, true)){
				System.out.println("tileToRemove was not in the ship!");
			}
		}
		//this.existingTiles.remove(tempTile);
	}

	/**
	 * Removes a tile by reference to the tile instance
	 * Should only be used when a tile instance is found. Handles flushing the adjacency relationships between tiles.
	 *
	 * @param tile - Tile to remove from ship
	 */
	public void removeTileFromShip(ShipTile tile){
		if(!this.existingTiles.removeValue(tile, true)){
			throw new RuntimeException("Error: Tile was not present in ship - \n" + Thread.currentThread().getStackTrace());
		} else {
			if(isCollectingTiles()){ // Delete tile if being collected.
				getCollapseCollect().removeValue(tile, true);
			}
			removeNeighbors(tile);
			logRemovedTile(tile);
		}
	}

	/**
	 * Uses removeTileFromShip to remove all instances of an array of tiles in the ship
	 *
	 * @param tiles - Tiles to remove from ship
	 */
	public void removeTilesFromShip(Array<ShipTile> tiles){
		for (int i = 0 ; i <= tiles.size ; i++){
			removeTileFromShip(tiles.get(i));
		}
	}

	/**
	 * Sets neighbors and checks if edge values change.
	 * <p></p>
	 * Gets contextual tiles via the ship's context, and calls delegate method within tile, passing the possible neighbor context.
	 * <p></p>
	 * Sets tile neighbors equal to adjacent tiles, or to null if they don't exist
	 * @param tile - tile to initialize
	 * @return - number of neighbor tiles
	 */
	private int setNeighbors(ShipTile tile){

		// Init vars
		float x = tile.getX();
		float y = tile.getY();

		// Get adjacent tile references
		ShipTile up = returnTile(x,y + ShipTile.TILESIZE*1.5f);
		ShipTile right = returnTile(x + ShipTile.TILESIZE*1.5f, y);
		ShipTile down = returnTile(x,y - ShipTile.TILESIZE/2.0f);
		ShipTile left = returnTile(x - ShipTile.TILESIZE/2.0f, y);

		// tie the tiles together
		int numberOfNeighbors = tile.setNeighbors(up, right, down, left);

		checkIfAdjustEdgeArray(up);
		checkIfAdjustEdgeArray(right);
		checkIfAdjustEdgeArray(down);
		checkIfAdjustEdgeArray(left);
		checkIfAdjustEdgeArray(tile);

		System.out.println("Added a tile, number of edge tiles : " + edgeTiles.size);

		if(existingTiles.size < edgeTiles.size){
			throw new RuntimeException("More edgeTiles than existing!" +
					"\nexistingTiles.size : " + existingTiles.size +
					"\nedgeTiles.size : " + edgeTiles.size);
		}

		return numberOfNeighbors;
	}

	/**
	 * Checks if tile should be removed or added to edge array.
	 *
	 * Assumes that the neighbors have been updated and reflect the current shipstate
	 */
	private void checkIfAdjustEdgeArray(ShipTile tile) {
		if(tile != null && existingTiles.size>1 && tile.numberOfNeighbors()==0){
			throw new RuntimeException("Tile was placed that has no neighbors despite other tiles existing");
		}else if(tile != null){
			boolean tileExistsAndShouldBeEdge = tile.isEdge && !edgeTiles.contains(tile, true) && existingTiles.contains(tile, true); // Gotta confirm the tile exists lol

			if(tileExistsAndShouldBeEdge){
				edgeTiles.add(tile);
				if(existingTiles.size < edgeTiles.size){
					throw new RuntimeException("More edgeTiles than existing!" +
							"\nexistingTiles.size : " + existingTiles.size +
							"\nedgeTiles.size : " + edgeTiles.size);
				}
			} else if (!tile.isEdge && edgeTiles.contains(tile, true) && existingTiles.contains(tile, true)){
				edgeTiles.removeValue(tile, true);
			}  else  {
				// The
			}
		}
	}

	/**
	 * Decouples the passed tile from the adjacent tiles
	 *
	 * Responsible for removing the tile from the edge tiles if present.
	 * @param shipTile - Tile to decouple
	 */
	private void removeNeighbors(ShipTile shipTile){
		AdjacentTiles neighbors = shipTile.getNeighbors();

		// Remember to remove the tile from the existing tiles
		if(shipTile.isEdge()){ // If was an edge then try to remove
			if(!edgeTiles.removeValue(shipTile, true)){
				throw new RuntimeException("Removed tile was not found in edge");
			}
		}

		// Return if tile has no neighbors
		if(neighbors.numberOfNeighbors() == 0)return;

		float x = shipTile.getX();
		float y = shipTile.getY();


		// Get references
		ShipTile up = returnTile(x,y + ShipTile.TILESIZE);
		ShipTile right = returnTile(x + ShipTile.TILESIZE, y);
		ShipTile down = returnTile(x,y - ShipTile.TILESIZE);
		ShipTile left = returnTile(x - ShipTile.TILESIZE, y);

		// Remove reference to removed tile with null and set each to edge since an adjacent tile is removed
		if(up != null){
			up.setDown(null);
			if(!up.isEdge){ // if not already in the edge party add it
				up.setIsEdge(true);
				if(edgeTiles.contains(up,true)){
					throw new RuntimeException("Non-edge tile present in edge tile array");
				} else {
					edgeTiles.add(up);
				}
			}
		}
		if(right != null){
			right.setLeft(null);
			if(!right.isEdge){ // if not already in the edge party add it
				right.setIsEdge(true);
				if(edgeTiles.contains(right,true)){
					throw new RuntimeException("Non-edge tile present in edge tile array");
				} else {
					edgeTiles.add(right);
				}
			}
		}
		if(down != null){
			down.setUp(null);
			if(!down.isEdge){ // if not already in the edge party add it
				down.setIsEdge(true);
				if(edgeTiles.contains(down,true)){
					throw new RuntimeException("Non-edge tile present in edge tile array");
				} else {
					edgeTiles.add(down);
				}
			}
		}
		if(left != null){
			left.setRight(null);
			if(!left.isEdge){ // if not already in the edge party add it
				left.setIsEdge(true);
				if(edgeTiles.contains(left,true)){
					throw new RuntimeException("Non-edge tile present in edge tile array");
				} else {
					edgeTiles.add(left);
				}
			}
		}

		if(existingTiles.size < edgeTiles.size){
			throw new RuntimeException("More edgeTiles than existing! " +
					" existingTiles.size : " + existingTiles.size +
					"edgeTiles.size : " + edgeTiles.size);
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
	 * @return - tile found, else returns null
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
		int indexXY[] = returnIndex(position.x, position.y);

	 	ShipTile temp;
	 	Stack<ShipTile> resultTiles = new Stack<>();

		 // Checking ship tiles for index matches
	 	for(int i = 0; i < existingTiles.size; i++) {
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
	 * Returns number of tiles, taking dragged tile into account.
	 * @return
	 */
	public int numberOfShipTiles(){
		if(draggedTile != null){
			return existingTiles.size + 1;
		} else {
			return existingTiles.size;
		}
	}

	public int numberOfEdgeTiles(){
		return edgeTiles.size;
	}

	/**
	 * Returns true if there are more tiles than edge tiles
	 *
	 * @return - Boolean representing if ship has non-edge tiles
	 */
	public boolean hasNonEdgeTiles(){
		return existingTiles.size > edgeTiles.size;
	}

	/**
	 * returnIndex returns the index of a tilelocation from an x,y location.
	 * Can translates negative coords to negative indexes for tile init.
	 *
	 * @param x - The x location in the jframe to adjust by cam
	 * @param y - The y location in the jframe to adjust by cam
	 * @return - int so the index is a whole number
	 */
	public int[] returnIndex(float x, float y) {

		// -1 shifting wont cause issues because the flow will subtract one from it either way
		// -64 - -1 will return index -1 yayy

		boolean yNegative = y <= -1;
		boolean xNegative = x <= -1;


		int XYresult[] = new int[2];
		if (xNegative) {
			if(yNegative) {
				// x, y negative
				// get index and subtract one.
				XYresult[0] = (int) (( (x + 1) / ShipTile.TILESIZE) - 1);
				XYresult[1] = (int) (( (y + 1) / ShipTile.TILESIZE) - 1);
			}
			else {
				// only x negative
				XYresult[0] = (int) (( (x + 1) / ShipTile.TILESIZE) - 1);
				XYresult[1] = (int) ( (y) / ShipTile.TILESIZE);
			}
		}
		else if (yNegative) {
			// only Y negative
			XYresult[0] = (int) ((x) / ShipTile.TILESIZE);
			XYresult[1] = (int) (( (y+ 1) / ShipTile.TILESIZE) - 1);
		}
		else {
			XYresult[0] = (int) ((x) / ShipTile.TILESIZE);
			XYresult[1] = (int) ((y) / ShipTile.TILESIZE);
		}

		if(XYresult.length == 2) {
			return XYresult;
		}
		else{
			throw new ArithmeticException("Unexpected number of indexes : " + XYresult.length );
		}
	}

	/**
	 * Helper method to make Ship aware of mouse location
	 * @param points
	 */
	public void setPointLocation(int[] points) {
		pointLocation = points;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}


	/**
	 * Method to find the closest tile to a point given an Array of ShipTiles
	 *
	 * @param location - Vector to check prox. to
	 * @param tiles - Array of Tiles to search in
	 * @return - closest tile
	 */
	public ShipTile closestTile(Vector2 location , Array<ShipTile> tiles) {
		if(tiles.size == 0)return null;
		if(tiles.size == 1)return tiles.get(0);

		double minDistance = Double.POSITIVE_INFINITY; // First check will always be true
		ShipTile tempT;
		ShipTile closestTile = null;
		Vector3 location3 = new Vector3(location.x, location.y, 0);
		Vector3 tileP;

		//Loop through ship to find closest tile
		for (int i = 0 ; i < tiles.size ; i++){
			tempT = tiles.get(i);
			tileP = new Vector3(tempT.getPosition().x + ShipTile.TILESIZE/2.0f, tempT.getPosition().y  + ShipTile.TILESIZE/2.0f , 0);
			Float distance = location3.dst(tileP);

			// Check if distance between position and current tile is shorter
			if(distance < minDistance){
				minDistance = distance;
				closestTile = tempT;
			}
		}
		return closestTile;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Handles collisions between ship and game objects
	 *
	 * @param gameObject
	 */
	@Override
	public void collision(GameObject gameObject) {

		if(gameObject.getID() == ID.Asteroid){ // Object collision was asteroid
			ShipTile shipTile;
			boolean removeAsteroid = false;

			for(int i = 0 ; i < existingTiles.size ; i++){
				shipTile = existingTiles.get(i);

				// Check if asteroid intersects ShipTile
				Rectangle rectangle = shipTile.getBounds();
				Circle asteroidCircle = gameObject.getCircleBounds();
				boolean isCollision = circleIntersectsRectangle(asteroidCircle,rectangle, screen);

				if(isCollision){
					System.out.println("Collision! with " + shipTile.getID());
					if(shipTile.getID() == ID.CoreTile){

						// Get middle of Asteroid
						float x = gameObject.getX() + gameObject.getCircleBounds().radius;
						float y = gameObject.getY() + gameObject.getCircleBounds().radius;

						removeAsteroid = true;

						// New basic tile
						addTile(x, y,
								ID.ShipTile);
						break;
					} else if (shipTile.getID() == ID.ShipTile) {
						// Explode tile and asteroid
						removeTileFromShip(shipTile);
						increaseDestroyedTile(1);

						removeAsteroid = true;
					}
				}
			}
			//Handles Asteroid destroy.
			// If this is done in the forloop it can't find the reference
			if(removeAsteroid){
				screen.removeGameObject(gameObject);
				screen.removeAsteroid(gameObject);
				screen.updateMouseMoved();
			}

		}

		// Might could be used? :/ Probably not tho
	}

	/**
	 * Finds the game object, removes and returns it from the game screen Array
	 * @param gameObject
	 * @return
	 */
	public GameObject removeGameObject(GameObject gameObject){
		int i = gameObjects.indexOf(gameObject, true); // Get index of gameObject

		if(i < 0){
			System.out.println("OH jeez");
		}
		if(i < 0){
			throw new RuntimeException("gameObject not found in GameScreen existing game objects - number of objects... : " + gameObjects.size +
					" location of gameObject " + gameObject.getX() + ", " + gameObject.getY() + " GameObject ID : " +gameObject.getID());
		}
		return gameObjects.removeIndex(i); // Returns the object reference and removes it.
	}

	public void removeAsteroid(GameObject asteroid) {
		asteroidManager.removeAsteroid((Asteroid) asteroid);
	}

	@Override
	public Circle getCircleBounds() {
		return null;
	}

	/**
	 * Takes in a tile, location, and closest tile and determines which side to place the tile.
	 *
	 * @param closestTile
	 * @param mousePosition
	 */
	public Vector2 getVectorOfClosestSide(ShipTile closestTile, Vector3 mousePosition) {

		int closestSide = getClosestSide(closestTile, new Vector2(mousePosition.x, mousePosition.y));

		// We can conceptualize this as as a four triangles converging in the center of the "closest tile"
		// We can use this framing to decide the side to place the tile.
		if (closestSide == 0) { // North
			return  new Vector2(closestTile.getX()+(ShipTile.TILESIZE/2.0f), closestTile.getY() + ShipTile.TILESIZE*1.5f);
		} else if (closestSide == 3) { // West
			return  new Vector2(closestTile.getX() - (ShipTile.TILESIZE/2.0f), closestTile.getY()+(ShipTile.TILESIZE/2.0f));
		} else if (closestSide == 1) { // East
			return  new Vector2(closestTile.getX() + (ShipTile.TILESIZE * 1.5f), closestTile.getY() + (ShipTile.TILESIZE/2.0f));
		} else if (closestSide == 2) { // South
			return  new Vector2(closestTile.getX()+(ShipTile.TILESIZE/2.0f), closestTile.getY() - (ShipTile.TILESIZE/2.0f));
		} else {
			throw new RuntimeException("Something went wrong while running setTileOnClosestSide()");
		}
	}

	/**
	 * Gets closest side of a tile from a Vector2 point
	 *
	 * @param tileWithSides - tile to find the side of
	 * @param position - position to compare with tile
	 * @return - int representing side of tile as compass - N = 0, E = 1, S = 2, W = 3
	 */
	private int getClosestSide(ShipTile tileWithSides, Vector2 position){
		float closeX = tileWithSides.getX();
		float closeY = tileWithSides.getY();

		// Should return the difference between the placed position and middle of the close tile.
		float normalX =
				position.x -
						(closeX + (ShipTile.TILESIZE/2.0f));
		float normalY =
				position.y -
						(closeY + (ShipTile.TILESIZE/2.0f));

		// Set vector such that the center of tile is equal to (0,0) and mouse position is a point relative to that
		Vector2 normalizedMousePosition = new Vector2(normalX,normalY); // Find center of block by adding to the x,y

		// the point is above y = x if the y is larger than x
		boolean abovexEy = normalizedMousePosition.y > normalizedMousePosition.x;
		// the point is above y = -x if the y is larger than the negation of x
		boolean aboveNxEy = normalizedMousePosition.y > (-normalizedMousePosition.x);

		// We can conceptualize this as as a four triangles converging in the center of the "closest tile"
		// We can use this framing to decide the side to place the tile.
		if(abovexEy){ // Check at halfway point of tile
			if(aboveNxEy){ // North = 0
				return  0;
			} else { // West = 3
				return 3;
			}
		} else { // location is to the right of the closest tile
			if(aboveNxEy){ // East = 1
				return 1;
			} else { // South = 2
				return 2;
			}
		}
	}

	/**
	 * Opposite of find ClosestTile,
	 * expected to be used within the ship's tiles to find a vector on the nearest vacancy
	 * Returns a Vector2 based on a vacancy closest to a point within the ship
	 *
	 * @param centerOfSearch
	 * @return
	 */
	public Vector2 closestInternalVacancy(Vector2 centerOfSearch) {
		ShipTile underVector = returnTile(centerOfSearch);

		if(underVector != null){
			// Get all Shiptiles that are on edge
			ShipTile nearestEdge = closestTile(centerOfSearch, edgeTiles);

			Array<Vector2> nullSidesLocs = nearestEdge.emptySideVectors();
			int nullSidesCount = nullSidesLocs.size;
			// We have the closest tile (yay!) We need to act differently depending on the number of null sides.
			// or do we? Can we just add a vector to a list and the check which is closest?
			if(nullSidesCount == 0) {
					throw new RuntimeException("Something went wrong when getting nearest edge vectors, None Found");
			} else if (nullSidesCount == 1) {
					System.out.println("Only one side : " + nullSidesLocs.peek().x + ", " + nullSidesLocs.peek().y);
					return nullSidesLocs.pop();
			} else if (5 > nullSidesCount) {
				Vector2 vector2 = closestVector2(centerOfSearch, nullSidesLocs);
				return vector2;
			} else {
				throw new RuntimeException("Too many null sides!");
			}
		} else {// Handle trivial case - vector is on vacancy
			return centerOfSearch;
		}
	}

	public void setDragged(ShipTile draggedTile) {
		this.draggedTile = draggedTile;
	}

	/**
	 * Checks both bottom corners of a gameObject using size and position,
	 * will also check intermediary points if size > TILESIZE
	 *
	 * If a single point is off the ship this returns true.
	 *
	 * @return - true if any points checked are off-ship, else returns false
	 */
	public boolean isGameObjectOffShip(GameObject gameObject){
		float objectSize = gameObject.getWidth();
		Vector2 leftCorner = gameObject.getPosition();
		Vector2 rightCorner = new Vector2(leftCorner.x + objectSize, leftCorner.y); // Same as left with adjusted x

		return isHorizontalSpanOffShip(leftCorner, rightCorner);
	}

	/**
	 * Checks a span defined by two Vector2 points within a ship.
	 * will also check intermediary points if size > TILESIZE
	 * If a single point is off the ship this returns true.
	 * @param leftPoint
	 * @param rightPoint
	 * @return - true if any points checked are off-ship, else returns false
	 */
	public boolean isHorizontalSpanOffShip(Vector2 leftPoint, Vector2 rightPoint){
		float objectSize = leftPoint.x - rightPoint.y; // get point span TODO : rename to spanSize

		// Check left and right corner
		if(isPositionOffShip(leftPoint) || isPositionOffShip(rightPoint)){
			return true;
		}

		// Check if additional points need to be checked.
		if(objectSize > ShipTile.TILESIZE) { // objectSize > TILESIZE ( Check for size is bigger than tilesize )
			// Want to find number of internal points.
			// size / Shiptile.TILESIZE
			// round up and use to find x
			int objectInternalPoints = (int) Math.ceil(objectSize / ShipTile.TILESIZE);
			for(int i = 1 ; i > objectInternalPoints - 1 ; i++){ // dont need to check i=objectInternalPoints since this = rightCorner
				if(isPositionOffShip(new Vector2(leftPoint.x + (i * ShipTile.TILESIZE), rightPoint.y))){
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if a position does not match a tile on the ship.
	 * Any checks should ping this method instead of handling the logic of returned value checking.
	 * Ships should be responsible for checking their own tiles.
	 * @param position - Vector2 position to check
	 * @return - Boolean true if no tile found - else return false
	 */
	public boolean isPositionOffShip(Vector2 position) {
		ShipTile tile = returnTile(position.x,position.y);
		if(tile == null){
			return true;
		} else {
			return false;
		}
	}

	public Array<ShipTile> getExistingTiles() {
		return existingTiles;
	}

	/**
	 * Method to count destroyed tiles
	 */
	public void increaseDestroyedTile(int destroyedTiles){
		destroyedTileCount += destroyedTiles;
	}

	public boolean isCollectingTiles(){
		return tileStackManager.isCollectingTiles();
	}

	/**
	 * Kick off collecting tiles in the stack manager
	 */
    public void startCollapseCollect() {tileStackManager.startCollect();}

	/**
	 * Clears and returns a stack of tiles collected during a collapse action
	 */
	public Array<ShipTile> finishCollapseCollect() {
		return tileStackManager.endCollect();
	}

	/**
	 * Standard getter
	 * @return
	 */
	public Array<ShipTile> getCollapseCollect(){
		return tileStackManager.getTileArray();
	}

	/**
	 * Adds an element to the CollapseCollection and returns a bool representing success.
	 * @param tile - Tile to add to manager stack
	 * @return boolean signifying success
	 */
	public boolean addTileToCollapseCollection(ShipTile tile){
		if(tileStackManager.isCollectingTiles()){
			tileStackManager.addTile(tile);
			return true;
		} else {
			throw new RuntimeException("CollectTiles is false : " + tileStackManager.isCollectingTiles());
		}
	}

	/**
	 * Adds an element to the CollapseCollection using a positional reference
	 * @param vector3 -  a position in space.
	 * @return - boolean signifying success
	 */
	public boolean addTileToCollapseCollection(Vector3 vector3){
		if(tileStackManager.isCollectingTiles()){
			ShipTile tile = returnTile(vector3.x, vector3.y);
			if(tile != null){
				tileStackManager.addTile(tile);
				return true;
			}
			return false;
		} else {
			throw new RuntimeException("CollectTiles is false : " + tileStackManager.isCollectingTiles());
		}
	}

	/**
	 * Returns reference to the hovering indicator reference
	 * @return - Hover indication instance
	 */
	public TileHoverIndicator getTileHoverIndicator() {
		return tileStackManager.getTileHoverIndicator();
	}

	/**
	 * Set hover indicator position
	 * @param x - x-position
	 * @param y - y-position
	 */
	public void setHoverIndicator(float x, float y){
		tileStackManager.setHoverIndicator(x,y);
	}

	/**
	 * Checks if the hovers should draw
	 * @return - true if is drawing, false if not drawing
	 */
	public boolean isHoverDrawing() {
		return tileStackManager.isHoverDrawing();
	}

	/**
	 * Sets if the hover position should draw.
	 * @param shouldDraw - new boolean value for drawing the hover layover
	 */
	public void setHoverShouldDraw(boolean shouldDraw){
		tileStackManager.setDrawHover(shouldDraw);
	}

	/**
	 * Check if manager has collected the limit of tiles.
	 * @return false if more tiles can be collected, else returns false
	 */
	public boolean collapseStackIsFull(){
		return tileStackManager.isFull();
	}


	public void cancelCurrentCollectArray(){
		tileStackManager.cancelCurrentCollectArray();
	}

	public boolean isTileCollected(ShipTile tile) {
		return tileStackManager.isTileCollected(tile);
	}

	/**
	 * Returns a tile
	 * Handles removing tiles from ship instance
	 * @param collectedTileArray - list of tiles to produce from
	 * @return - ShipTile resulting from build action.
	 */
	public ShipTile buildNewTile(Array<ShipTile> collectedTileArray) {
		ShipTile producedTile = tileCondenser.buildNewTile(collectedTileArray);

		if(producedTile == null){
			tileStackManager.cancelCurrentCollectArray(); // Reset the stack due to failed production
			return null;
		} else { // if Tile produced then
			removeTilesFromShip(collectedTileArray);
			return producedTile;
		}
	}
}
