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

        // Orient tiles for comparison
        Array<ShipTile> upTurnedTiles = orientArrayUpwards(tiles);

        // Check against recipes for match
        result = attemptArrayMatch(upTurnedTiles);
        if(result != null)return result;

        // If not matched check if the array's reverse matches.
        Array<ShipTile> reverseAndOrientArray = reverseAndOrientArray(tiles);
        result = attemptArrayMatch(reverseAndOrientArray);
        if(result != null)return result;

        // If all else fails
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
        // TODO : have tile Orienter orient the array.
        // Get the orientation and review the translation procedure
        int orientation = getOrientation(tiles);

        switch (orientation) {
            case 0 :
                System.out.println("Up Orientation");
                return tiles; // No change needed
            case 1 :
                System.out.println("Right Orientation");
                break;
            case 2 :
                System.out.println("Down Orientation");
                break;
            case 3 :
                System.out.println("Left Orientation");
        }
        return null;
    }

    /**
     * Returns the orientation between tile0 and tile1
     *
     * @param tiles - Array of tiles to check.
     * @return Returns 0-3 representing orientation between the two tiles. -1 if invalid inputs.
     * 0 = Up
     * 1 = Right
     * 2 = Down
     * 3 = Left
     */
    private int getOrientation(Array<ShipTile> tiles) {
        TileOrienter orienter = new TileOrienter(tiles);
        return orienter.getOrientation();
    }

    private Array<TileRecipes> getAvailableRecipes() {
return null;
    }

}
