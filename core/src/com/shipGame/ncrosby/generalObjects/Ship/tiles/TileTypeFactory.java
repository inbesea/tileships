package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;

/**
 * This gives us ShipTile objects of specific types depending on the ID given.
 */
public class TileTypeFactory {
    public static ShipTile getShipTileTypeInstance(Vector2 vector2, ID id){
        switch (id) {
            case StandardTile :
                return new StandardTile(vector2);
            case CoreTile:
                return new CoreTile(vector2);
            default: return null;
        }
    }
}
