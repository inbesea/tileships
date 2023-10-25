package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

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

    private TileColor color;
    private Texture colorTexture;
    private boolean isInvulnerable = false;

    /**
     * Color tile allows a tile to change color on the fly
     *
     * @param position - the position of the tile
     * @param manager - the shipTile's manager
     */
    public ColorTile(Vector2 position, ShipTilesManager manager) {
        super(position, ID.ColorTile, TileTypeData.ColorTile, manager);

        setColor(TileColor.getRandomColor()); // Default color is red. We can set this again after the tile is created
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
        return destroySelf();
    }

    @Override
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    /**
     * Setter method to set the invulnerability
     * @param isInvulnerable
     */
    public void setInvulnerability(boolean isInvulnerable){
        this.isInvulnerable = isInvulnerable;
    }
}
