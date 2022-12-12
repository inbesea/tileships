package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.player.TileHoverIndicator;

import java.util.Stack;

public class TileStackManager {

    public boolean collectTiles = false;
    // Lives in the stack manager
    private TileHoverIndicator tileHoverIndicator;
    private Stack<ShipTile> stackedTiles;
    // How many tiles can be grabbed for a stack of tiles.
    private int stackLimit = 5;

    /**
     * Class to handle collecting tiles.
     */
    public TileStackManager(){
        tileHoverIndicator = new TileHoverIndicator(new Vector2(0,0), new Vector2(64,64)); // Location doesn't matter
        stackedTiles = new Stack<>();
    }

    /**
     * Check if the stack is at the limit
     * @return
     */
    public boolean isFull(){
        if(stackedTiles.size() == stackLimit){
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
        collectTiles = true;
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
}
