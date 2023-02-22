package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;

public class StrongTile extends ShipTile implements ShipTileInterface{
    public StrongTile(Vector2 vector2){
        super(vector2, ID.StrongTile, TileTypeData.StrongTile);
    }
}
