package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;

public class TileCollectHandler extends InputAdapter {

    private final Ship playerShip;

    public TileCollectHandler(Ship playerShip) {
        this.playerShip = playerShip;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (playerShip.isCollectingTiles() && button == Input.Buttons.RIGHT) {
            playerShip.cancelCurrentCollectArray(); // Cancel collect action
        }
        return false;
    }
}