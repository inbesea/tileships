package com.shipGame.ncrosby;

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
	Player("RobotV2.png"),
	Asteroid("asteroid_purple.png"),
	CoreTile("ShipTile_Core.png"),
	Ship("none"),
	Hover("HoverIndicator.png"),
	StandardTile("ShipTile_Red.png"),
	StrongTile("ShipTile_Strong.png");

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
