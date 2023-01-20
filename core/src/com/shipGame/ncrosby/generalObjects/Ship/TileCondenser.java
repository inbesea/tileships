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
 * Used to match up patterns using member classes.
 */
public class TileCondenser {

    // Smallest amount that can be turned into a new tile.
    static int SMALLEST_INPUT = 1;

    public TileCondenser(){
    }

    /**
     * Returns a tile based on an array of tiles.
     *
     * @param tiles - array of tiles to condense
     * @return - a single product tile.
     */
    public ShipTile buildNewTile(Array<ShipTile> tiles){
        // This initiates a lot of sub-methods to match the passed tile array.

        if(tiles.isEmpty() || tiles.size < SMALLEST_INPUT){ // No tiles, or below minimum array size
            Gdx.app.debug("BuildingTile","Passed array size too small for TileCondenser");
            return null;
        } else { // Process array
            return developTileFromArray(tiles);
        }
    }

    /**
     * Runs logic to develop a tile if possible.
     * Checks the tiles both directions through
     * @param tiles
     * @return
     */
    private ShipTile developTileFromArray(Array<ShipTile> tiles) {
        ShipTile result;

        // Get String for comparison
        TileArrayToString arrayToString = new TileArrayToString(tiles);
        String arrayString = arrayToString.toCompareString();

        // Check against recipes for match
        result = attemptArrayMatch(arrayString);
        if(result != null)return result;

        // If not matched check if the array's reverse matches.
        String reverseCompareString = arrayToString.reverseToCompareString();
        result = attemptArrayMatch(reverseCompareString);
        if(result != null)return result;

        // If all else fails
        return null;
    }

    private ShipTile attemptArrayMatch(String compareString) {
        Array<TileRecipes> recipes = getAvailableRecipes(); // Array of recipes available to the player.
//        for(int i = 0 ; i < upTurnedTiles.size ; i++){
//            // Check turned tiles against the array. If a match is found return that
//            // Else try to reverse the array and run the check again.
//        }
        return null;
    }

    private Array<TileRecipes> getAvailableRecipes() {
return null;
    }

}
