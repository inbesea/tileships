package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class MetalTile extends ShipTile {
    protected Boolean isInvulnerable = false;

    public MetalTile(Vector2 position) {
        super(position, ID.MetalTile, TileTypeData.MetalTile);
        super.isInvulnerable = isInvulnerable;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }

    @Override
    public Texture getTexture() {
        return Resources.MetalTileTexture;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }
}
