package org.bitbucket.noahcrosby.shipGame;

/**
 * Enums for easy identification of game objects
 * <p></p>
 * Player
 * Asteroid
 * ShipTile
 * CoreTile
 * ship
 */
public enum ID {

	/**
	 * ID class to identify and classify different game-objects.
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
    FurnaceTile( true),
    FuelTile( true),
    AncientTile(true),
    ForegroundObject(false),
    CommunicationTile(true);

    //Easy way to check if an object is a tile or not.
	private final boolean isTileType;

    ID(boolean isTileType){
        this.isTileType = isTileType;
	}

	public boolean isTileType(){
		return isTileType;
	}
}
