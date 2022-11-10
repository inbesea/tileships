package com.ncrosby.game;

import com.ncrosby.game.generalObjects.Player;
import com.ncrosby.game.generalObjects.Ship;
import com.ncrosby.game.tiles.ShipTile;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickLocationListener extends MouseAdapter {
	
	// Gives the lister access to the ship's tiles/methods. 
	private Camera cam;
	private Player pl;

	/**
	 * When constructed CLL should have a reference to the player's ship. 
	 * This isn't great if we want there to be multiple other ships however, so a 
	 * master list of ships may need to exist, or a way to add ships to a list here may be needed... 
	 * Tiles Could all be in a single list and we could search for them based on the location. 
	 * The tiles may need a way to hold the 
	 * 
	 * @param cam
	 */
	public ClickLocationListener(Player pl, Camera cam) {
		this.pl = pl;
		this.cam = cam;
	}

	/**
	 * Handles click events. 
	 * This can only refer to the ship the player currently has referenced in their vars. 
	 * This can stay if the player can switch ship references in their vars. 
	 * We need a list of ships even more now, and a way to click on a space and get back a 
	 * reference to a tile that knows what ship it's a part of. 
	 */
	public void mouseReleased(MouseEvent e) {
		//System.out.println(e.getX() + "," + e.getY() + " \n Camera adjusted : " + (e.getX() + cam.x) + ", " + (e.getY() + cam.y));
		int x = e.getX();
		int y = e.getY();
		
		Ship sh = pl.getPlayerShip();
		ShipTile shipTile = sh.returnTile(x, y);
		
		if (e.getButton() == 1) {
			// godmode checks
			if(pl.godMode) {
				if(shipTile == null) {
					sh.addTileByCoord(x, y, ID.ShipTile);
				}
				else if(shipTile instanceof ShipTile) {
					sh.removeTile(x, y);
				}
				else {
					throw new ArithmeticException("Unexpected value for shipTile : " + shipTile);
				}
			}
			
			// Standard checks
			else {
				// no tile
				if(shipTile == null) {
					if(pl.heldShipTiles != null) {
						ShipTile st = pl.popTile();
						sh.addTileByCoord(x, y, st.getID());
					}
					// Is a tile
				} else {
					// If player is not holding a tile > grab tile 
					if(pl.heldShipTiles == null) {
						pl.pickupTile(shipTile);
						sh.removeTile(x, y);
					}
					// If player is holding tile log event
					else {
						System.out.println("Already holding a tile");
					}
				}
				
			}
		}
		if(e.getButton() == 3) {
			pl.getPlayerShip().removeTile(x, y);
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		Ship sh = pl.getPlayerShip();
		// Need to get the 
		
	}
	
	
	public void MouseMotionListener(MouseEvent e) {
		System.out.println(e.getPoint());
	}
}
