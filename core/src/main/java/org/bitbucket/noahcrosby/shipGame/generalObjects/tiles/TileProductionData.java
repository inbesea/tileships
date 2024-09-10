package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles;

import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

/**
 * Intermediate Parameter Object representing the tile being produced.
 */
public class TileProductionData {

    private float x,y;
    private int[] index;
    private ShipTile tileLiteral;
    private ID id;
    private boolean canFloat = false;

    public TileProductionData(float x, float y, ShipTile tile) {
        initDefault(x,y);
        this.tileLiteral = tile;
    }

    public TileProductionData(float x, float y, ID id) {
        initDefault(x,y);
        this.id = id;
    }

    private void initDefault(float x, float y){
        this.x = x;
        this.y = y;
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

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

}
