package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.player.TileHoverIndicator;
import org.bitbucket.noahcrosby.javapoet.Resources;

/**
 * Essentially an array and hover indicator.
 * Used to track when tiles are being collected, to hold collected tiles and return the stack of tiles after
 * the collect action finishes.
 * This class can be toggled on, and off to control tile addition.
 */
public class CollectionManager {
    long collectionSoundID = 0;
    float collectionSoundPitch = 1f;
    private static float collectionPitchMax = 2.0f;
    public boolean collectTiles = false;
    // Lives in the stack manager
    private final TileHoverIndicator tileHoverIndicator;
    private final Array<ShipTile> collectedTiles;

    public int getCOLLECTED_TILE_LIMIT() {
        return COLLECTED_TILE_LIMIT;
    }

    public void setCOLLECTED_TILE_LIMIT(int COLLECTED_TILE_LIMIT) {
        this.COLLECTED_TILE_LIMIT = COLLECTED_TILE_LIMIT;
    }

    // How many tiles can be grabbed for an array of tiles.
    private int COLLECTED_TILE_LIMIT = 5;

    /**
     * Class to handle collecting tiles.
     */
    public CollectionManager() {
        tileHoverIndicator = new TileHoverIndicator(new Vector2(0, 0), new Vector2(ShipTile.TILE_SIZE, ShipTile.TILE_SIZE)); // Location doesn't matter
        collectedTiles = new Array<>();
    }

    /**
     * Check if the stack is at the limit
     *
     * @return whether the stock is full
     */
    public boolean isFull() {
        return collectedTiles.size >= COLLECTED_TILE_LIMIT;
    }

    public TileHoverIndicator getTileHoverIndicator() {
        return tileHoverIndicator;
    }

    public void setHoverIndicator(float x, float y) {
        tileHoverIndicator.setX(x);
        tileHoverIndicator.setY(y);
    }

    /**
     * Checks if the hovers should draw
     *
     * @return
     */
    public boolean hoverShouldDraw() {
        return tileHoverIndicator.isHoverDrawing();
    }

    /**
     * Sets if the hover position should draw.
     *
     * @param shouldDraw
     */
    public void setHoverShouldDraw(boolean shouldDraw) {
        tileHoverIndicator.setDrawHover(shouldDraw);
    }

    public void startCollect() {
        System.out.println("Begin collecting tiles");

        collectionSoundID = Resources.sfxSelectionBuzzLooped.loop();

        if (!collectedTiles.isEmpty()) collectedTiles.clear();

        collectTiles = true;
    }

    /**
     * Method to return the collected stack, and ends collection mode.
     *
     * @return - a Stack object of ShipTiles.
     */
    public Array<ShipTile> endCollect() {
        Array<ShipTile> shipTileStack = getTileArray();
        System.out.println("End collecting tiles");

        Resources.sfxSelectionBuzzLooped.stop(collectionSoundID);
        collectionSoundPitch = 1f;
        collectTiles = false;
        return shipTileStack;
    }

    public void setDrawHover(boolean shouldDraw) {
        tileHoverIndicator.setDrawHover(shouldDraw);
    }

    /**
     * Checks if the hover indicator is drawing.
     *
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
     *
     * @return - Stack of tiles as Stack Object
     */
    public Array<ShipTile> getTileArray() {
        return collectedTiles;
    }

    /**
     * Add tile to manager stack
     *
     * @param shipTile - Tile to stack
     * @return - true if added, false if not.
     */
    public boolean addTile(ShipTile shipTile) {
        if (!isCollectingTiles()) {
            throw new RuntimeException("Trying to collect tiles while not collecting.");
        }
        if (isTileCollected(shipTile) || shipTile == null) { // If shipTile is already collected or is null
            return false;
        } else if (!this.isFull()) { // Confirm the hovered tile is a neighbor
            if (collectedTiles.isEmpty() || shipTile.isNeighbor(collectedTiles.peek())) {
                collectionSoundPitch += 0.1f;
                pitchSelectionSound();

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

    private void pitchSelectionSound() {
        Resources.sfxSelectionBuzzLooped.setPitch(collectionSoundID, collectionSoundPitch);
        if(collectionSoundPitch > collectionPitchMax){
            collectionSoundPitch = collectionPitchMax;
        }
    }

    /**
     * Returns true if tile is present in array
     *
     * @param tile - tile to check
     * @return - true if present, else false
     */
    public boolean isTileCollected(ShipTile tile) {
        return collectedTiles.contains(tile, true);
    }

    public void cancelCurrentCollectArray() {
        if (isCollectingTiles()) {
            collectedTiles.clear();
        }
    }
}
