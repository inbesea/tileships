package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.TileArrayToString;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.screens.GameScreen;
import org.bitbucket.noahcrosby.shipGame.util.SecondOrderDynamics;

import static org.bitbucket.noahcrosby.shipGame.util.generalUtil.returnUnprojectedInputPosition;

public class TileDragHandler extends InputAdapter {

    private static final float PLACE_DRAG_GOAL = 50f;
    private final Player player;
    private final Ship playerShip;
    private boolean dragging;

    SecondOrderDynamics sOD_x;
    SecondOrderDynamics sOD_y;
    Float f = 3f, z = 0.65f, r = 2f; // Second Order Dynamic constants
    Float placementR = 4f;
    boolean placingTile = false;
    // Used at the end of the drag to place the tile smoothly
    private Vector2 placementIndicator;

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
        sOD_x = new SecondOrderDynamics(f, z, r, 0f);
        sOD_y = new SecondOrderDynamics(f, z, r, 0f);
    }

    /**
     * Updates a dragged tile if playership has one.
     * This update works with the second order dynamics model
     */
    public void update() {
        if(getDragging() && playerShip.isDragging()) {
            ShipTile draggedTile = playerShip.getDraggedTile();
            Vector3 playerInputPosition = returnUnprojectedInputPosition(GameScreen.getGameCamera());
            float targetX = playerInputPosition.x - ShipTile.TILE_SIZE / 2.0f;
            float targetY = playerInputPosition.y - ShipTile.TILE_SIZE / 2.0f;
            draggedTile.setX(sOD_x.Update(Gdx.graphics.getDeltaTime(), targetX , null));
            draggedTile.setY(sOD_y.Update(Gdx.graphics.getDeltaTime(), targetY , null));
        }
        if(placingTile){ // True after touchup()
            ShipTile draggedTile = playerShip.getDraggedTile();
            boolean closeEnoughToPlace = placementIndicator.dst(new Vector2(draggedTile.getCenter().x, draggedTile.getCenter().y)) < PLACE_DRAG_GOAL;
            if(closeEnoughToPlace){
                placeDraggedTile();
                placingTile = false;
                setDragSODVariables();
            } else {
                // Use ship-determined vector for placement
                float targetX = placementIndicator.x - ShipTile.TILE_SIZE / 2.0f;
                float targetY = placementIndicator.y - ShipTile.TILE_SIZE / 2.0f;

                draggedTile.setX(sOD_x.Update(Gdx.graphics.getDeltaTime(), targetX , null));
                draggedTile.setY(sOD_y.Update(Gdx.graphics.getDeltaTime(), targetY , null));
                Gdx.app.debug("Placement","Trying to go to " +
                    placementIndicator.x + ", " + placementIndicator.y + " Currently at " +
                    draggedTile.getX() + ", " + draggedTile.getY() +
                    " Target is " + targetX + ", " + targetY);
            }
        }
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
                this.setDraggingSound(true);
                pickUpTile(pickedUpTile);
                pickedUpTile.pickedUp();

                // init new drag
                sOD_x.setXp(pickedUpTile.getX());
                sOD_x.setY(pickedUpTile.getX());
                sOD_y.setXp(pickedUpTile.getY());
                sOD_y.setY(pickedUpTile.getY());
            }
        }

        return true;
    }

    /**
     * Handles dragging the input point around the screen.
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!getDragging()) return false; // Nothing to do if not dragging a tile

        // Need to handle dragging to collect more tiles
        ShipTile draggedTile = playerShip.getDraggedTile();
        if (playerShip.isCollectingTiles() && !playerShip.collapseStackIsFull()) {
            // After get a tile we can check if the stack is complete or not.
            // if it is then we can turn off collecting tiles. A fullStack Check is not needed.
            playerShip.updateCollect(new Vector3(screenX, screenY, 0));
        } else if (draggedTile != null) { // Dragging a tile
            // the update for the dragged tile is handled each frame by update();
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
            beginPlacingDragged(playerShip, screenX, screenY);
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
     * Removes the tile from the ship and moves to the dragged tile variable
     *
     * @param pickedUpTile - Tile selected to drag
     */
    private void pickUpTile(ShipTile pickedUpTile) {
        playerShip.removeTileFromShip(pickedUpTile);
        playerShip.setDraggedTile(pickedUpTile); // Set intermediate tile to *remove from existing tiles*

        Resources.PickUpTileQuick0Sfx.play(
            AppPreferences.getAppPreferences().getSoundVolume()*8);
    }

    /**
     * Adds the dragged tile back to the ship and sets dragged to null.
     * Note : This retains the identity of the original tile
     *
     * @param playerShip - ship to add tile to
     * @param x - x position of dragged tile
     * @param y - y position of dragged tile
     */
    private void beginPlacingDragged(Ship playerShip, float x, float y) {
        setDragging(false);
        placingTile = true;
        placementIndicator = playerShip.getTileManager().getPlacementVector();
        setPlacementSODVariables();

//        ShipTile tempTile = playerShip.getDraggedTile();
//        /* We could produce a shiptile object here that moves into place with SOD movement.
//        * Second Order Dynamics could make it look nice. Can we do that with a draw bool for the new tile? */
//        playerShip.addTileToShip(x, y, tempTile);
//        tempTile.replaced();
//        Resources.PlaceTileSoundSfx.play(AppPreferences.getAppPreferences().getSoundVolume());
//        this.setDraggingSound(false);
//        // Dispose of used dragged tile references
//        playerShip.setDraggedTile(null);
    }

    /**
     * Sets the behavior variables for placing a tile
     */
    private void setPlacementSODVariables() {
        sOD_x.setR(placementR);
        sOD_y.setR(placementR);
    }

    private void setDragSODVariables(){
        sOD_x.setR(r);
        sOD_y.setR(r);
    }

    /**
     * Places the dragged tile, playing the placement sound, etc.
     */
    private void placeDraggedTile() {
        ShipTile tempTile = playerShip.getDraggedTile();
        /* We could produce a shiptile object here that moves into place with SOD movement.
         * Second Order Dynamics could make it look nice. Can we do that with a draw bool for the new tile? */
        playerShip.addTileToShip(placementIndicator.x, placementIndicator.y, tempTile);
        tempTile.replaced();
        Resources.PlaceTileSoundSfx.play(AppPreferences.getAppPreferences().getSoundVolume());
        this.setDraggingSound(false);
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
     * Starts and loops the tile moving/dragging sound effect.
     * @param makeSound - conditional to start or stop the moving tile sound effect.
     */
    private void setDraggingSound(boolean makeSound) {
        if(makeSound){
            Resources.MovingTileSoundSfx.loop(AppPreferences.getAppPreferences().getSoundVolume() * 0.1f);
        } else {
            Resources.MovingTileSoundSfx.stop();
        }
    }

    /**
     * Standard getter for dragging boolean
     * @return
     */
    public boolean getDragging(){
        return this.dragging;
    }
}
