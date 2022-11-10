package com.ncrosby.game;

/**
 * Enums for easy identification of game objects
 * <p></p>
 * Player
 * BasicEnemy
 * ShipTile
 * CoreTile
 * Ship
 */
public enum ID {

	/**
	 * ID class to identify and classify different gameobjects.
	 * Contains reference to asset for sprite texture.
	 */
	Player("RobotV1.png"),
	BasicEnemy("asteroid_purple.png"),
	ShipTile("ShipTile_Red.png"),
	CoreTile("ShipTile_Core.png"),
	Ship("none");

	private String texture;

	ID(String texture){
		this.texture = texture;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}
}
