package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;
import org.bitbucket.noahcrosby.javapoet.Resources;

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
