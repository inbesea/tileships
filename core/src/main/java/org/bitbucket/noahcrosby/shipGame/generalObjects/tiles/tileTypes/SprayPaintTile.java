package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class SprayPaintTile extends ShipTile{
    public SprayPaintTile(Vector2 position) {
        super(
            position,
            ID.SprayPaintTile,
            TileTypeData.SprayPaintTile
        );
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }

    @Override
    public Texture getTexture() {
        return Resources.SprayPaintTileTexture;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }
}
