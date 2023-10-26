package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class BombTile extends ShipTile {

    /**
     * ShipTiles are the basic unit of a Ship. They are boxes of data, and can be extended to do more.
     * The position needs to a multiple of TILE_SIZE or the index will be wrong.
     *
     * @param position
     * @param id
     * @param typeData
     * @param manager
     */
    public BombTile(Vector2 position, ID id, TileTypeData typeData, ShipTilesManager manager) {
        super(position, id, typeData, manager);
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
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }
}
