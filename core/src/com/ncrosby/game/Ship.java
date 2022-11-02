package com.ncrosby.game;

import com.ncrosby.game.main.legacyGame;

import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

public class Ship extends GameObject {

	/*
	 * This class is meant to be used on a JFrame with a canvas in it.
	 * It should be able to place, remove and keep track of ShipTiles.
	 */
	private ShipTile[][] shipTiles;
	private LinkedList<ShipTile> existingTiles = new LinkedList<ShipTile>();
	private Camera cam;
	private ShipTile mouseLocation;

	private int pointLocation[] = new int[2];

	/**
	 * ShipHandler keeps track of the tiles of the ship and has methods for
	 * managing removing and adding tiles.
	 */
	public Ship (int x, int y, ID id, Camera cam) {
		super(x, y, id);
		this.cam = cam;
		addTileByCoord(x, y, ID.CoreTile, cam);
		addTileByCoord(x + ShipTile.TILESIZE, y, ID.ShipTile, cam);
		addTileByCoord(x + ShipTile.TILESIZE, y + ShipTile.TILESIZE, ID.ShipTile, cam);
		addTileByCoord(x, y + ShipTile.TILESIZE, ID.ShipTile, cam);
	}

	/**
	 *  Loops the list of existing tiles and renders them
	 * @param g - Graphics renderer
	 */
	public void render(Graphics g) {
		for(int i = 0; i < existingTiles.size(); i++) {
			ShipTile tempObject = existingTiles.get(i);
			tempObject.render(g);
		}
		g.setColor(Color.white);


		// I have no idea why this must be scaled by camera at this point. :(
		int xIndexByTileSizeByCamX = (pointLocation[0] * ShipTile.TILESIZE) - cam.x;
		int yIndexByTileSizeByCamY = (pointLocation[1] * ShipTile.TILESIZE) - cam.y;
		g.drawRect(xIndexByTileSizeByCamX, yIndexByTileSizeByCamY, ShipTile.TILESIZE, ShipTile.TILESIZE);
	}

	/**
	 * This Method adds a tile to the ship with a reference to the tile's x and y
	 * positions (without camera adjustment), the color, ID, and camera object for updating
	 * the render location
	 *
	 * @param x - The x coordinate this tile will be added to on the canvas (can go negative)
	 * @param y - The y coordinate this tile will be added to on the canvas (can go negative)
	 * @param id - The ID of the GameObject
	 * @param cam - the camera object needed to adjust render location relative to the camera.
	 */
	public void addTileByCoord(int x, int y, ID id, Camera cam) {
		// scale the location by the tile size so it can be rendered
		// give it an ID and the passed in color.

		cam = legacyGame.getCam();
		// returnTile handles camera location
		ShipTile testTile = returnTile(x, y);
		int indexXY[] = returnIndex(x, y);

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

			ShipTile tempTile = new ShipTile(indexXY[0], indexXY[1], id, cam);
			this.existingTiles.add(tempTile);
		}
		else {
			System.out.println("Already a tile at :" + x + ", " + y);
			System.out.println("Already a tile at :" + indexXY[0] + ", " + indexXY[1]);
		}
	}


	/**
	 * removeTile gets a reference to a tile object and removes it from the linked list.
	 * Would be cool for a non-tile space to play a BLERT sound effect lol
	 */
	public void removeTile(int x, int y) {
		ShipTile tileToRemove = returnTile(x, y);

		if(tileToRemove == null) {
			System.out.println("No tile found at " + x + ", " + y);
		}
		else {
			this.existingTiles.remove(tileToRemove);
		}

		//this.existingTiles.remove(tempTile);
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
	public ShipTile returnTile(int x, int y) {
		// The index is gotten by using the x and adding camera to it.
		// Then by using the ship tile size to get an index.
	 	//int indexX = (x + cam.x) / ShipTile.TILESIZE;
	 	//int indexY = (y + cam.y) / ShipTile.TILESIZE;
		int indexXY[] = returnIndex(x, y);

	 	Stack<ShipTile> xTileList = new Stack<>();
	 	ShipTile temp;
	 	Stack<ShipTile> resultTiles = new Stack<>();

	 	for(int i = 0; i < existingTiles.size(); i++) {


	 		//Assign current tile to temp
	 		temp = existingTiles.get(i);
	 		// Check if x matches
			if(temp.getX() == indexXY[0]) {
				// If yes then check y as well
				if(temp.getY() == indexXY[1]) {
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
	public int[] returnIndex(int x, int y) {

		boolean yNegative = (y  + cam.y <= -1);
		boolean xNegative = (x  + cam.x <= -1);


		int XYresult[] = new int[2];
		if (xNegative) {
			if(yNegative) {
				// x, y negative
				// get index and subtract one.
				XYresult[0] = ((x + cam.x) / ShipTile.TILESIZE) - 1;
				XYresult[1] = ((y + cam.y) / ShipTile.TILESIZE) - 1;
			}
			else {
				// only x negative
				XYresult[0] = ((x + cam.x) / ShipTile.TILESIZE) - 1;
				XYresult[1] = ((y + cam.y) / ShipTile.TILESIZE);
			}
		}
		else if (yNegative) {
			// only Y negative
			XYresult[0] = ((x + cam.x) / ShipTile.TILESIZE);
			XYresult[1] = ((y + cam.y) / ShipTile.TILESIZE) - 1;
		}
		else {
			XYresult[0] = (x + cam.x) / ShipTile.TILESIZE;
			XYresult[1] = (y + cam.y) / ShipTile.TILESIZE;
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
