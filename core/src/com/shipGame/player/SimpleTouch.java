package com.shipGame.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.shipGame.generalObjects.Player;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.generalObjects.Ship.TileArrayToString;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.screens.GameScreen;
import com.shipGame.TileShipGame;

import static com.shipGame.util.generalUtil.returnUnprojectedPosition;

/**
 * Main user input handling class.
 * Uses event handling to do things.
 */
public class SimpleTouch implements InputProcessor {

        OrthographicCamera camera;
        Ship playerShip;
        Player player;
        // touch point
        Vector3 tp = new Vector3();
        GameScreen screen;

    boolean isDragging;
        private ShipTile draggedTile;

    /**
     * Constructor for this event handler needs
     * playership, camera and player
     * This is so the player may access the tiles on their current ship.
     *
     * @param gameScreen
     */
    public SimpleTouch(GameScreen gameScreen){
            this.camera = gameScreen.getCamera();
            // A future change could be to add a way to update the player ship.
            this.playerShip = gameScreen.getPlayerShip();
            this.player = gameScreen.getPlayer();
            this.screen = gameScreen;
        }

    /**
     * Method to handle mouse movement
     *
     * @param screenX - x position on screen
     * @param screenY - y position on screen
     * @return - boolean
     */
    @Override public boolean mouseMoved (int screenX, int screenY) {
            if(playerShip.isCollectingTiles()){
                // Fix screen position to camera position
                Vector3 unprojectedV3 = new Vector3(screenX, screenY, 0);
                camera.unproject(unprojectedV3);

                ShipTile tile = playerShip.returnTile(unprojectedV3.x, unprojectedV3.y);
                if(tile != null ){ // Check if on ship
                    // Draw placeholder art on tile
                    playerShip.setHoverShouldDraw(true);
                    Vector2 vector2 = playerShip.getGridAlignedPosition(unprojectedV3.x, unprojectedV3.y);
                    playerShip.setHoverIndicator(vector2.x, vector2.y);
                } else {
                    playerShip.setHoverShouldDraw(false);
                }
            }
            // we can also handle mouse movement without anything pressed
//		camera.unproject(tp.set(screenX, screenY, 0));
            return false;
        }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        //System.out.println("Zooming camera : old amount " + camera.zoom + " scrolled number : " + amountX + " " + amountY);
        // Check the final value you would have based on the new calculation.
        float newValue = camera.zoom + ((amountY * TileShipGame.zoomSpeed) * Gdx.graphics.getDeltaTime());

        if (newValue >= TileShipGame.zoomMax)
            newValue = TileShipGame.zoomMax;
        else newValue = Math.max(newValue, TileShipGame.zoomMin);

        camera.zoom = newValue;

        return false; // Unused bool
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(playerShip.isCollectingTiles() && button == Input.Buttons.RIGHT){
            playerShip.cancelCurrentCollectArray(); // Cancel collect action
        }
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;

        // Set init values
        camera.unproject(tp.set(screenX, screenY, 0));
        setIsDragging(true);
        Vector3 vector3 = returnUnprojectedPosition(camera);

        // Handle init collect click
        if (playerShip.isCollectingTiles()){
                playerShip.updateCollect(vector3);
        } else { // Pick up and drag tile
            // Get a tile and check if it can be picked up.
            ShipTile pickedUpTile = playerShip.returnTile(vector3.x, vector3.y);
            boolean canGrabTile = canGrabTile(pickedUpTile);

            if(canGrabTile){
                pickUpTile(pickedUpTile);
            }
        }

