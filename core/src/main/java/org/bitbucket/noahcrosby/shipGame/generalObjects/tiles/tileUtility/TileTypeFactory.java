package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.*;

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
        if(id == null) return null;
        switch (id) {
            case StandardTile :
                return new StandardTile(vector2, manager);
            case CoreTile:
                return new CoreTile(vector2, manager);
            case StrongTile:
                return new StrongTile(vector2, manager);
            case ColorTile:
                return new ColorTile(vector2, manager);
            default: return null;
        }
    }
}
