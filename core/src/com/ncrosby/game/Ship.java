package com.ncrosby.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

public class Ship extends GameObject {


	// TODO: fix this class to use libjdx camera and draw methods.
	/*
	* Refactor notes
	* We need to understand how this is used in the firstplace
	*  */
	/*
	 * This class is meant to be used on a JFrame with a canvas in it.
	 * It should be able to place, remove and keep track of ShipTiles.
	 */
	private LinkedList<ShipTile> existingTiles = new LinkedList<ShipTile>();
//	private Camera cam;
	private OrthographicCamera cam;
	private ShipTile mouseLocation;

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
			ShipTile tempObject = existingTiles.get(i);
			game.batch.draw(new Texture(Gdx.files.internal(tempObject.getTexture())),
					tempObject.getX(), tempObject.getY(),
					tempObject.getSize().x, tempObject.getSize().y);
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

		// Place tile at location relative to the camera.
		if(testTile == null) {
			/*Dividing by tilesize is meant to get an index.
			 * by subtracting cam from the location we can get an index.
			 * However, it might be better to subtract cam first and then divide...
			 * This is a serious lack of encapsulation on display here. I need to be
			 * Able to add tiles by coord and index separately.. Or just
			 * encapsulate the indexes entirely.*/
			System.out.println("Create tile at " + x + "," + y);
			System.out.println("Create tile at " + returnIndex(x, y)[0] + ", " + returnIndex(x, y)[1]);

			ShipTile tempTile = new ShipTile(new Vector2 ((int) indexXY[0] * ShipTile.TILESIZE, (int) indexXY[1] * ShipTile.TILESIZE), id);
			this.existingTiles.add(tempTile);
			return tempTile; // Will be null
		}
		else {
			System.out.println("Already a tile at :" + x + ", " + y);
			System.out.println("Already a tile at :" + indexXY[0] + ", " + indexXY[1]);
			return testTile;
		}
	}


	/**
	 * removeTile gets a reference to a tile object and removes it from the linked list.
	 * Would be cool for a non-tile space to play a BLERT sound effect lol
	 */
	public void removeTile(float x, float y) {
		ShipTile tileToRemove = returnTile(x, y);

		if(tileToRemove == null) {
			System.out.println("No tile found at " + x + ", " + y);
		}
		else {
			logRemovedTile(tileToRemove);
			this.existingTiles.remove(tileToRemove);
		}
		//this.existingTiles.remove(tempTile);
	}


	/**
	 * Removes a tile by reference to the tile instance
	 * Should only be used when a tile instance is found.
	 * @param tile
	 */
	public void removeTile(ShipTile tile){
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
	 * Returns reference to a tile based on unadjusted x,y of jFrame.
	 *
	 * @param x - click coordinate (without cam adjustment)
	 * @param y - y click coord. (without cam adjustment)
	 * @return  - Can return a ShipTile object. Will return null on spaces without tiles.
	 *
	 * This has a BigO of 2n, up from n^2
	 * search the resulting size n list (n^2 so far)
	 */
	public ShipTile returnTile(float x, float y) {
		// The index is gotten by using the x and adding camera to it.
		// Then by using the ship tile size to get an index.
	 	//int indexX = (x + cam.x) / ShipTile.TILESIZE;
	 	//int indexY = (y + cam.y) / ShipTile.TILESIZE;
		float indexXY[] = returnIndex(x, y);

	 	Stack<ShipTile> xTileList = new Stack<>();
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

	 	if(resultTiles.size() > 1) {
	 		throw new ArithmeticException("Multiple Tiles Found in ReturnTile() for " + x + "," + y);
	 	}
	 	else if (resultTiles.size() <= 0) {
	 		//System.out.println("No Tiles found at " + x + "," + y);
	 		return null;
	 	}
	 	else if(resultTiles.size() == 1){
	 		return resultTiles.pop();
	 	}
	 	else {
	 		throw new ArithmeticException("Error : Problem with result set. " + resultTiles);
	 	}

	 	// Search the linked list for x matches, return null if empty list (log this happening).
	 	// and then loop through that list for y matches, return null if this whittles down to nothing.
	 	// Maybe need a tooltip to popup reminding that empty space cannot be grabbed lol .
	}
	// Issue is the pixel location isn't stored anywhere yeah? :/ How can I check the tile at a location without searching all over creation for it...

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

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
