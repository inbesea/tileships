package com.shipGame.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.shipGame.generalObjects.Player;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.generalObjects.Ship.TileArrayToString;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;

public class TileDragHandler extends InputAdapter {

    private final Player player;
    private final Ship playerShip;
    private boolean dragging;

    public TileDragHandler(Player player) {
        this.player = player;
        this.playerShip = player.getPlayerShip();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) return false;
        // Set init values
        dragging = true;

        // Handle init collect click
        if (playerShip.isCollectingTiles()) {
            playerShip.updateCollect(new Vector3(screenX, screenY, 0f));
        } else { // Pick up and drag tile
            // Get a tile and check if it can be picked up.
            ShipTile pickedUpTile = playerShip.returnTile(screenX, screenY);
            boolean canGrabTile = canGrabTile(pickedUpTile);

            if (canGrabTile) {
                pickUpTile(pickedUpTile);
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;

        // Need to handle dragging to collect more tiles
        ShipTile draggedTile = playerShip.getDraggedTile();
        if (playerShip.isCollectingTiles() && !playerShip.collapseStackIsFull()) {
            // After get a tile we can check if the stack is complete or not.
            // if it is then we can turn off collecting tiles. A fullStack Check is not needed.
            playerShip.updateCollect(new Vector3(screenX, screenY, 0));
        } else if (draggedTile != null) { // Dragging a tile
            // Drag the tile with mouse
            draggedTile.setX(screenX - ShipTile.TILESIZE / 2.0f);
            draggedTile.setY(screenY - ShipTile.TILESIZE / 2.0f);
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
                System.out.println("Tiles collected : " + tileArrayToString.tilesToString() + " Size : " + collectedTileArray.size);
            }
            playerShip.buildNewTile(collectedTileArray);
        } else if (playerShip.getDraggedTile() != null) { // If there is a tile being dragged
            handlePlacingDragged(playerShip, screenX, screenY);
        }
        dragging = false;
        return true;
    }

    private boolean canGrabTile(ShipTile tile) {
        if (tile == null) return false; // Check if a tile was grabbed

        // Is player on the tile in question?
        boolean leftCornerOff = playerShip.returnTile(player.getX(), player.getY()) != tile;
        boolean rightCornerOff = playerShip.returnTile(player.getX() + player.getWidth(), player.getY()) != tile;

        return leftCornerOff && rightCornerOff;// Check if tile is same as tile that is stood on
    }

    private void pickUpTile(ShipTile pickedUpTile) {
        playerShip.removeTileFromShip(pickedUpTile);
        playerShip.setDraggedTile(pickedUpTile); // Set intermediate tile to *remove from existing tiles*
    }

    private void handlePlacingDragged(Ship playerShip, float x, float y) {
        playerShip.addTileToShip(x, y, playerShip.getDraggedTile().getID());
        // Dispose of used dragged tile references
        playerShip.setDraggedTile(null);
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}