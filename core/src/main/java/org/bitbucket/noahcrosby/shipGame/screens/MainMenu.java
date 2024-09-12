package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.StrongTile;
import org.bitbucket.noahcrosby.shipGame.input.InputPreProcessor;
import org.bitbucket.noahcrosby.shipGame.input.TileDragHandler;
import org.bitbucket.noahcrosby.shipGame.levelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.util.TileInit;

/**
 * New class to render main menu
 */
public class MainMenu  extends ScreenAdapter implements Screen {

    final TileShipGame game;
    final OrthographicCamera camera;
    ExtendViewport viewport;
    InputPreProcessor input;
    TileDragHandler dragHandler;
    Music mainMenuMusic;
    boolean afterLoadingMap = false;

    private Ship ship;
    private final Box2DWrapper box2DWrapper = new Box2DWrapper();

    private ShipTile startTile;

    public MainMenu(final TileShipGame game) {
        initShipMenu();

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        viewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);

//        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Resources.loadAssets();
        input = new InputPreProcessor(camera);
        dragHandler = new TileDragHandler(ship);
    }

    private void initShipMenu(){
        this.ship = new Ship(new Vector2(TileInit.ORIGIN_X, TileInit.ORIGIN_Y), box2DWrapper);

        // We want a clean menu with several options to start the game.
        // We want to add in several tiles gapped by 2 spaces that change the game state when attaching the block to it.
        // The tiles can have labels above them. Polish would be making some physics letters that move out of the way and snap back after tile
        // moves away.
        startTile = ship.getTileManager().addTileNoSnap(300,500, new StrongTile(new Vector2(0,0)));
        ship.getTileManager().addTileNoSnap(500,500, new StrongTile(new Vector2(0,0)));
        ship.getTileManager().addTileNoSnap(125,500, new StrongTile(new Vector2(0,0)));

        Gdx.input.setInputProcessor(input);
//        input.addProcessor(dragHandler);
    }

    /**
     * Renders out the data for the MainMenu
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.00f, 0.00f, 0.10f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        viewport.apply();
        camera.update();
        TileShipGame.batch.setProjectionMatrix(viewport.getCamera().combined);

        TileShipGame.batch.setProjectionMatrix(camera.combined);

        // Call to load textures to asset manager
        Resources.updateAssets();

        TileShipGame.batch.begin();

        boolean doneLoading = Resources.assetManager.update();
        if(doneLoading){

            TileShipGame.font.draw(TileShipGame.batch, "T\nI\nL\n" +
                "E\nS\nH\nI\nP\nS", 600, 350);

            ship.render(game);

            dragHandler.update();

            if(!afterLoadingMap){
                input.addProcessor(dragHandler);
                afterLoadingMap = true;
            }

        } else {
            TileShipGame.font.draw(TileShipGame.batch, "~~~Loading Assets " + Resources.assetManager.getProgress() + " ~~~", 0, camera.viewportHeight - 100);
        }
        TileShipGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        // Need to call it with file string so the remaining files can be loaded
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune.wav"));
        mainMenuMusic.play();
        mainMenuMusic.setVolume(game.getPreferences().getMusicVolume());
        mainMenuMusic.setLooping(true);

        Gdx.input.setInputProcessor(input);

        TileShipGame.setCurrentCamera(camera);
    }
}
