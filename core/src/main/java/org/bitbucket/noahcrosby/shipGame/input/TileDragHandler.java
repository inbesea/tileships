package org.bitbucket.noahcrosby.shipGame.input;

import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.TileArrayToString;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

public class TileDragHandler extends InputAdapter {

    private final Player player;
    private final Ship playerShip;
    private boolean dragging;

    /**
     * Handles input from the player for grabbing tiles from the player's ship.
     *
     * TODO : This should be moved to the multiple input class and reworked. It could even be dissolved into other classes
     * Probably a case of Shotgun Surgery <a href="https://refactoring.guru/smells/shotgun-surgery">...</a>
     * @param player - Player object that will use this handler
     */
    public TileDragHandler(Player player) {
        this.player = player;
        this.playerShip = player.getPlayerShip();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) return false;
        // Set init values
        setDragging(true);

        // Handle init collect click
        if (playerShip.isCollectingTiles()) { // Begin collecting tiles for condensing
            playerShip.updateCollect(new Vector3(screenX, screenY, 0f));
        } else { // If not collecting, assume pick up and drag tile

            // Get a tile and check if it can be picked up.
            ShipTile pickedUpTile = playerShip.returnTile(screenX, screenY);
            boolean selectedTileCanBeGrabbed = canGrabTile(pickedUpTile);

            if (selectedTileCanBeGrabbed) {
                pickUpTile(pickedUpTile);
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!getDragging()) return false;

        // Need to handle dragging to collect more tiles
        ShipTile draggedTile = playerShip.getDraggedTile();
        if (playerShip.isCollectingTiles() && !playerShip.collapseStackIsFull()) {
            // After get a tile we can check if the stack is complete or not.
            // if it is then we can turn off collecting tiles. A fullStack Check is not needed.
            playerShip.updateCollect(new Vector3(screenX, screenY, 0));
        } else if (draggedTile != null) { // Dragging a tile
            // Drag the tile with mouse
            draggedTile.setX(screenX - ShipTile.TILE_SIZE / 2.0f);
            draggedTile.setY(screenY - ShipTile.TILE_SIZE / 2.0f);
            //TODO : Add a sound here to make moving tiles feel better.
            // The sound can increase in pitch based on the distance moved (use play(volume, pitch, pan))
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) return false;

        // Need to handle letting go of the mouse to construct.
        if (playerShip.isCollectingTiles()) {
            /*
            Note : This is going to change as we understand how this should work for the player
            We probably don't want the collapse mode to end if the player releases the mouse button
            without constructing anything.
            If the player doesn't make anything then we keep going, but the array is wiped.
            */
            Array<ShipTile> collectedTileArray = playerShip.getCollapseCollect(); // Return collected
            // Continue collect with clear array?
            if (collectedTileArray.isEmpty()) {
                System.out.println("Tiles collected : None");
            } else {
                TileArrayToString tileArrayToString = new TileArrayToString(collectedTileArray);
                System.out.println("Tiles collected : " + tileArrayToString.tileArrayToPrintString() + " Size : " + collectedTileArray.size);
            }
            playerShip.buildNewTile(collectedTileArray);
        } else if (playerShip.getDraggedTile() != null) { // If there is a tile being dragged
            placeDraggedTile(playerShip, screenX, screenY);
        }
        setDragging(false);
        return true;
    }

    private boolean canGrabTile(ShipTile tile) {
        if (tile == null) return false; // Check if a tile was grabbed

        // Is player on the tile in question?
        boolean leftCornerOff = playerShip.returnTile(player.getX(), player.getY()) != tile;
        boolean rightCornerOff = playerShip.returnTile(player.getX() + player.getWidth(), player.getY()) != tile;

        return leftCornerOff && rightCornerOff;// Check if tile is same as tile that is stood on
    }

    /**
     * Removes the tile from the Ship and moves to the dragged tile variable
     *
     * @param pickedUpTile - Tile selected to drag
     */
    private void pickUpTile(ShipTile pickedUpTile) {
        playerShip.removeTileFromShip(pickedUpTile);
        playerShip.setDraggedTile(pickedUpTile); // Set intermediate tile to *remove from existing tiles*
        Resources.sfxPickUpTileQuick0.play(AppPreferences.getAppPreferences().getSoundVolume());
    }

    /**
     * Adds the dragged tile back to the ship and sets dragged to null.
     * Note : This loses the identity of the original tile!! It is added via ID reference
     *
     * @param playerShip - Ship to add tile to
     * @param x - x position of dragged tile
     * @param y - y position of dragged tile
     */
    private void placeDraggedTile(Ship playerShip, float x, float y) {
        playerShip.addTileToShip(x, y, playerShip.getDraggedTile().getID());
        Resources.sfxPlaceTileSound.play(AppPreferences.getAppPreferences().getSoundVolume());
        // Dispose of used dragged tile references
        playerShip.setDraggedTile(null);
    }

    /**
     * Standard setter for dragging bool
     * @param dragging
     */
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    /**
     * Standard getter for dragging boolean
     * @return
     */
    public boolean getDragging(){
        return this.dragging;
    }
}
