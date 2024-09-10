package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles;

import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

/**
 * Intermediate Parameter Object representing the tile being produced.
 */
public class TileProductionData {

    private float x,y;
    private ShipTile tileLiteral;
    private boolean canFloat = false;

    public TileProductionData(float x, float y, ShipTile tile) {
        this.x = x;
        this.y = y;
        this.tileLiteral = tile;
    }

    // TODO: Add constructors for class and ID data later.

    public void setCanFloat(boolean canFloat) {
        this.canFloat = canFloat;
    }
    public boolean getCanFloat() {
        return canFloat;
    }

    public ShipTile getTileLiteral() {
        return tileLiteral;
    }

    public void setTileLiteral(ShipTile tileLiteral) {
        this.tileLiteral = tileLiteral;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
