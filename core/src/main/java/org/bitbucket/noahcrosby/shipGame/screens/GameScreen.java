package org.bitbucket.noahcrosby.shipGame.screens;

import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.HUD;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.input.*;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.managers.TextBoxManager;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.CollisionHandler;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.CollisionListener;
import org.bitbucket.noahcrosby.shipGame.player.PlayerInput;
import org.bitbucket.noahcrosby.shipGame.player.SimpleTouch;
import org.bitbucket.noahcrosby.shipGame.util.SoundTextBubble;
import space.earlygrey.shapedrawer.ShapeDrawer;
import org.bitbucket.noahcrosby.javapoet.Resources;

import java.util.ArrayList;

/**
 * Main game screen - where the game happens
 */
public class GameScreen implements Screen {
    final TileShipGame game;
    final AsteroidManager asteroidManager;
    private final HUD hud;
    ExtendViewport extendViewport;
    private final Player player;

    // Represents each side's size
    public static final Vector2 playerSize = new Vector2(ShipTile.TILE_SIZE * .33f, ShipTile.TILE_SIZE * .45f);

    static OrthographicCamera camera;
    SimpleTouch st;
    private final Array<GameObject> gameObjects;
    private final Ship playerShip;
    private TileDragHandler tileDragHandler;
    public static final float spawnAreaMax = TileShipGame.convertPixelsToMeters(300);
    private final CollisionHandler collisionHandler;

    TextBoxManager textBoxHandler;

    InputPreProcessor input;
    TileCollectHandler tileCollectHandler;
    DebugInputHandler debugInputHandler;
    ZoomHandler zoomHandler;

    Box2DWrapper box2DWrapper;


    public GameScreen(final TileShipGame game) {
        this.game = game;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);

        box2DWrapper = new Box2DWrapper(new Vector2(0, 0), true);

        // init ship
        playerShip = new Ship(new Vector2(-1, -1), box2DWrapper);
        game.setPlayerShip(playerShip);
        playerShip.initialize();

        // init player
        // Give player the game reference
        player = new Player(new Vector2(playerShip.getX(), playerShip.getY()), playerSize, ID.Player, camera, this.game);

        // Place init game objects into the array
        gameObjects = new Array<>();
        gameObjects.add(player);
        gameObjects.add(playerShip);

        // Add input event handling
        initializeInputEventHandling();

        asteroidManager = new AsteroidManager(box2DWrapper ,camera);
        hud = new HUD(game);

        // Create collision listener
        collisionHandler = new CollisionHandler(asteroidManager);// Handler has manager to manage stuff
        CollisionListener collisionListener = new CollisionListener(collisionHandler);// Listener can give collisions to collision handler
        box2DWrapper.setWorldContactListener(collisionListener); // Get the world in contact with this collision listener

        textBoxHandler = new TextBoxManager();
    }

    private void initializeInputEventHandling() {
        input = new InputPreProcessor(camera);
        input.addProcessor(tileCollectHandler = new TileCollectHandler(playerShip));
        tileDragHandler = new TileDragHandler(player);
        input.addProcessor(tileDragHandler);
        input.addProcessor(debugInputHandler = new DebugInputHandler(this));
        input.addProcessor(zoomHandler = new ZoomHandler(camera));
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
        box2DWrapper.updateGameObjectsToPhysicsSimulation();

        // Draw game objects
        drawGameObjects();

        // Draws the HUD
        hud.draw();

        // process user input
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            PlayerInput.handleKeyPressed(player, camera);
        }
        PlayerInput.updateCameraOnPlayer(player, camera);
        asteroidManager.checkForSpawn(); // Handle the asteroid spawning
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
        GameObject go;

        for (int i = 0; i < gameObjects.size; i++) {
            go = gameObjects.get(i);

            // This render should happen after a sweep attempt
            if (go.isDead()) {
                System.out.println("ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
                continue;
            }

            drawGameObject(go); // Call helper to draw object
        }

        if (playerShip.isCollectingTiles() && playerShip.isHoverDrawing()) {
            drawGameObject(playerShip.getTileHoverIndicator());
            drawGameObject(playerShip);
        } else if (playerShip.isDragging()) {
            playerShip.drawDraggingPlacementIndicator();
        }
        drawGameObject(player);// Draw last to be on top of robot
        // Draw hud at this step

        textBoxHandler.render();

        TileShipGame.batch.end();

        drawGameObjects(asteroidManager.getAsteroids());

        if(AppPreferences.getAppPreferences().getIsDebug()){
            box2DWrapper.drawDebug(camera);
        }

        box2DWrapper.stepPhysicsSimulation(Gdx.graphics.getDeltaTime());

        // Call collision handling first and then sweep as objects are marked during this step lol
        collisionHandler.handleCollisions();
        box2DWrapper.sweepForDeadBodies();
    }

    /**
     * Draws specific array[] of type-identical game objects
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
                System.out.println("ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
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
     * @return - Ship object assigned to the player
     */
    public Ship getPlayerShip() {
        return playerShip;
    }


    public Player getPlayer() {
        return player;
    }

    /**
     * Add new game object to game.
     * New object will be renderered based on position and its own render method.
     *
     * @param go the game object to add
     */
    public void newGameObject(GameObject go) {
        gameObjects.add(go);
    }

    /**
     * Finds the game object, removes and returns it from the game screen Array
     *
     * @param gameObject the game object to remove
     * @return the removed game object
     */
    public GameObject removeGameObject(GameObject gameObject) {
        int i = gameObjects.indexOf(gameObject, true); // Get index of gameObject

        if (i < 0) {
            throw new RuntimeException("gameObject not found in GameScreen existing game objects - number of objects... : " + gameObjects.size +
                    " location of gameObject " + gameObject.getX() + ", " + gameObject.getY() + " GameObject ID : " + gameObject.getID());
        }
        return gameObjects.removeIndex(i); // Returns the object reference and removes it.
    }

    public ExtendViewport getExtendViewport() {
        return extendViewport;
    }

    public TileShipGame getGame() {
        return game;
    }


    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * removes an asteroid instance from the asteroid manager
     *
     * @param asteroid the asteroid to remove
     */
    public void removeAsteroid(GameObject asteroid) {
        asteroidManager.deleteMember(asteroid);
    }

    /**
     * Update logic for mouseMoved to make sure it's updated
     */
    public void updateMouseMoved() {
        st.mouseMoved(Gdx.input.getX(), Gdx.input.getY());
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
    public void setFocusToGame(){
        this.initializeInputEventHandling();
    }

    public static OrthographicCamera getGameCamera(){
        return camera;
    }
}
