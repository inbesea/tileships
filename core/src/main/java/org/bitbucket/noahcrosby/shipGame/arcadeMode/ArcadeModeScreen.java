package org.bitbucket.noahcrosby.shipGame.arcadeMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.bitbucket.noahcrosby.AppPreferences;
import org.bitbucket.noahcrosby.directors.ShipDirector;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.hud.HUD;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.input.*;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.ArcadeCollisionHandler;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.CollisionListener;
import org.bitbucket.noahcrosby.shipGame.player.PlayerInput;
import org.bitbucket.noahcrosby.shipGame.screens.GameScreen;

import java.util.ArrayList;

public class ArcadeModeScreen extends ScreenAdapter  implements Screen {
    public static int tetrisSize = 6;
    // Variables here
    private Ship arcadeShip;
    TileCollectHandler tileCollectHandler;
    ZoomHandler zoomHandler;
    DebugInputHandler debugInputHandler;
    PlayerInput playerInput;
    private Player player;
    TileShipGame game;
    ExtendViewport extendViewport;
    static OrthographicCamera camera;
    private final HUD hud;
    InputPreProcessor input;
    Box2DWrapper box2DWrapper;

    final AsteroidManager asteroidManager;

    ArcadeCollisionHandler collisionHandler;

    // Constructor
    public ArcadeModeScreen(final TileShipGame game) {
        this.game = game;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);

        // Create hud
        hud = new HUD(game);

        // Create default Box2D
        box2DWrapper = new Box2DWrapper();


        arcadeShip = new ShipDirector().buildArcadeShip(box2DWrapper, new Vector2(0, 0));
        arcadeShip.getCollectionManager().setCOLLECTED_TILE_LIMIT(tetrisSize);
        asteroidManager = new AsteroidManager(box2DWrapper, camera);
        asteroidManager.setArcadeMode(true);

        player = new Player(new Vector2(arcadeShip.getX(), arcadeShip.getY()), GameScreen.playerSize, ID.Player, this.game);
        player.setPlayerShip(arcadeShip);
        initializeInputEventHandling();

        // Used to render the objects?
        Array<GameObject> arcadeGameObjects = new Array<>();

        collisionHandler = new ArcadeCollisionHandler(this, arcadeShip.getTileManager());
        CollisionListener collisionListener = new CollisionListener(collisionHandler);
        box2DWrapper.setWorldContactListener(collisionListener);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.00f, 0.00f, 0.10f, 1));
        camera.update();

        // Sets focus to drawn on extended viewport
        extendViewport.apply();

        // Update game object positions
//        box2DWrapper.updateGameObjectsToPhysicsSimulation();
        asteroidManager.update(Gdx.graphics.getDeltaTime()); // Handle the asteroid spawning

        drawGame();

        // Don't forget to step the simulation
        box2DWrapper.stepPhysicsSimulation(Gdx.graphics.getDeltaTime());
        collisionHandler.handleCollisions();
        box2DWrapper.sweepForDeadBodies();
        // process user input
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            playerInput.handleKeyPressed(player, camera);
        }
        playerInput.updateCameraOnPlayer(player, camera);
    }

    private void drawGame() {
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        TileShipGame.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        TileShipGame.batch.begin();
        drawGameObject(arcadeShip);
        player.render(game);
        TileShipGame.batch.end();

        drawGameObjects(asteroidManager.getTransientAsteroids());

        if(AppPreferences.getAppPreferences().getIsDebug()){
            box2DWrapper.drawDebug(camera);
        }

    }

    private void initializeInputEventHandling() {
        input = new InputPreProcessor(camera);
        input.addProcessor(tileCollectHandler = new TileCollectHandler(arcadeShip));
        TileDragHandler tileDragHandler = new TileDragHandler(arcadeShip);
        input.addProcessor(tileDragHandler);
        input.addProcessor(debugInputHandler = new DebugInputHandler(game, this.arcadeShip, tileDragHandler));
        input.addProcessor(playerInput = new PlayerInput());
        input.addProcessor(zoomHandler = new ZoomHandler(camera));
        Gdx.input.setInputProcessor(input);
    }

    /**
     * Draws specific array[] of type-identical game objects
     * @param gameObjects
     */
    private void drawGameObjects(ArrayList<? extends GameObject> gameObjects) {
        // tell the camera to update its matrices.
        camera.update();

        game.batch.begin();
        for (GameObject go : gameObjects) {

            // This render should happen after a sweep attempt
            if (go.isDead()) {
                Gdx.app.error("SweepError","ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
                continue;
            }

            if (go.getTexture() != null) {
                Vector2 size = go.getSize();
                TileShipGame.batch.draw(go.getTexture(), go.getX(), go.getY(), size.x, size.y);
            }
            go.render(this.game);
        }
        game.batch.end();
    }

    /**
     * Draws specific array[] of type-identical game objects
     * @param gameObjects
     */
    private void drawGameObjects(Array<? extends GameObject> gameObjects) {
        // tell the camera to update its matrices.
        camera.update();

        game.batch.begin();
        for (GameObject go : gameObjects) {

            // This render should happen after a sweep attempt
            if (go.isDead()) {
                Gdx.app.error("SweepError","ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
                continue;
            }

            if (go.getTexture() != null) {
                Vector2 size = go.getSize();
                TileShipGame.batch.draw(go.getTexture(), go.getX(), go.getY(), size.x, size.y);
            }
            go.render(this.game);
        }
        game.batch.end();
    }

    /**
     * Helper method holds rendering logic, takes game object
     * Expects to be called after game.batch.begin() and with a batch that has the correct camera location context
     */
    private void drawGameObject(GameObject gameObject) {
        if (gameObject.getTexture() != null) {
            Vector2 size = gameObject.getSize();
            TileShipGame.batch.draw(gameObject.getTexture(), gameObject.getX(), gameObject.getY(), size.x, size.y);
        }
        gameObject.render(this.game);
    }

    // Methods
    @Override
    public void show() {
        Resources.MainMenuExtendedMessingaroundMusic.play();
        Resources.MainMenuExtendedMessingaroundMusic.setVolume(game.getPreferences().getMusicVolume());
        Resources.MainMenuExtendedMessingaroundMusic.setLooping(true);
    }

    @Override
    public void resize(int width, int height) {

        extendViewport.update(width, height);
        hud.update(width, height);
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
        Resources.MainMenuExtendedMessingaroundMusic.pause();
    }

    @Override
    public void resume() {
        System.out.println("Resuming " + this.getClass().getSimpleName());
        Gdx.input.setInputProcessor(input);
        Resources.MainMenuExtendedMessingaroundMusic.play();
    }

    public AsteroidManager getAsteroidManager() {
        return asteroidManager;
    }
}
