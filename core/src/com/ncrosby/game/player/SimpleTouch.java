package com.ncrosby.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ncrosby.game.generalObjects.Player;
import com.ncrosby.game.generalObjects.Ship;
import com.ncrosby.game.tiles.ShipTile;
import com.ncrosby.game.screens.GameScreen;

import static com.ncrosby.game.util.generalUtil.returnUnprojectedMousePosition;

/**
 * Main user input handling class.
 * Uses event handling to do things.
 */
public class SimpleTouch implements InputProcessor {

        OrthographicCamera camera;
        Ship playerShip;
        Player player;
        Vector3 tp = new Vector3();
        boolean dragging;
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

        @Override public boolean mouseMoved (int screenX, int screenY) {
            // we can also handle mouse movement without anything pressed
//		camera.unproject(tp.set(screenX, screenY, 0));
            return false;
        }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        dragging = true;
        //clickPlayerShipTiles(camera, playerShip, player); // Used to pickup tiles. Unused currently

        Vector3 v = returnUnprojectedMousePosition(camera);

        ShipTile temp = playerShip.returnTile(v.x, v.y);
        if(playerShip.returnTile(player.getX(),player.getY()) != temp){// Check if tile is same as tile that is stood on
            draggedTile = temp; // Get the tile clicked on
            playerShip.removeTileFromShip(temp);
            playerShip.setDragged(draggedTile); // Set intermediate tile to *remove from existing tiles*
        }
        return true;
        }

        @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
            if (!dragging) return false;
            camera.unproject(tp.set(screenX, screenY, 0));

            if(draggedTile != null){ // Dragging a tile
                // Get mouse location
                Vector3 mouseLocation = new Vector3();
                mouseLocation.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mouseLocation);

                // Drag the tile with mouse
                draggedTile.setX(mouseLocation.x - ShipTile.TILESIZE/2.0f);
                draggedTile.setY(mouseLocation.y - ShipTile.TILESIZE/2.0f);
            }

            return true;
        }

        @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            Vector3 mousePosition = returnUnprojectedMousePosition(camera);

            if(draggedTile != null){ // If there is a tile being dragged
                handlePlacingDragged(playerShip, mousePosition);
            }

            dragging = false;
            return true;
        }

    /**
     * Handles placing a dragged tile.
     * Expects to be used within SimpleTouch context, utilizing a class-scoped "dragged" ShipTile variable
     *
     * @param playerShip - The ship the tile can be added to
     * @param mousePosition - the unprojected mouse position
     */
    private void handlePlacingDragged(Ship playerShip, Vector3 mousePosition){

        Vector2 mousePosition2 = new Vector2(mousePosition.x, mousePosition.y);

        // TODO : Handle spaces not adjacent to the ship, or spaces occupied by the shiptiles
        ShipTile destinationTile = playerShip.returnTile(mousePosition2);
            if(destinationTile != null){ // Released on Shiptile
                Vector2 nearestEmptySpace = playerShip.closestVacancy(mousePosition2);
//                    playerShip.addTileByCoord();
            } else { // Released on empty space
                ShipTile closestTile = playerShip.closestTile(mousePosition2);
                playerShip.setTileOnClosestSide(draggedTile, closestTile, mousePosition);
            }

            // Dispose of used dragged tile references
            playerShip.setDragged(null);
            draggedTile = null; // Dispose of dragged tile
        }

        @Override public boolean keyDown (int keycode) {
            return false;
        }

        @Override public boolean keyUp (int keycode) {
            return false;
        }

        @Override public boolean keyTyped (char character) {
            return false;
        }

}