        return true;
        }

    @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
            if (!isDragging) return false;
            camera.unproject(tp.set(screenX, screenY, 0));

            // Need to handle dragging to collect more tiles
            if(playerShip.isCollectingTiles() && !playerShip.collapseStackIsFull()){
                // After get a tile we can check if the stack is complete or not.
                // if it is then we can turn off collecting tiles. A fullStack Check is not needed.
                playerShip.updateCollect(new Vector3(tp.x, tp.y, 0));
            } else if (draggedTile != null){// Dragging a tile
                // Drag the tile with mouse
                draggedTile.setX(tp.x - ShipTile.TILE_SIZE /2.0f);
                draggedTile.setY(tp.y - ShipTile.TILE_SIZE /2.0f);
            } else {

            }

            return true;
        }

        @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            Vector3 mousePosition = returnUnprojectedPosition(camera);

            // Need to handle letting go of the mouse to construct.
            if(playerShip.isCollectingTiles()){
                /*
                Note : This is going to change as we understand how this should work for the player
                We probably don't want the collapse mode to end if the player releases the mouse button without constructing anything.
                If the player doesn't make anything then we keep going, but the array is wiped.
                 */
                Array<ShipTile> collectedTileArray =
                        playerShip.getCollapseCollect(); // Return collected
                // Continue collect with clear array?
                if(collectedTileArray.isEmpty()){
                    System.out.println("Tiles collected : None");
                } else {
                    TileArrayToString tileArrayToString = new TileArrayToString(collectedTileArray);
                    System.out.println("Tiles collected : " + tileArrayToString.tilesToString() + " Size : " + collectedTileArray.size);
                }
                playerShip.buildNewTile(collectedTileArray);
            }else if(draggedTile != null){ // If there is a tile being dragged
                handlePlacingDragged(playerShip, mousePosition);
            }

            setIsDragging(false);
            return true;
        }

        @Override public boolean keyDown (int keycode) {
        System.out.println("Keycode pressed is : " + keycode);

        // Begin collecting tiles for collapse
        if(!playerShip.isCollectingTiles() && keycode == 59){
            if(draggedTile != null){ // If holding tile
                playerShip.addTileToShip(draggedTile.getX(), draggedTile.getY(), draggedTile.getID());
                setDraggedTileToNull();
                setIsDragging(false);
            }
            playerShip.startCollapseCollect(); // Begins ship collecting
        }
        if(keycode == 111){
            screen.quitGame();
        }

            return false;
        }

        @Override public boolean keyUp (int keycode) {

            if(playerShip.isCollectingTiles() && keycode == 59){ // If user keys up should
                attemptNewTileProduction();
            }
            return false;
        }

    private void attemptNewTileProduction() {
        Array<ShipTile> shipTileArray =
                playerShip.finishCollapseCollect(); // Ends collecting
        if(shipTileArray.isEmpty()){
            System.out.println("Tiles collected : None");
        }else {
            System.out.println("Tiles collected : " + shipTileArray + " Size : " + shipTileArray.size);
            playerShip.buildNewTile(shipTileArray);
        }
    }

    @Override public boolean keyTyped (char character) {
            return false;
        }


    public boolean isDragging() {
        return isDragging;
    }

    public void setIsDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    /**
     * Handle moving tiles around while picking up tiles
     * Assumes that the pickedup tile is valid to pick up and that it can be removed from the ship.
     * @param pickedUpTile
     */
    private void pickUpTile(ShipTile pickedUpTile) {
        draggedTile = pickedUpTile; // Get the tile clicked on
        playerShip.removeTileFromShip(pickedUpTile);
        playerShip.setDraggedTile(draggedTile); // Set intermediate tile to *remove from existing tiles*
    }

    /**
     * Checks if a tile is valid to pick up.
     * @param temp
     * @return
     */
    private boolean canGrabTile(ShipTile temp) {
        if(temp == null) return false;// Check if a tile was grabbed

        // Is player on the tile in question?
        boolean leftCornerOff = playerShip.returnTile(player.getX(),player.getY()) != temp;
        boolean rightCornerOff = playerShip.returnTile(player.getX() + player.getWidth(),player.getY()) != temp;

        return leftCornerOff && rightCornerOff;// Check if tile is same as tile that is stood on
    }

    public ShipTile setDraggedTileToNull(){
        ShipTile shipTile = draggedTile;
        draggedTile = null;
        return shipTile;
    }


    /**
     * Handles placing a dragged tile.
     * Expects to be used within SimpleTouch context, utilizing a class-scoped "dragged" ShipTile variable
     *
     * @param playerShip - The ship the tile can be added to
     * @param mousePosition - the unprojected mouse position
     */
    private void handlePlacingDragged(Ship playerShip, Vector3 mousePosition) {

        Vector2 mousePosition2 = new Vector2(mousePosition.x, mousePosition.y);

        playerShip.addTileToShip(mousePosition2.x, mousePosition.y, draggedTile.getID());

        // Dispose of used dragged tile references
        playerShip.setDraggedTile(null);
        draggedTile = null; // Dispose of dragged tile
    }
}
