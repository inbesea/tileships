package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.bitbucket.noahcrosby.AppPreferences;
import org.bitbucket.noahcrosby.directors.ShipDirector;
import org.bitbucket.noahcrosby.shapes.Line;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GoalChecker;
import org.bitbucket.noahcrosby.shipGame.levelData.ForegroundObject;
import org.bitbucket.noahcrosby.shipGame.levelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.levelData.Maps;
import org.bitbucket.noahcrosby.shipGame.MainGameHUD;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.background.GalaxyBackGround;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.input.*;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;
import org.bitbucket.noahcrosby.shipGame.managers.TextBoxManager;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.ClassicCollisionHandler;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.CollisionListener;
import org.bitbucket.noahcrosby.shipGame.player.PlayerInput;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.util.TileInit;

import java.util.ArrayList;

/**
 * Main game screen - where the game happens
 */
public class GameScreen implements Screen, Listener<MapNode> {
    final TileShipGame game;
    final AsteroidManager asteroidManager;
    private final MainGameHUD hud;
    ExtendViewport extendViewport;
    private final Player player;

    // Represents each side's size
    public static final Vector2 playerSize = new Vector2(ShipTile.TILE_SIZE * .33f, ShipTile.TILE_SIZE * .45f);

    static OrthographicCamera camera;
    private final Ship playerShip;
    private TileDragHandler tileDragHandler;
    private MapInputHandler gameScreenMapInputHandler;
    public static final float spawnAreaMax = TileShipGame.convertPixelsToMeters(300);
    private final ClassicCollisionHandler collisionHandler;

    TextBoxManager textBoxHandler;

    InputPreProcessor input;
    TileCollectHandler tileCollectHandler;
    DebugInputHandler debugInputHandler;
    ZoomHandler zoomHandler;

    Box2DWrapper box2DWrapper;
    private boolean updateLocalMapLocation = true;
    private MapNavManager mapNavigator;
    private MapNavigationHandler mapInputNavigator;
    GalaxyBackGround backGround;
    GoalChecker goalChecker;

    public GameScreen(final TileShipGame game) {
        this.game = game;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);
        backGround = new GalaxyBackGround((int) TileShipGame.defaultViewportSizeX, (int) TileShipGame.defaultViewportSizeY, 0.001f);

        box2DWrapper = new Box2DWrapper(new Vector2(0, 0), true);

        // init ship
        playerShip = new ShipDirector().buildClassicShip(box2DWrapper, new Vector2(TileInit.ORIGIN_X, TileInit.ORIGIN_Y));
        game.setPlayerShip(playerShip);
        goalChecker = new GoalChecker();
        playerShip.publisher.add(goalChecker);

        // init player
        // Give player the game reference
        player = new Player(new Vector2(playerShip.getX(), playerShip.getY()), playerSize, ID.Player, camera, this.game);

        asteroidManager = new AsteroidManager(box2DWrapper, camera);
        mapNavigator = new MapNavManager();
        mapNavigator.getPublisher().add(this);// Get game screen to watch the new nodes.
        mapNavigator.addMap(Maps.getBasicSpacemap(20));// Temp for testing
        hud = new MainGameHUD(game, mapNavigator);

        // Add input event handling
        initializeInputEventHandling();

        // Create collision listener
        collisionHandler = new ClassicCollisionHandler(asteroidManager, playerShip.getTileManager());// Handler has manager to manage stuff
        CollisionListener collisionListener = new CollisionListener(collisionHandler);// Listener can give collisions to collision handler
        box2DWrapper.setWorldContactListener(collisionListener); // Get the world in contact with this collision listener

        textBoxHandler = new TextBoxManager();

