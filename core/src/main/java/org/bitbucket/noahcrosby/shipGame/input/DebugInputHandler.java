package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class DebugInputHandler extends InputAdapter {

    private final Ship playerShip;
    private final TileDragHandler tileDragHandler;
    private TileShipGame game;

    public DebugInputHandler(TileShipGame game ,Ship playerShip, TileDragHandler tileDragHandler) {
        this.game = game;
        this.playerShip = playerShip;
        this.tileDragHandler = tileDragHandler;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.debug("DebugInputHandler","Keycode pressed is : " + keycode);

        // Begin collecting tiles for collapse
        if (!playerShip.isCollectingTiles() && keycode == Input.Keys.SHIFT_LEFT) {
            ShipTile draggedTile = playerShip.getDraggedTile();
            if (draggedTile != null) { // If holding tile
                playerShip.addTileToShip(draggedTile.getX(), draggedTile.getY(), draggedTile.getID());
                playerShip.setDraggedTile(null);
                tileDragHandler.setDragging(false);
            }
            playerShip.startCollapseCollect(); // Begins ship collecting
        } else if (keycode == Input.Keys.ESCAPE) {
            game.changeScreen(TileShipGame.MENU);
            // TODO : Lets change this and the .pause() on screens to pause the game. We can clean this up to bring up an ingame menu very easily.
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (playerShip.isCollectingTiles() &&
            keycode == Input.Keys.SHIFT_LEFT) { // If user keys up should
            attemptNewTileProduction();
        }
        return false;
    }

    /**
     * Resolves the collect tiles action, and then attempts to build a new tile by passing to the tile Condenser
     */
    private void attemptNewTileProduction() {
        Array<ShipTile> shipTileArray = playerShip.finishCollapseCollect(); // Ends collecting
        if (shipTileArray.isEmpty()) {
            System.out.println("Tiles collected : None");
        } else {
            Gdx.app.log("attemptNewTileProduction","Tiles collected : " + TileTypeData.generateTileDescriptionString(shipTileArray) + " Size : " + shipTileArray.size);
            playerShip.buildNewTile(shipTileArray);
        }
    }
}
