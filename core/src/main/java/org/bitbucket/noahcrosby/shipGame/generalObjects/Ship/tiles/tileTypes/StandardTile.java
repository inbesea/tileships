package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileUtility.TileTypeData;
import org.bitbucket.noahcrosby.javapoet.Resources;

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
    public Texture getTexture() {
        return Resources.ShipTileRedTexture;
    }

    @Override
    public boolean deleteFromGame() {
        destroySelf();
        return true;
    }
}
