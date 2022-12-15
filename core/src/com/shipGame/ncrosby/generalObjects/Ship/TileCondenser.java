package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileRecipes;

/**
 * Class to build out new instances of tiles based on ordered arrays of tiles.
 */
public class TileCondenser {

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
        if(tiles.isEmpty()){
            Gdx.app.error("BuildingTile","Empty array passed to TileCondenser");
            return null;
        } else {

            Array<ShipTile> upTurnedTiles = rotateArray(tiles);

            Array<TileRecipes> recipes = getAvailableRecipes(); // Array of recipes available to the player.
            for(int i = 0 ; i < tiles.size ; i++){

            }
        }
        return null;
    }

    /**
     * Takes array and rotates it to point upwards.
     *
     * This is done by looking at the first two and checking the direction.
     * @return
     */
    private Array<ShipTile> rotateArray(Array<ShipTile> tiles) {
        // Check first two
        ShipTile tile0 = tiles.get(0);
        ShipTile tile1 = tiles.get(1);
        int orientation = getOrientation(tile0, tile1);

        return null;
    }

    /**
     * review a pair of tiles to parse their relative orientation and return an int representing orientation.
     *
     * This is based on the index's relationship to one another, and they must be orthogonally adjacent
     * The result is only meant to be a way to match the shape to a recipe so orientation of the containing ship should not matter.
     *
     * @param tile0
     * @param tile1
     * @return Returns 0-3 representing orientation between the two tiles. -1 if invalid inputs.
     */
    private int getOrientation(ShipTile tile0, ShipTile tile1) {
        int result;
        Vector2 tile0Index = new Vector2(tile0.getxIndex(), tile0.getyIndex());
        Vector2 tile1Index = new Vector2(tile1.getxIndex(), tile1.getyIndex());

        boolean xSame = tile0Index.x == tile1Index.x;
        boolean ySame = tile0Index.y == tile1Index.y;

        // Addition
        if(tile0Index.y + 1 == tile1Index.y && xSame){ // Up
            result = 0;
            return result;
        } else if(tile0Index.x + 1 == tile1Index.x && ySame) { // Right
            return 1;
        } else if(tile0Index.y - 1 == tile1Index.y && xSame) { // Down
            return 2;
        } else if(tile0Index.x - 1 == tile1Index.x && ySame) { // Left
            return 3;
        } else {
            Gdx.app.debug("CondenserMessage", "Incomparable tiles passed to GetOrientation()" + tile0Index + " " + tile1Index);
            result = -1;
            return result;
        }
    }

    private Array<TileRecipes> getAvailableRecipes() {
return null;
    }

}
