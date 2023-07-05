package com.shipGame.generalObjects.Ship.tiles.tileTypes;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ID;
import com.shipGame.generalObjects.Ship.ShipTilesManager;
import com.shipGame.generalObjects.Ship.tiles.tileUtility.TileTypeData;

/**
 * Basic tile generated from a basic asteroid hit
 */
public class StandardTile extends ShipTile{

    public StandardTile(Vector2 vector2, ShipTilesManager manager){
        super(vector2, ID.StandardTile, TileTypeData.StandardTile, manager);

    }

    @Override
    public boolean isInvulnerable(){
        return false;
    }

    @Override
    public boolean deleteFromGame() {
        destroySelf();
        return true;
    }
}
