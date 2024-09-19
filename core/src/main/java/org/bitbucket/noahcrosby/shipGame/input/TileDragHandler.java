package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.effects.EffectContent;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.TileArrayToString;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.util.SecondOrderDynamics;

import static org.bitbucket.noahcrosby.shipGame.util.generalUtil.returnUnprojectedInputPosition;

public class TileDragHandler extends InputAdapter {

    private static final float PLACE_DRAG_GOAL = 20f;
    private Ship playerShip;
    private boolean dragging;
    private float placementSpeed = 15f;
    private int shake = 0;
    public Signal<EffectContent> publisher;
    private ShipTile draggedTile;

    SecondOrderDynamics sOD_x;
    SecondOrderDynamics sOD_y;
    Float f = 3f, z = 0.65f, r = 2f; // Second Order Dynamic constants
    boolean placingTile = false;
    // Used at the end of the drag to place the tile smoothly
    private Vector2 placementIndicator;
    private Player player;

    /**
     * Handles input from the player for grabbing tiles from the player's ship.
     * <p>
     * TODO : This should be moved to the multiple input class and reworked. It could even be dissolved into other classes
     * Probably a case of Shotgun Surgery <a href="https://refactoring.guru/smells/shotgun-surgery">...</a>
     */
    public TileDragHandler(Ship playerShip) {
        initCommonElements(playerShip);
    }

    public TileDragHandler(Ship playerShip, Player player) {
        this.player = player;
        initCommonElements(playerShip);
    }

    // Keep it DRY
    private void initCommonElements(Ship playerShip){
        this.playerShip = playerShip;
        sOD_x = new SecondOrderDynamics(f, z, r, 0f);
        sOD_y = new SecondOrderDynamics(f, z, r, 0f);
        publisher = new Signal<>();
    }

