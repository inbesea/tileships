package com.shipGame.ncrosby.generalObjects.Ship;

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
        int nextTileDirection;

        // Default position
        if(orientation == 0) return tilesToString();

        // Loop array and build out string
        for(int i = 0 ; i < tiles.size ; i++){
            tempTile = tiles.get(i);
            tileArrayString.append(tempTile.getAbbreviation());
            nextTileDirection = tempTile.getAdjacency(tiles.get(i+1));
            tileArrayString.append(orienter.directionRemap(nextTileDirection, AdjacentTiles.UP));
            // Want a way to get a number 0-3 (-1 as no invalid) to
            // Check for the direction from the last tile.
            // We can use the neighbor methods in ShipTile to determine this.

            if(i == tiles.size - 2){ // When on last iteration (ending before running out of tiles) getAbbreviation() for last tile.
                tileArrayString.append(tiles.get(i+1).getAbbreviation());
                return tileArrayString.toString();
            }
        }
        return null;

        // Can we avoid changing the tiles original instances?? :/

        // We have the orientation now. I want to scribe each tile individually when looping.
        // The orienter may just move the array over and ruin the actual tile in the ship. Can we do that differently.
        // tilea tileB tileC have indexes and the first two point right. Lets make a string out of them.
    }

    /**
     * Takes arbitrary array of tiles and returns string representation
     * @return
     */
    public String tilesToString() {
        StringBuilder tileArrayString = new StringBuilder();
        ShipTile tile;
        int nextTileDirection;

        // Loop array and build out string
        for(int i = 0 ; i < tiles.size ; i++){
            tile = tiles.get(i);
            tileArrayString.append(tile.getAbbreviation());
            nextTileDirection = tile.getAdjacency(tiles.get(i+1));
            tileArrayString.append(nextTileDirection);
            // Want a way to get a number 0-3 (-1 as no invalid) to
            // Check for the direction from the last tile.
            // We can use the neighbor methods in ShipTile to determine this.

            if(i == tiles.size - 2){ // When on last iteration (ending before running out of tiles) getAbbreviation() for last tile.
                tileArrayString.append(tiles.get(i+1).getAbbreviation());
                return tileArrayString.toString();
            }
        }
        return null;
    }
}
