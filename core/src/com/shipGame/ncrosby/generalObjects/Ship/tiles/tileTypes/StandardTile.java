package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;

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
}
