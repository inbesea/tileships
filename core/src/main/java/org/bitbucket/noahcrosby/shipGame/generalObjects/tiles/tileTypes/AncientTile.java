package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class AncientTile extends ShipTile{
    public boolean powered;

    /**
     * ShipTiles are the basic unit of a ship. They are boxes of data, and can be extended to do more.
     * The position needs to a multiple of TILE_SIZE or the index will be wrong.
     *
     * This tile represents the ancient powered tiles
     *
     * @param position
     * @param id
     * @param typeData
     */
    public AncientTile(Vector2 position, ID id, TileTypeData typeData) {
        super(position, id, typeData);
    }

    public AncientTile(Vector2 vector2) {
        super(
            vector2,
            ID.AncientTile,
            TileTypeData.AncientTile
        );
    }

    @Override
    public Texture getTexture() {
        if(powered){
            return Resources.PowerTilePoweredTexture;
        } else {
            return Resources.PowerTileUnpoweredTexture;
        }
    }

    @Override
    public boolean isInvulnerable(){
        return true;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }

    @Override
    public void newNeighbor(ShipTile newNeighbor) {

    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }
}
