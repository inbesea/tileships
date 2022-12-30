package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileOrienter;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileRecipes;

import static com.shipGame.ncrosby.util.generalUtil.reverseArray;

/**
 * Class to build out new instances of tiles based on ordered arrays of tiles.
 */
public class TileCondenser {

    // Smallest amount that can be turned into a new tile.
    static int SMALLEST_INPUT = 2;
    Array<ID> TileIDs;

    public TileCondenser(){
        TileIDs = new Array<>();
    }

    /**
     * Returns a tile based on an array of tiles.
     * @param tiles - array of tiles to condense
     * @return - a single product tile.
     */
    public ShipTile buildNewTile(Array<ShipTile> tiles){
        if(tiles.isEmpty() || tiles.size < SMALLEST_INPUT){
            Gdx.app.error("BuildingTile","Empty array passed to TileCondenser");
            return null;
        } else {
            ShipTile result;

            // Orient tiles for comparison
            Array<ShipTile> upTurnedTiles = orientArrayUpwards(tiles);

            // Check against recipes
            result = attemptArrayMatch(upTurnedTiles);
            if(result != null)return result;

            // If not returned check if the array needs to be reversed.
            Array<ShipTile> reverseAndOrientArray = reverseAndOrientArray(tiles);
            result = attemptArrayMatch(reverseAndOrientArray);
            if(result != null)return result;
        }
        return null;
    }

    /**
     * Function to reverse and orient an array upwards.
     *
     * @param tiles
     * @return
     */
    private Array<ShipTile> reverseAndOrientArray(Array<ShipTile> tiles) {
        Array<ShipTile> result = new Array<>();
        result = reverseArray(tiles);
        result = orientArrayUpwards(result);
        return result;
    }

    private ShipTile attemptArrayMatch(Array<ShipTile> upTurnedTiles) {
        Array<TileRecipes> recipes = getAvailableRecipes(); // Array of recipes available to the player.
        for(int i = 0 ; i < upTurnedTiles.size ; i++){
            // Check turned tiles against the array. If a match is found return that
            // Else try to reverse the array and run the check again.
        }
        return null;
    }

    /**
     * Takes array and rotates it to point upwards.
     *
     * This is done by looking at the first two and checking the direction.
     * @return
     */
    private Array<ShipTile> orientArrayUpwards(Array<ShipTile> tiles) {
        // Check first two
        ShipTile tile0 = tiles.get(0);
        ShipTile tile1 = tiles.get(1);
        int orientation = getOrientation(tile0, tile1);



        return null;
    }

    /**
     * Returns the orientation between tile0 and tile1
     *
     * @param tile0 - first tile
     * @param tile1 - second tile
     * @return Returns 0-3 representing orientation between the two tiles. -1 if invalid inputs.
     */
    private int getOrientation(ShipTile tile0, ShipTile tile1) {
        TileOrienter orienter = new TileOrienter(tile0, tile1);
        return orienter.getOrientation();
    }

    private Array<TileRecipes> getAvailableRecipes() {
return null;
    }

}
