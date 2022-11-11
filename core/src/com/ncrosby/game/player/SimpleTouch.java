package com.ncrosby.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ncrosby.game.generalObjects.Player;
import com.ncrosby.game.generalObjects.Ship;
import com.ncrosby.game.tiles.ShipTile;
import com.ncrosby.game.screens.GameScreen;

public class SimpleTouch implements InputProcessor {

        OrthographicCamera camera;
        Ship playerShip;
        Player player;
        Vector3 tp = new Vector3();
        boolean dragging;
        private ShipTile tempTile;

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
        //clickPlayerShipTiles(camera, playerShip, player);

        Vector3 v = new Vector3();
        v.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(v);
        ShipTile temp = playerShip.returnTile(v.x, v.y);
        if(playerShip.returnTile(player.getX(),player.getY()) != temp){// Check if tile is same as tile that is stood on
            tempTile = temp; // Get the tile clicked on
        }
        return true;
        }

        @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
            if (!dragging) return false;
            camera.unproject(tp.set(screenX, screenY, 0));

            if(tempTile != null){
                Vector3 v = new Vector3();
                v.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(v);
                tempTile.setX(v.x - ShipTile.TILESIZE/2.0f);
                tempTile.setY(v.y - ShipTile.TILESIZE/2.0f);
            }

            return true;
        }

        @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            Vector3 v = new Vector3();
            v.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(v);

            if(tempTile != null){
                playerShip.removeTile(tempTile);
                // Will place the tile at an empty location and add it to the ship. Should snap into place.
                if(playerShip.addTileByCoord(v.x, v.y, tempTile.getID()) != null){ // If found a tile already in place

                }
                // TODO : Handle spaces not adjacent to the ship, or spaces occupied by the shiptiles

                tempTile = null;
            }

            dragging = false;
            return true;
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