    /**
     * Updates a dragged tile if playership has one.
     * This update works with the second order dynamics model
     */
    public void update() {
        if (getDragging() && playerShip.isDragging()) { // We should only need to check is dragging in one place god damnit.
            ShipTile draggedTile = playerShip.getDraggedTile();
            Vector3 playerInputPosition = returnUnprojectedInputPosition(TileShipGame.getCurrentCamera());

            draggedTile.setX(playerInputPosition.x - ShipTile.TILE_SIZE / 2.0f);
            draggedTile.setY(playerInputPosition.y - ShipTile.TILE_SIZE / 2.0f);
        }
        if (placingTile) { // True after touchup()
            ShipTile draggedTile = playerShip.getDraggedTile();
            boolean closeEnoughToPlace = placementIndicator.dst(new Vector2(draggedTile.getCenter().x, draggedTile.getCenter().y)) < PLACE_DRAG_GOAL;
            if (closeEnoughToPlace) {
                placeDraggedTile();
                placingTile = false;
                setDragSODVariables();
            } else {
                // Use ship-determined vector for placement
                Vector2 draggedTilePosition = draggedTile.getPosition();

                float dx = (placementIndicator.x - ShipTile.TILE_SIZE / 2) - draggedTilePosition.x;
                float dy = (placementIndicator.y - ShipTile.TILE_SIZE / 2) - draggedTilePosition.y;

                float newX = draggedTilePosition.x + (((dx * placementSpeed) * Gdx.graphics.getDeltaTime()));
                float newY = draggedTilePosition.y + (((dy * placementSpeed) * Gdx.graphics.getDeltaTime()));

                draggedTile.setX(newX);
                draggedTile.setY(newY);
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) return false;
        // Set init values
        setIsDraggingTile(true); // This is setting dragging to true before we start dragging.

        // Handle init collect click
        if (playerShip.isCollectingTiles()) { // Begin collecting tiles for condensing
            playerShip.lookForTileToCollect(new Vector3(screenX, screenY, 0f));
        } else { // If not collecting, assume pick up and drag tile

            if (placingTile) { // Handle tiles mid-placement
                catchTileMidPlacement();
                return false; // Don't pick up if already dragging a tile
            }

            // Get a tile and check if it can be picked up.
            ShipTile pickedUpTile = playerShip.returnTile(screenX, screenY);

            if (canGrabTile(pickedUpTile)) {
                pickUpTile(pickedUpTile);

                // init new drag
                initDraggedTilesSmoothMovement(pickedUpTile);
            }
        }

        return true;
    }

    /**
     * Sets SOD variables for smooth movements
     * @param pickedUpTile - tile to get location info from
     */
    private void initDraggedTilesSmoothMovement(ShipTile pickedUpTile) {
        sOD_x.setXp(pickedUpTile.getX());
        sOD_x.setY(pickedUpTile.getX());
        sOD_y.setXp(pickedUpTile.getY());
        sOD_y.setY(pickedUpTile.getY());
    }

    private void catchTileMidPlacement() {
        ShipTile pickedUpTile = playerShip.getDraggedTile();
        if(pickedUpTile == null)return;

        setIsDraggingTile(true);
        placingTile = false;

        pickUpTile(pickedUpTile);
        initDraggedTilesSmoothMovement(pickedUpTile);
    }

    /**
     * Handles dragging the input point around the screen.
     *
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
            playerShip.lookForTileToCollect(new Vector3(screenX, screenY, 0));
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
            Array<ShipTile> collectedTileArray = playerShip.finishCollapseCollect(); // Return collected
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
        setIsDraggingTile(false);
        return true;
    }

    private boolean canGrabTile(ShipTile tile) {
        if (tile == null) return false; // Check if a tile was grabbed

        if(player == null){
            return !tile.isLocked();
        } else {// Is player on the tile in question?
            boolean leftCornerOff = playerShip.returnTile(player.getX(), player.getY()) != tile;
            boolean rightCornerOff = playerShip.returnTile(player.getX() + player.getWidth(), player.getY()) != tile;

            return !tile.isLocked() && leftCornerOff && rightCornerOff;// Check if tile is same as tile that is stood on
        }
    }

    /**
     * Removes the tile from the ship and moves to the dragged tile variable
     *  Handles other background changes
     * @param pickedUpTile - Tile selected to drag
     */
    private void pickUpTile(ShipTile pickedUpTile) {
        this.setDraggingSound(true);

        playerShip.removeTileFromShip(pickedUpTile);
        playerShip.setDraggedTile(pickedUpTile); // Set intermediate tile to *remove from existing tiles*
        pickedUpTile.pickedUp();
        setIsDraggingTile(true);
        Resources.PickUpTileQuick0Sfx.play(
            AppPreferences.getAppPreferences().getSoundVolume() * 8);
    }

    /**
     * Adds the dragged tile back to the ship and sets dragged to null.
     * Note : This retains the identity of the original tile
     *
     * @param playerShip - ship to add tile to
     * @param x          - x position of dragged tile
     * @param y          - y position of dragged tile
     */
    private void beginPlacingDragged(Ship playerShip, float x, float y) {
        setIsDraggingTile(false);
        placingTile = true;
        placementIndicator = playerShip.getTileManager().getPlacementVector();
    }

    private void setDragSODVariables() {
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

        publishTilePlacement(new EffectContent(EffectContent.BLOCK_PLACEMENT, tempTile));
    }

    /**
     * Emit publish data
     * @param effectsContent
     */
    private void publishTilePlacement(EffectContent effectsContent) {
        publisher.dispatch(effectsContent);
    }

    /**
     * Standard setter for dragging bool
     *
     * @param dragging
     */
    public void setIsDraggingTile(boolean dragging) {
        this.dragging = dragging;
    }

    /**
     * Starts and loops the tile moving/dragging sound effect.
     *
     * @param makeSound - conditional to start or stop the moving tile sound effect.
     */
    private void setDraggingSound(boolean makeSound) {
        if (makeSound) {
            Resources.MovingTileSoundSfx.loop(AppPreferences.getAppPreferences().getSoundVolume() * 0.1f);
        } else {
            Resources.MovingTileSoundSfx.stop();
        }
    }

    /**
     * Standard getter for dragging boolean
     *
     * @return
     */
    public boolean getDragging() {
//        if(this.dragging == (playerShip.getDraggedTile() == null)) {
//            throw new RuntimeException("Dragging without dragged tile!");
//        }
        return this.dragging;
    }
}
