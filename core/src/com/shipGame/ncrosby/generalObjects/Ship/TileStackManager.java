package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.player.TileHoverIndicator;

public class TileStackManager {

    public boolean collectTiles = false;
    // Lives in the stack manager
    private TileHoverIndicator tileHoverIndicator;
    private Array<ShipTile> collectedTiles;
    // How many tiles can be grabbed for an array of tiles.
    private int collectedTileLimit = 5;

    /**
     * Class to handle collecting tiles.
     */
    public TileStackManager(){
        tileHoverIndicator = new TileHoverIndicator(new Vector2(0,0), new Vector2(64,64)); // Location doesn't matter
        collectedTiles = new Array<>();
    }

    /**
     * Check if the stack is at the limit
     * @return
     */
    public boolean isFull(){
        if(collectedTiles.size == collectedTileLimit){
            return true;
        } else {
            return false;
        }
    }

    public TileHoverIndicator getTileHoverIndicator() {
        return tileHoverIndicator;
    }

    public void setHoverIndicator(float x, float y){
        tileHoverIndicator.setX(x);
        tileHoverIndicator.setY(y);
    }

    /**
     * Checks if the hovers should draw
     * @return
     */
    public boolean hoverShouldDraw() {
        return tileHoverIndicator.isHoverDrawing();
    }

    /**
     * Sets if the hover position should draw.
     * @param shouldDraw
     */
    public void setHoverShouldDraw(boolean shouldDraw){
        tileHoverIndicator.setDrawHover(shouldDraw);
    }

    public void startCollect() {
        System.out.println("Begin collecting tiles");
        if(!collectedTiles.isEmpty())collectedTiles.clear();

        collectTiles = true;
    }

    /**
     * Method to return the collected stack, and ends collection mode.
     * @return - a Stack object of ShipTiles.
     */
    public Array<ShipTile> endCollect() {
        Array<ShipTile> shipTileStack = getTileArray();
        System.out.println("End collecting tiles");
        collectTiles = false;
        return shipTileStack;
    }

    public void setDrawHover(boolean shouldDraw) {
        tileHoverIndicator.setDrawHover(shouldDraw);
    }

    /**
     * Checks if the hover indicator is drawing.
     * @return
     */
    public boolean isHoverDrawing() {
        return tileHoverIndicator.isHoverDrawing();
    }

    public boolean isCollectingTiles() {
        return collectTiles;
    }

    /**
     * Standard getter
     * @return - Stack of tiles as Stack Object
     */
    public Array<ShipTile> getTileArray() {
        return collectedTiles;
    }

    /**
     * Add tile to manager stack
     * @param shipTile - Tile to stack
     */
    public void addTile(ShipTile shipTile) {
        collectedTiles.add(shipTile);
    }
}
