package org.bitbucket.noahcrosby.shipGame;

import org.bitbucket.noahcrosby.shipGame.screens.MainMenuScreen;

/**
 * Enums for easy identification of game objects
 * <p></p>
 * Player
 * Asteroid
 * ShipTile
 * CoreTile
 * Ship
 */
public enum ID {

	/**
	 * ID class to identify and classify different gameobjects.
	 * Contains reference to asset for sprite texture.
	 * Sprite selection happens here.
	 */
	Player( false),
	Asteroid( false),
	CoreTile( true),
	Ship( false),
	Hover( false),
	StandardTile( true),
	StrongTile( true),
    ColorTile( true),
    EngineTile( true),
    FuelTile( true),
    ForegroundObject(false);

	private String texture;

	//Easy way to check if an object is a tile or not.
	private boolean isTileType;

	ID(boolean isTileType){
		this.texture = texture;
		this.isTileType = isTileType;
	}

	public boolean isTileType(){
		return isTileType;
	}


	public void setTexture(String texture) {
		this.texture = texture;
	}
}
