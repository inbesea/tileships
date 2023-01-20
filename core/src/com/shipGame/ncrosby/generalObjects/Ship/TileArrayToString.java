package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileOrienter;

/**
 * Takes an array of ShipTiles and can return a string representation of said array.
 *
 * String example can be : "std0std1std1std2"
 * This represents four standard tiles at (0,0),(0,1),(1,1),(1,0) going up right down
 */
public class TileArrayToString {

    private Array<ShipTile> tiles;

    public TileArrayToString(Array<ShipTile> tiles){
        this.tiles = tiles;
    }



    /**
     * Generates a string for tile development comparison.
     * @return
     */
    public String toCompareString() {
        // Create a string representing the array passed to this object, where orientation is up.
        TileOrienter orienter = new TileOrienter(tiles);
        int orientation = orienter.getCurrentOrientation();
        StringBuilder tileArrayString = new StringBuilder();
        ShipTile tempTile;
        ShipTile tempTile1;
        int nextTileDirection;

        // Default position
        if(orientation == 0) return tilesToString();

        // Loop array and build out string
        for(int i = 0 ; i < tiles.size ; i++){
            tempTile = tiles.get(i);
            tempTile1 = tiles.get(i + 1);

            tileArrayString.append(simpleToCompareString(orientation, tempTile, tempTile1));

            if(i == tiles.size - 2){ // When on last iteration (ending before running out of tiles) getAbbreviation() for last tile.
                tileArrayString.append(tempTile1.getAbbreviation());
                return tileArrayString.toString();
            }
        }
        return null;
    }

    /**
     *  Method to return a compare string representation of the current tile array starting at the end of the array and working backwards.
     * @return - The array backwards as a string.
     */
    public String reverseToCompareString() {
        // Create a string representing the array passed to this object, where orientation is up.
        int orientation = TileOrienter.calculateOrientation(tiles.get(tiles.size - 1), tiles.get(tiles.size - 2));
        if(orientation == -1){
            Gdx.app.debug("reverseToCompareString", "Error getting orientation");
            return "ERROR";
        }

        // Var init
        StringBuilder tileArrayString = new StringBuilder();
        ShipTile tempTile;
        ShipTile tempTile1;

        // Loop array backwards and build out string
        for(int i = tiles.size - 1 ; i > 0 ; i--){ // Start with last, subtrack one each time, stop before index 0
            tempTile = tiles.get(i);
            tempTile1 = tiles.get(i - 1);

            tileArrayString.append(simpleToCompareString(orientation, tempTile, tempTile1));

            if(i == 1){ // When on last iteration (ending before running out of tiles) getAbbreviation() for last tile.
                tileArrayString.append(tempTile1.getAbbreviation());
                return tileArrayString.toString();
            }
        }
        return null;
    }

    /**
     * Returns string snippet with respect to the passed reference direction to an updirection.
     * @param originalOrientation - Direction to move from
     * @param tile0 - First tile
     * @param tile1 - Second tile
     * @return - Short snippet with "ABB0" as the basic format
     */
    public String simpleToCompareString(int originalOrientation, ShipTile tile0, ShipTile tile1){
        StringBuilder stringBuilder = new StringBuilder();
        TileOrienter orienter = new TileOrienter(originalOrientation);

        stringBuilder.append(tile0.getAbbreviation());
        stringBuilder.append(orienter.directionRemap(tile0.getAdjacency(tile1), AdjacentTiles.UP));

        return stringBuilder.toString();
    }

    /**
     * Takes arbitrary array of tiles and returns string representation
     * @return
     */
    public String tilesToString() {
        StringBuilder tileArrayString = new StringBuilder();
        ShipTile tile;
        ShipTile tempTile1;
        int nextTileDirection;

        // Loop array and build out string
        for(int i = 0 ; i < tiles.size ; i++){
            tile = tiles.get(i);
            tempTile1 = tiles.get(i+1);
            tileArrayString.append(simpleTileToString(tile, tempTile1));

            if(i == tiles.size - 2){ // When on last iteration (ending before running out of tiles) getAbbreviation() for last tile.
                tileArrayString.append(tempTile1.getAbbreviation());
                return tileArrayString.toString();
            }
        }
        return null;
    }

    /**
     * Takes two tiles and builds a string with format (abbreviation)(numberOfDirection) without any reorientation
     * @param tile0
     * @param tile1
     * @return
     */
    public String simpleTileToString(ShipTile tile0, ShipTile tile1){
        StringBuilder tileArrayString = new StringBuilder();
        int nextTileDirection;

        tileArrayString.append(tile0.getAbbreviation());
        nextTileDirection = tile0.getAdjacency(tile1);
        tileArrayString.append(nextTileDirection);

        return tileArrayString.toString();
    }
}
