package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileRecipes;

/**
 * Class to build out new instances of tiles based on ordered arrays of tiles.
 * Used to match up patterns using member classes.
 */
public class TileCondenser {

    // Smallest amount that can be turned into a new tile.
    static int SMALLEST_INPUT = 1;
    private UnlockTracker unlockTracker;

    public TileCondenser(UnlockTracker unlockTracker){
        this.unlockTracker = unlockTracker;
    }

    /**
     * Returns a tile based on an array of tiles.
     *
     * @param tiles - array of tiles to condense
     * @return - an ID representing a tile type.
     */
    public ID determineNewTileID(Array<ShipTile> tiles){
        // This initiates a lot of sub-methods to match the passed tile array.

        if(tiles.isEmpty() || tiles.size < SMALLEST_INPUT){ // No tiles, or below minimum array size
            Gdx.app.debug("BuildingTile","Passed array size too small for TileCondenser");
            return null;
        } else { // Process array
            return developTileIDFromArray(tiles);
        }
    }

    /**
     * Runs logic to develop a tile if possible.
     * Runs a lot of background processes to parse the array
     *
     * @param tiles
     * @return
     */
    private ID developTileIDFromArray(Array<ShipTile> tiles) {
        ID result;

        if(tiles.size == 1)return singleTileHandle(tiles);

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

    /**
     * Handles situation where a single tile is passed to condenser
     * @param tiles - single-tile array
     * @return - ID for single tile recipe
     */
    private ID singleTileHandle(Array<ShipTile> tiles) {
        TileArrayToString arrayToString = new TileArrayToString(tiles);
        String arrayString = arrayToString.toCompareString();
        ID result = attemptArrayMatch(arrayString);
        return result;
    }

    /**
     * Takes the array of unlocked recipes and returns an ID if a recipe matches an available tile recipe
     * @param compareString - A string representing an array of tiles.
     * @return - ID representing a tile to be produced
     */
    private ID attemptArrayMatch(String compareString) {
        Array<TileRecipes> recipes = getAvailableRecipes(); // Array of recipes available to the player.
        ID id;

        for(int i = 0 ; i < recipes.size ; i++){
            id = recipes.get(i).tileIfMatch(compareString);
            if(id != null)return id;
        }
        return null;
    }

    private Array<TileRecipes> getAvailableRecipes() {
        return unlockTracker.unlockedRecipes;
    }

}
