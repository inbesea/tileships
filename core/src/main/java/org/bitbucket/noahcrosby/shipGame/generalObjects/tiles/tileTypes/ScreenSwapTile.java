package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class ScreenSwapTile extends ShipTile{


    public ScreenSwapTile(Vector2 vector2){
        super(vector2, ID.ScreenSwapTile, TileTypeData.ScreenSwapTile);
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }
}
