package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(playerShip.isCollectingTiles()){
            // Fix screen position to camera position
//            Vector3 unprojectedV3 = new Vector3(screenX, screenY, 0);
//            camera.unproject(unprojectedV3);

            ShipTile tile = playerShip.returnTile(screenX, screenY);
            if(tile != null ){ // Check if on ship
                // Draw placeholder art on tile
                playerShip.setHoverShouldDraw(true);
                Vector2 vector2 = playerShip.getGridAlignedPosition(screenX, screenY);
                playerShip.setHoverIndicator(vector2.x, vector2.y);
            } else {
                playerShip.setHoverShouldDraw(false);
            }
        }
        return false;
    }
}