        backGround.addForegroundObject(new ForegroundObject(new Vector2(20, 20), new Vector2(300, 300), Resources.AsteroidRedTexture));
    }

    /**
     * Convenience method to initialize input event handling when starting game.
     */
    private void initializeInputEventHandling() {
        AppPreferences.initLogging(); // Start debug logging

        input = new InputPreProcessor(camera);
        input.addProcessor(tileCollectHandler = new TileCollectHandler(playerShip));
        tileDragHandler = new TileDragHandler(player);
        mapInputNavigator = new MapNavigationHandler(mapNavigator, (OrthographicCamera) hud.getCamera());
        input.addProcessor(tileDragHandler);
        input.addProcessor(debugInputHandler = new DebugInputHandler(game, this.playerShip, this.tileDragHandler));
        input.addProcessor(zoomHandler = new ZoomHandler(camera));
        input.addProcessor(gameScreenMapInputHandler = new MapInputHandler(this));
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void show() {
        Resources.MainMenuExtendedMessingaroundMusic.play();
        Resources.MainMenuExtendedMessingaroundMusic.setVolume(game.getPreferences().getMusicVolume());
        Resources.MainMenuExtendedMessingaroundMusic.setLooping(true);
    }

    private void checkForSpeechBubble() {

    }

    /**
     * Draw the game
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.00f, 0.00f, 0.10f, 1));

        // Sets focus to drawn on extended viewport
        extendViewport.apply();

        // Update game object positions
//        box2DWrapper.updateGameObjectsToPhysicsSimulation();
        asteroidManager.checkForSpawn(); // Handle the asteroid spawning

        backGround.draw(game);

        // Draw game objects
        drawGameObjects();

        // Draws the hud
        hud.draw();

        if (updateLocalMapLocation) {
            // process user input
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                PlayerInput.handleKeyPressed(player, camera);
            }
            PlayerInput.updateCameraOnPlayer(player, camera);
        }
        if(goalChecker.getWon()) {
            goalChecker.renderWin();
        }
    }

    /**
     * Draws all game objects and calls their render() methods
     */
    private void drawGameObjects() {
        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        TileShipGame.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        TileShipGame.batch.begin();

        playerShip.render(game);

        if (playerShip.isCollectingTiles() && playerShip.isHoverDrawing()) {
            drawGameObject(playerShip);
            drawGameObject(playerShip.getTileHoverIndicator());
        }
        drawGameObject(player);// Draw last to be on top of robot
        // Draw hud at this step

        textBoxHandler.render();

        TileShipGame.batch.end();

        drawGameObjects(asteroidManager.getAllAsteroids());

        if (AppPreferences.getAppPreferences().getIsDebug()) { // This sucks because we're having these calls done outside the object. These
            // should be called where we can generalize the object behavior.
            box2DWrapper.drawDebug(camera);
            playerShip.drawDraggingPlacementIndicator();
            Circle spawnLimit = asteroidManager.getAsteroidSpawnZone();
            Line.drawHollowCircle(new Vector2(spawnLimit.x, spawnLimit.y), spawnLimit.radius, 0.5f, Color.WHITE, camera.combined);
        }

        if (updateLocalMapLocation) {
            box2DWrapper.stepPhysicsSimulation(Gdx.graphics.getDeltaTime());

            // Call collision handling first and then sweep as objects are marked during this step lol
            collisionHandler.handleCollisions();
            box2DWrapper.sweepForDeadBodies();
        }
    }

    /**
     * Draws specific array[] of type-identical game objects
     *
     * @param gameObjects
     */
    private void drawGameObjects(ArrayList<? extends GameObject> gameObjects) {
        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        TileShipGame.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        TileShipGame.batch.begin();

        for (GameObject go : gameObjects) {

            // This render should happen after a sweep attempt
            if (go.isDead()) {
                Gdx.app.error("drawGameObjects(args)", "ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
                continue;
            }

            drawGameObject(go); // Call helper to draw object
        }

        TileShipGame.batch.end();
    }

    /**
     * Draws specific array[] of type-identical game objects
     *
     * @param gameObjects
     */
    private void drawGameObjects(Array<? extends GameObject> gameObjects) {
        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        TileShipGame.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        TileShipGame.batch.begin();

        for (GameObject go : gameObjects) {

            // This render should happen after a sweep attempt
            if (go.isDead()) {
                Gdx.app.error("drawGameObjects", "ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
                continue;
            }

            drawGameObject(go); // Call helper to draw object
        }

        TileShipGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {

        extendViewport.update(width, height);
        hud.update(width, height);
        backGround.update(width, height);
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
        Resources.MainMenuExtendedMessingaroundMusic.pause();
    }

    @Override
    public void resume() {
        System.out.println("Resuming GameScreen");
        Gdx.input.setInputProcessor(input);
        Resources.MainMenuExtendedMessingaroundMusic.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

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

    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Returns the current player ship
     *
     * @return - ship object assigned to the player
     */
    public Ship getPlayerShip() {
        return playerShip;
    }


    public Player getPlayer() {
        return player;
    }

    public ExtendViewport getExtendViewport() {
        return extendViewport;
    }

    public TileShipGame getGame() {
        return game;
    }

    public void quitGame() {
        Gdx.app.exit();
    }

    public TileDragHandler getTileDragHandler() {
        return tileDragHandler;
    }

    /**
     * Toggles the in-game menu
     */
    public void toggleInGameMenu() {
        this.hud.toggleMenu();
    }

    public void setFocusToGame() {
        this.initializeInputEventHandling();
    }

    public static OrthographicCamera getGameCamera() {
        return camera;
    }

    /**
     * Toggles the map on or off in the hud
     */
    public void toggleMap() {
        // If the game is showing the map we want to pause the game updates.
        boolean showingMap = this.hud.toggleMap();
        this.setMainScreenUpdates(!showingMap);// Pause the game if map showing
        if (showingMap) { // Map is now showing...
            input.addProcessor(this.mapInputNavigator);

        } else {
            input.removeProcessor(this.mapInputNavigator);
        }
    }

    /**
     * Set the GameScreen to stop updating the current location, and disconnects local input handlers
     * This includes the ship, asteroids, etc.
     *
     * @param updateLocation - Whether to pause the game
     */
    private void setMainScreenUpdates(boolean updateLocation) {
        this.updateLocalMapLocation = updateLocation;
        if (!updateLocation) {
            input.removeProcessor(zoomHandler);
            input.removeProcessor(debugInputHandler);
            input.removeProcessor(tileDragHandler);
            input.removeProcessor(tileCollectHandler);
            Gdx.input.setInputProcessor(input);
        } else {
            input.addProcessor(zoomHandler);
            input.addProcessor(debugInputHandler);
            input.addProcessor(tileDragHandler);
            input.addProcessor(tileCollectHandler);
            Gdx.input.setInputProcessor(input);
        }
    }

    public boolean showingMap() {
        return hud.showingMap();
    }

    /**
     * Receives new nodes from the MapNavManger
     * NOTE : This could be expanded with new implementations of the signal,
     * or other implemented listeners.
     * Thinking the signal could have some way to affect the logic
     *
     * @param signal  The Signal that triggered event
     * @param newNode The object passed on dispatch
     */
    @Override // Listener for the MapNavManager switching nodes.
    public void receive(Signal<MapNode> signal, MapNode newNode) {
        Gdx.app.log("Received Node", "GameScreen has received a new node " + newNode.getPositionAsString());
        transitionNodes(newNode, mapNavigator.getPreviousNode());
//        backGround.updateForeground(newNode.getForeground());
    }

    /**
     * Moves the game screen between two nodes.
     * Does not set the new node or anything
     * Might be difficult to edit in the future, but will work for us now.
     *
     * @param nn
     * @param pn
     */
    private void transitionNodes(MapNode nn, MapNode pn) {
        if (pn != null) {
            pn.setAsteroids(asteroidManager.getFiniteAsteroids());
        }
        // VERY TODO : Get the spawners working so we can get a set of finite asteroids here lol
        asteroidManager.setFiniteAsteroids(nn.returnAsteroids() /* Empties the asteroids from the new node, to be reset later when switching nodes. */);
        asteroidManager.setSpawner(nn.getAsteroidSpawner()); // Need this to keep up basic spawning situations.
    }
}
