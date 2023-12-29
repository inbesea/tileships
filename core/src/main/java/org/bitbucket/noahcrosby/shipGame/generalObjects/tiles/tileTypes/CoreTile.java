package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;
import org.bitbucket.noahcrosby.javapoet.Resources;

public class CoreTile extends ShipTile {

    /**
     * These tiles will all need health, and a way to relate to tiles next to them..?
     * But they will need to be stored in a 2d array.
     * So when the game initializes there will need to be an array of tiles built out.
     *
     * @param position
     * @param id
     */
    public CoreTile(Vector2 position, ID id, TileTypeData typeData) {
        super(position, id, typeData);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
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
    public CoreTile(Vector2 position) {
        super(position, ID.CoreTile, TileTypeData.CoreTile);
    }

    @Override
    public Texture getTexture() {
        return Resources.ShipTileCoreTexture;
    }

    @Override
    public boolean deleteFromGame() {
        setIsDeadTrue();
        return true;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }
}
