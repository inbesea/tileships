package com.ncrosby.game.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ncrosby.game.Player;
import com.ncrosby.game.Ship;
import com.ncrosby.game.screens.GameScreen;

import static com.ncrosby.game.PlayerInput.clickPlayerShipTiles;

public class SimpleTouch implements InputProcessor {

    OrthographicCamera camera;
    Ship playerShip;
    Player player;
        Vector3 tp = new Vector3();
        boolean dragging;

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
        clickPlayerShipTiles(camera, playerShip, player);

        return true;
        }

        @Override public boolean touchDragged (int screenX, int screenY, int pointer) {
            if (!dragging) return false;
            camera.unproject(tp.set(screenX, screenY, 0));
            return true;
        }

        @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            camera.unproject(tp.set(screenX, screenY, 0));
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
