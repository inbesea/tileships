package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
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
            Array<TileRecipes> recipes = getAvailableRecipes(); // Array of recipes available to the player.
            for(int i = 0 ; i < tiles.size ; i++){

            }
        }
        return null;
    }

    private Array<TileRecipes> getAvailableRecipes() {
return null;
    }

}
