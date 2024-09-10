package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.StrongTile;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.util.TileInit;

/**
 * New class to render main menu
 */
public class MainMenu  extends ScreenAdapter implements Screen {

    private Ship ship;
    private final Box2DWrapper box2DWrapper = new Box2DWrapper();

    public MainMenu() {

        initShipMenu();
    }

    private void initShipMenu(){
        this.ship = new Ship(new Vector2(TileInit.ORIGIN_X, TileInit.ORIGIN_Y), box2DWrapper);

        // We want a clean menu with several options to start the game.
        // We want to add in several tiles gapped by 2 spaces that change the game state when attaching the block to it.
        // The tiles can have labels above them. Polish would be making some physics letters that move out of the way and snap back after tile
        // moves away.
        ship.getTileManager().addTileNoSnap(0,0, new StrongTile(new Vector2(0,0)));
        ship.getTileManager().addTileNoSnap(125,0, new StrongTile(new Vector2(0,0)));
        ship.getTileManager().addTileNoSnap(-125,0, new StrongTile(new Vector2(0,0)));

    }


}
