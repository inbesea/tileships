package com.shipGame;

import com.shipGame.screens.MainMenuScreen;

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
	Player("RobotV2.png", false),
	Asteroid("asteroid_purple.png", false),
	CoreTile("ShipTile_Core.png", true),
	Ship("none", false),
	Hover("HoverIndicator.png", false),
	StandardTile("ShipTile_Red.png", true),
	StrongTile("ShipTile_Strong.png", true);

	private String texture;

	//Easy way to check if an object is a tile or not.
	private boolean isTileType;

	ID(String texture, boolean isTileType){
		this.texture = texture;
		this.isTileType = isTileType;
	}

	public String getTexture() {
		return texture;
	}

	public boolean isTileType(){
		return isTileType;
	}

	public String getSprite() {
		return MainMenuScreen.spritePath + texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}
}
