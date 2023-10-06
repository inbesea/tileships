package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileTypes;

/**
 * Interface to specify methods used with all tiles.
 * This allows the ShipTile concrete class to be called directly which
 */
public interface ShipTileInterface {

    /**
     * Class to get an abbreviation string from a shipTile object.
     * Expected to be gotten for building compare strings
     * @return - A string reflecting an abbreviation of a type of tile.
     */
    public abstract String getAbbreviation();

}
