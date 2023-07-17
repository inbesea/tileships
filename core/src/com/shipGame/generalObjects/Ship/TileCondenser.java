package com.shipGame.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ID;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.generalObjects.Ship.tiles.tileUtility.TileRecipes;

/**
 * Class to determine IDs based on ordered arrays of tiles.
 * Matches are made against recipe objects.
 */
public class TileCondenser {

    // Smallest amount that can be turned into a new tile.
    static int SMALLEST_INPUT = 1;
    private final UnlockTracker unlockTracker;

    public TileCondenser(UnlockTracker unlockTracker) {
        this.unlockTracker = unlockTracker;
    }

    /**
     * Returns a tile based on an array of tiles.
     *
     * @param tiles - array of tiles to condense
     * @return - an ID representing a tile type.
     */
    public ID determineNewTileID(Array<ShipTile> tiles) {
        // This initiates a lot of sub-methods to match the passed tile array.

        if (tiles.isEmpty() || tiles.size < SMALLEST_INPUT) { // No tiles, or below minimum array size
            Gdx.app.debug("BuildingTile", "Passed array size too small for TileCondenser");
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

        if (tiles.size == 1) return singleTileHandle(tiles);

        // Get String for comparison
        TileArrayToString arrayToString = new TileArrayToString(tiles);
        String arrayString = arrayToString.toCompareString();

        // Check against recipes for match
        result = attemptArrayMatch(arrayString);

        // If not matched check if the array's reverse matches.
        String reverseCompareString = arrayToString.reverseToCompareString();
        ID temp = attemptArrayMatch(reverseCompareString);
        if (result != null && temp != null) throw new RuntimeException("Double recipe match error\n");

        return result;
    }

    /**
     * Handles situation where a single tile is passed to condenser
     *
     * @param tiles - single-tile array
     * @return - ID for single tile recipe
     */
    private ID singleTileHandle(Array<ShipTile> tiles) {
        TileArrayToString arrayToString = new TileArrayToString(tiles);
        String arrayString = arrayToString.toCompareString();
        return attemptArrayMatch(arrayString);
    }

    /**
     * Takes the array of unlocked recipes and returns an ID if a recipe matches an available tile recipe
     *
     * @param compareString - A string representing an array of tiles.
     * @return - ID representing a tile to be produced, null if no matches are found
     */
    private ID attemptArrayMatch(String compareString) {
        Array<TileRecipes> recipes = getAvailableRecipes(); // Array of recipes available to the player.
        ID temp;
        ID result = null;

        for (int i = 0; i < recipes.size; i++) {
            temp = recipes.get(i).tileIfMatch(compareString);
            if (result != null && temp != null) {
                throw new RuntimeException("Multiple matches found for tile matching input : " + compareString + " \n");
            } else {
                result = temp;
            }
        }
        return result;
    }

    private Array<TileRecipes> getAvailableRecipes() {
        return unlockTracker.unlockedRecipes;
    }
}