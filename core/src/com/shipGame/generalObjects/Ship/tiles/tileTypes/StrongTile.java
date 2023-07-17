package com.shipGame.generalObjects.Ship.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.javapoet.Resources;
import com.shipGame.ID;
import com.shipGame.generalObjects.Ship.ShipTilesManager;
import com.shipGame.generalObjects.Ship.tiles.tileUtility.TileTypeData;

public class StrongTile extends ShipTile implements ShipTileInterface{
    public StrongTile(Vector2 vector2, ShipTilesManager manager){
        super(vector2, ID.StrongTile, TileTypeData.StrongTile, manager);
    }

    @Override
    public boolean isInvulnerable(){
        return true;
    }

    @Override
    public Texture getTexture() {
        return Resources.ShipTileStrongTexture;
    }

    @Override
    public boolean deleteFromGame() {
        destroySelf();
        return true;
    }
}
