package com.shipGame.ncrosby.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shipGame.ncrosby.generalObjects.Player;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;

import java.util.Stack;

import static com.shipGame.ncrosby.util.generalUtil.returnUnprojectedPosition;

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
        }

    /**
     * Method to handle mouse movement
     *
     * @param screenX - x position on screen
     * @param screenY - y position on screen
     * @return - boolean
     */
    @Override public boolean mouseMoved (int screenX, int screenY) {
            if(playerShip.isCollectingTiles() && playerShip.getCollapseCollect().empty()){

                // Fix screen position to camera position
                Vector3 unprojectedV3 = new Vector3(screenX, screenY, 0);
                camera.unproject(unprojectedV3);

                if(!playerShip.isPositionOffShip(new Vector2(unprojectedV3.x, unprojectedV3.y)) ){ // Check if on ship
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
        System.out.println(amountX + " " + amountY);
        camera.zoom += ((amountY * tileShipGame.zoomSpeed) * Gdx.graphics.getDeltaTime());
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;

        // Set init values
        camera.unproject(tp.set(screenX, screenY, 0));
        setIsDragging(true);
        Vector3 v = returnUnprojectedPosition(camera);

        if (playerShip.isCollectingTiles()){
            ShipTile collectTile = playerShip.returnTile(v.x, v.y);
            playerShip.addTileToCollapseCollection(collectTile); // Stack should be empty
        } else {
            // Get a tile and check if it can be picked up.
            ShipTile pickedUpTile = playerShip.returnTile(v.x, v.y);
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
                // is
//                if(playerShip.returnTile(tp.x, tp.y) )
                // After get a tile we can check if the stack is complete or not.
                // if it is then we can turn off collecting tiles.
            } else if (draggedTile != null){// Dragging a tile
                // Get mouse location
                Vector3 mouseLocation = new Vector3();
                mouseLocation.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mouseLocation);

                // Drag the tile with mouse
                draggedTile.setX(mouseLocation.x - ShipTile.TILESIZE/2.0f);
                draggedTile.setY(mouseLocation.y - ShipTile.TILESIZE/2.0f);
            } else {

            }

            return true;
        }

        @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            Vector3 mousePosition = returnUnprojectedPosition(camera);

            // Need to handle letting go of the mouse to construct.

            if(draggedTile != null){ // If there is a tile being dragged
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
                playerShip.addTile(draggedTile.getX(), draggedTile.getY(), draggedTile.getID());
                setDraggedTileToNull();
                setIsDragging(false);
            }
            playerShip.startCollapseCollect(); // Begins ship collecting
        }
            return false;
        }

        @Override public boolean keyUp (int keycode) {

            if(playerShip.isCollectingTiles() && keycode == 59){
                Stack<ShipTile> shipTileStack =
                        playerShip.finishCollapseCollect(); // Ends collecting
                if(shipTileStack.empty()){
                    System.out.println("Tiles collected : None");
                }else {
                    System.out.println("Tiles collected : " + shipTileStack.stream().toString());
                }
            }
            return false;
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
     * @param pickedUpTile
     */
    private void pickUpTile(ShipTile pickedUpTile) {
        draggedTile = pickedUpTile; // Get the tile clicked on
        playerShip.removeTileFromShip(pickedUpTile);
        playerShip.setDragged(draggedTile); // Set intermediate tile to *remove from existing tiles*
    }

    private boolean canGrabTile(ShipTile temp) {
        if(temp == null) return false;// Check if a tile was grabbed

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

        playerShip.addTile(mousePosition2.x, mousePosition.y, draggedTile.getID());

        // Dispose of used dragged tile references
        playerShip.setDragged(null);
        draggedTile = null; // Dispose of dragged tile
    }
}
