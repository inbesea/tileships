package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.ShipTilesManager;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

/**
 * Basic tile that is a color. Abstract material tile, just has a color
 * We are using this for arcade mode, but there's no reason we couldn't use this for other modes
 */
public class ColorTile extends ShipTile{

    TileColor color;
    Texture colorTexture;

    /**
     * These tiles will all need health, and a way to relate to tiles next to them..?
     * But they will need to be stored in a 2d array.
     * So when the game initializes there will need to be an array of tiles built out.
     *
     * @param position
     * @param id
     * @param typeData
     * @param manager
     */
    public ColorTile(Vector2 position, ID id, TileTypeData typeData, ShipTilesManager manager) {
        super(position, id, typeData, manager);
    }

    @Override
    public Texture getTexture() {
        return colorTexture;
    }

    /**
     * When a color tile is set to a specific color it will update the texture reference
     * @param color
     */
    public void setColor(TileColor color){
        this.color = color;
        this.colorTexture = TileColor.getTexture(color);
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }
}
