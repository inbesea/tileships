package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

/**
 * Basic tile that is a color. Abstract material tile, just has a color
 * We are using this for arcade mode, but there's no reason we couldn't use this for other modes
 */
public class ColorTile extends ShipTile{

    private ArcadeColors color;
    private Texture colorTexture;
    private boolean isInvulnerable = false;

    /**
     * Color tile allows a tile to change color on the fly
     *
     * @param position - the position of the tile
     */
    public ColorTile(Vector2 position) {
        super(position, ID.ColorTile, TileTypeData.ColorTile);

        setColor(ArcadeColors.getRandomColor()); // Default color is red. We can set this again after the tile is created
    }

    @Override
    public Texture getTexture() {
        return colorTexture;
    }

    /**
     * When a color tile is set to a specific color it will update the texture reference
     * @param color
     */
    public void setColor(ArcadeColors color){
        this.color = color;
        this.colorTexture = ArcadeColors.getTileTexture(color);
    }

    @Override
    public boolean deleteFromGame() {
        return setIsDeadTrue();
    }

    @Override
    public boolean isInvulnerable() {
        return isInvulnerable;
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

    /**
     * Setter method to set the invulnerability
     * @param isInvulnerable
     */
    public void setInvulnerability(boolean isInvulnerable){
        this.isInvulnerable = isInvulnerable;
    }

    public ArcadeColors getColor() {
        return color;
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        Gdx.app.log("Color Tile", "NOT SET");
    }
}
