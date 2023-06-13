package com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.ShipTilesManager;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileUtility.TileTypeData;

public class CoreTile extends ShipTile {

    /**
     * These tiles will all need health, and a way to relate to tiles next to them..?
     * But they will need to be stored in a 2d array.
     * So when the game initializes there will need to be an array of tiles built out.
     *
     * @param position
     * @param id
     */
    public CoreTile(Vector2 position, ID id, TileTypeData typeData, ShipTilesManager manager) {
        super(position, id, typeData, manager);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    public CoreTile(Vector2 position, ShipTilesManager manager) {
        super(position, ID.CoreTile, TileTypeData.CoreTile, manager);
    }

    @Override
    public boolean deleteFromGame() {
        destroySelf();
        return true;
    }
}
