package com.shipGame.generalObjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.player.TileHoverIndicator;

/**
 * Essentially an array and hover indicator.
 * Used to track when tiles are being collected, to hold collected tiles and return the stack of tiles after
 * the collect action finishes.
 * This class can be toggled on, and off to control tile addition.
 */
public class CollectionManager {

    public boolean collectTiles = false;
    // Lives in the stack manager
    private TileHoverIndicator tileHoverIndicator;
    private Array<ShipTile> collectedTiles;
    // How many tiles can be grabbed for an array of tiles.
    private int collectedTileLimit = 5;

    /**
     * Class to handle collecting tiles.
     */
    public CollectionManager(){
        tileHoverIndicator = new TileHoverIndicator(new Vector2(0,0), new Vector2(ShipTile.TILESIZE,ShipTile.TILESIZE)); // Location doesn't matter
        collectedTiles = new Array<>();
    }

    /**
     * Check if the stack is at the limit
     * @return
     */
    public boolean isFull(){
        if(collectedTiles.size >= collectedTileLimit){
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
     * @return - true if added, false if not.
     */
    public boolean addTile(ShipTile shipTile) {
        if (!isCollectingTiles()) {
            throw new RuntimeException("Trying to collect tiles while not collecting.");
        }
        if (isTileCollected(shipTile) || shipTile == null){
            return false;
        } else if (!this.isFull()) { // Confirm the hovered tile is a neighbor
            if (collectedTiles.isEmpty() || shipTile.isNeighbor(collectedTiles.peek())) {
                Sound collectSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/collectTileSound.mp3"));
                collectSound.play();
                System.out.println("Adding tile to array stack!");
                collectedTiles.add(shipTile);
                return true;
            } else {
                System.out.println("Collapse array is full!");
                return false;
            }
        }
        return false;
    }

    /**
     * Returns true if tile is present in array
     * @param tile - tile to check
     * @return - true if present, else false
     */
    public boolean isTileCollected(ShipTile tile) {
        return collectedTiles.contains(tile, true);
    }

    public void cancelCurrentCollectArray() {
        if(isCollectingTiles()){
            collectedTiles.clear();
        }
    }
}
