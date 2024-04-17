package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;
import org.bitbucket.noahcrosby.javapoet.Resources;

/**
 * Basic tile generated from a basic asteroid hit
 */
public class StandardTile extends ShipTile{

    public StandardTile(Vector2 vector2){
        super(vector2, ID.StandardTile, TileTypeData.StandardTile);

    }

    @Override
    public boolean isInvulnerable(){
        return false;
    }
    /**
     * Do this when placed
     */
    @Override
    public void replaced() {

    }

    /**
     * Do this when picked up
     */
    @Override
    public void pickedUp() {

    }
    @Override
    public Texture getTexture() {
        return Resources.ShipTileRedTexture;
    }

    @Override
    public boolean deleteFromGame() {
        setIsDeadTrue();
        return true;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }

    @Override
    public boolean isFuel() {
        return true;
    }
}
