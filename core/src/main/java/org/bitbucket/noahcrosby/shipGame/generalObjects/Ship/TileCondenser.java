package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.LambdaRecipe;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileRecipes;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeFactory;

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
    public ShipTile determineNewTile(Array<ShipTile> tiles) {
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
     * Generated tiles need to be moved
     *
     * @param tiles
     * @return
     */
    private ShipTile developTileIDFromArray(Array<ShipTile> tiles) {
        ShipTile result;
        ID id;

        // Faster check for single tile
        if (tiles.size == 1) return TileTypeFactory.getShipTileTypeInstance(new Vector2(0, 0), singleTileHandle(tiles));

        id = checkStringRecipeMatches(tiles);

        result = checkCollectionRecipeMatches(tiles);
        if(result != null) return result;

        if(id != null) { // This is too specific, we want to be able to produce more specific stuff than this.
            result = TileTypeFactory.getShipTileTypeInstance(new Vector2(0, 0), id);
            return result;
        }else{
            return null;
        }
    }

    /**
     * Checks tiles for a set based recipe.
     *
     * @param tiles
     * @return - tile if a match for lambda recipe, else returns null
     */
    private ShipTile checkCollectionRecipeMatches(Array<ShipTile> tiles) {
        ShipTile result = null;
        for(int i = 0; i < unlockTracker.allLambdaRecipes.size ; i++){
            LambdaRecipe lambdaRecipe = unlockTracker.allLambdaRecipes.get(i);
             result = lambdaRecipe.run(tiles);
            if(result != null) return result;
        }
        return null;
    }

    /**
     * Checks the list of tiles against the available recipes.
     * Returns ID if a match is found, else returns null
     *
     * @param tiles - Tiles to check
     * @return - ID if a match, else returns null
     */
    private ID checkStringRecipeMatches(Array<ShipTile> tiles) {
        ID id;
        // Get String for comparison
        TileArrayToString arrayToString = new TileArrayToString(tiles);
        String arrayString = arrayToString.toCompareString();

        // Check against recipes for match
        id = attemptArrayStringMatch(arrayString);

        // If not matched check if the array's reverse matches.
        String reverseCompareString = arrayToString.reverseToCompareString();
        ID temp = attemptArrayStringMatch(reverseCompareString);
        if (id != null && temp != null) throw new RuntimeException("Double recipe match error\n");

        // Return result, will be null without match
        return id;
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
        return attemptArrayStringMatch(arrayString);
    }

    /**
     * Takes the array of unlocked recipes and returns an ID if a recipe matches an available tile recipe
     *
     * @param compareString - A string representing an array of tiles.
     * @return - ID representing a tile to be produced, null if no matches are found
     */
    private ID attemptArrayStringMatch(String compareString) {
        Array<TileRecipes> recipes = getAvailableStringRecipes(); // Array of recipes available to the player.
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

    private Array<TileRecipes> getAvailableStringRecipes() {
        return unlockTracker.unlockedStringRecipes;
    }
}
