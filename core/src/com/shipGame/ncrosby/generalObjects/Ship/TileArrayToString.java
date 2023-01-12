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
     * Generates a string for ShipTile development comparison.
     * @return
     */
    public String toCompareString() {
        // Create a string representing the array passed to this object, where orientation is up.
        TileOrienter orienter = new TileOrienter(tiles);
        int orientation = orienter.setAndReturnCurrentOrientation(0);
        // We have the orientation now. I want to scribe each tile individually when looping.
        // The orienter may just move the array over and ruin the actual tile in the ship. Can we do that differently.
        // tilea tileB tileC have indexes and the first two point right. Lets make a string out of them.
        if(orientation != AdjacentTiles.INVALID){
            tileToString(orienter.getTileArray());
        }
        return null;
    }

    /**
     * Takes arbitrary array of tiles and returns string representation.
     * @param tiles
     * @return
     */
    private String tileToString(Array<ShipTile> tiles) {


        return null;
    }
}
