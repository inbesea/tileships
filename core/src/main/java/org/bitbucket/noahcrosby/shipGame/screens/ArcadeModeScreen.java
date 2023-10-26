package org.bitbucket.noahcrosby.shipGame.screens;

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
import org.bitbucket.noahcrosby.Directors.ShipDirector;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.HUD;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.input.*;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.ArcadeCollisionHandler;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.CollisionListener;
import org.bitbucket.noahcrosby.shipGame.player.PlayerInput;

import java.util.ArrayList;

public class ArcadeModeScreen extends ScreenAdapter  implements Screen {
    public static int tetrisSize = 6;
    // Varibles here
    private Ship arcadeShip;
    TileCollectHandler tileCollectHandler;
    private TileDragHandler tileDragHandler;
    ZoomHandler zoomHandler;
    DebugInputHandler debugInputHandler;
    private Player player;
    TileShipGame game;
    ExtendViewport extendViewport;
    static OrthographicCamera camera;
    private final HUD hud;
    InputPreProcessor input;
    Box2DWrapper box2DWrapper;

    final AsteroidManager asteroidManager;
    private final Array<GameObject> arcadeGameObjects; // Used to render the objects?

    ArcadeCollisionHandler collisionHandler;

    // Constructor
    public ArcadeModeScreen(final TileShipGame game) {
        this.game = game;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);

        // Create HUD
        hud = new HUD(game);

        // Create Box2D
        box2DWrapper = new Box2DWrapper(new Vector2(0, 0), true);


        arcadeShip = new ShipDirector().buildArcadeShip(box2DWrapper, new Vector2(0, 0));
        arcadeShip.getCollectionManager().setCOLLECTED_TILE_LIMIT(tetrisSize);
        asteroidManager = new AsteroidManager(box2DWrapper, camera);
        asteroidManager.setArcadeMode(true);

        player = new Player(new Vector2(arcadeShip.getX(), arcadeShip.getY()), GameScreen.playerSize, ID.Player, camera, this.game);
        player.setPlayerShip(arcadeShip);
        initializeInputEventHandling();

        arcadeGameObjects = new Array<>();

        collisionHandler = new ArcadeCollisionHandler(this);
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
        box2DWrapper.updateGameObjectsToPhysicsSimulation();
        asteroidManager.checkForSpawn(); // Handle the asteroid spawning

        drawGame();

        // Don't forget to step the simulation
        box2DWrapper.stepPhysicsSimulation(Gdx.graphics.getDeltaTime());
        collisionHandler.handleCollisions();
        box2DWrapper.sweepForDeadBodies();
        // process user input
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            PlayerInput.handleKeyPressed(player, camera);
        }
        PlayerInput.updateCameraOnPlayer(player, camera);
    }

    private void drawGame() {
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        TileShipGame.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        TileShipGame.batch.begin();
        drawGameObject(arcadeShip);
        player.render(game);
        TileShipGame.batch.end();

        drawGameObjects(asteroidManager.getAsteroids());

        if(AppPreferences.getAppPreferences().getIsDebug()){
            box2DWrapper.drawDebug(camera);
        }

    }

    private void initializeInputEventHandling() {
        input = new InputPreProcessor(camera);
        input.addProcessor(tileCollectHandler = new TileCollectHandler(arcadeShip));
        tileDragHandler = new TileDragHandler(player);
        input.addProcessor(tileDragHandler);
        input.addProcessor(debugInputHandler = new DebugInputHandler(game, this.arcadeShip, this.tileDragHandler));
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
                System.out.println("ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
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
