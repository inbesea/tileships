package com.shipGame.ncrosby.generalObjects.Ship.tiles.tileUtility;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.ShipTilesManager;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.CoreTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.StandardTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.StrongTile;

/**
 * This gives us ShipTile objects of specific types depending on the ID given.
 *
 * Used in the ship tile manager to get the specific tile type at tile creation.
 */
public class TileTypeFactory {

    /**
     * Returns a specific tile type based on id and position
     * @param vector2
     * @param id
     * @return
     */
    public static ShipTile getShipTileTypeInstance(Vector2 vector2, ID id, ShipTilesManager manager){
        switch (id) {
            case StandardTile :
                return new StandardTile(vector2, manager);
            case CoreTile:
                return new CoreTile(vector2, manager);
            case StrongTile:
                return new StrongTile(vector2, manager);
            default: return null;
        }
    }
}
