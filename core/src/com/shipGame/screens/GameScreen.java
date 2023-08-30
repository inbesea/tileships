package com.shipGame.screens;

import com.Builders.TextBoxBuilder;
import com.Directors.TextBoxDirector;
import com.Shapes.Tentacle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.javapoet.Resources;
import com.shipGame.ID;
import com.shipGame.TileShipGame;
import com.shipGame.generalObjects.GameObject;
import com.shipGame.generalObjects.HUD;
import com.shipGame.generalObjects.Player;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.input.DebugInputHandler;
import com.shipGame.input.InputPreProcessor;
import com.shipGame.input.TileCollectHandler;
import com.shipGame.input.TileDragHandler;
import com.shipGame.input.ZoomHandler;
import com.shipGame.managers.AsteroidManager;
import com.shipGame.physics.box2d.Box2DWrapper;
import com.shipGame.physics.collisions.CollisionHandler;
import com.shipGame.physics.collisions.CollisionListener;
import com.shipGame.player.PlayerInput;
import com.shipGame.player.SimpleTouch;
import com.shipGame.util.SoundTextBubble;
import space.earlygrey.shapedrawer.ShapeDrawer;

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

    OrthographicCamera camera;
    SimpleTouch st;
    private final Array<GameObject> gameObjects;
    private final Ship playerShip;
    private TileDragHandler tileDragHandler;
    public static final float spawnAreaMax = TileShipGame.convertPixelsToMeters(300);
    Music gameScreenMusic;
    CircleShape circle = new CircleShape();
    private final CollisionHandler collisionHandler;

    Tentacle tentacle;
    public static ShapeDrawer shapeDrawer;
    public SoundTextBubble textBubble;
    public SoundTextBubble textBubble1;
    TextBoxDirector boxDirector;

    private Stage menuOverlay;

    public GameScreen(final TileShipGame game) {
        this.game = game;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);

        // init ship
        playerShip = new Ship(new Vector2(-1, -1));
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
        InputPreProcessor input = new InputPreProcessor(camera);
        input.addProcessor(new TileCollectHandler(playerShip));
        tileDragHandler = new TileDragHandler(player);
        input.addProcessor(tileDragHandler);
        input.addProcessor(new DebugInputHandler(this));
        input.addProcessor(new ZoomHandler(camera));
        Gdx.input.setInputProcessor(input);

        asteroidManager = new AsteroidManager(this);
        hud = new HUD(game);

        // Create collision listener
        collisionHandler = new CollisionHandler(asteroidManager);// Handler has manager to manage stuff
        CollisionListener collisionListener = new CollisionListener(collisionHandler);// Listener can give collisions to collision handler
        Box2DWrapper.getInstance().setWorldContactListener(collisionListener);

        shapeDrawer = new ShapeDrawer(TileShipGame.batch);
        shapeDrawer.setTextureRegion(new TextureRegion(Resources.ShipTileStrongTexture, 20, 20, 20, 20));

        boxDirector = new TextBoxDirector(new TextBoxBuilder());
        // TODO : Add back these textbubbles with the manager handling updates, and the director creating instances.
//        textBubble = new SoundTextBubble("Hello, this is a test message", 300, Resources.sfxCollectTileSound, player.getPosition());
//
//        textBubble1 = new SoundTextBubble("This is also a test message", 300, Resources.sfxCollectTileSound, player.getPosition());
    }

    @Override
    public void show() {
        Resources.MainMenuExtendedMessingaroundMusic.play();
        Resources.MainMenuExtendedMessingaroundMusic.setVolume(game.getPreferences().getMusicVolume());
        Resources.MainMenuExtendedMessingaroundMusic.setLooping(true);
    }

    /**
     * Draw the game
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.00f, 0.00f, 0.10f, 1));

        extendViewport.apply();

        // Update game object positions
        Box2DWrapper.getInstance().updateGameObjectsToPhysicsSimulation();

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
            if (go.isDead()) throw new RuntimeException("Game Object was not swept " + go.getID().toString());

            drawGameObject(go); // Call helper to draw object
        }

        if (playerShip.isCollectingTiles() && playerShip.isHoverDrawing()) {
            drawGameObject(playerShip.getTileHoverIndicator());
            drawGameObject(playerShip);
        }
        drawGameObject(player);// Draw last to be on top of robot
        // Draw hud at this step

//        tentacle.follow(player.getX(), player.getY());
//        tentacle.draw(shapeDrawer);


        TileShipGame.batch.end();

        Box2DWrapper.getInstance().drawDebug(camera);

        Box2DWrapper.getInstance().stepPhysicsSimulation(Gdx.graphics.getDeltaTime());

        // Call collision handling first and then sweep as objects are marked during this step lol
        collisionHandler.handleCollisions();
        Box2DWrapper.getInstance().sweepForDeadBodies();
    }

    @Override
    public void resize(int width, int height) {

        extendViewport.update(width, height);
        hud.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        circle.dispose();
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
}