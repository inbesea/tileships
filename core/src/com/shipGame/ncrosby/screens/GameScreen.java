package com.shipGame.ncrosby.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.PlayerInput;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Player;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.player.SimpleTouch;
import com.shipGame.ncrosby.tileShipGame;
import com.shipGame.ncrosby.util.AsteroidManager;

import java.util.Objects;

/**
 * Main game screen - where the game happens
 */
public class GameScreen implements Screen {
    final tileShipGame game;
    AsteroidManager asteroidManager;

    ExtendViewport extendViewport;
    private final Player player;

    // Represents each side's size
    private final Vector2 playerSize = new Vector2(47,53);

    OrthographicCamera camera;
    SimpleTouch st;

    private final Array<GameObject> gameObjects;
    private final Ship playerShip;
    public static final int spawnAreaMax = 300;


    public GameScreen(final tileShipGame game) {
        this.game = game;
        game.setGameScreen(this); // Give this to be disposed at exit

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        this.extendViewport = new ExtendViewport(650,550, camera);

        // init ship
        playerShip = new Ship(new Vector2(200, 200), ID.Ship, camera);
        game.setPlayerShip(playerShip);

        // init player
        // Give player the game reference
        player = new Player(new Vector2(222, 222), playerSize, ID.Player, camera, this.game);

        // Place init game objects into the array
        gameObjects = new Array<>();
        gameObjects.add(player);
        gameObjects.add(playerShip);

        // Add input event handling
        st = new SimpleTouch(this);
        Gdx.input.setInputProcessor(st);

        asteroidManager = new AsteroidManager(this);
//        newGameObject(new Asteroid(new Vector2(195, 195), new Vector2(64,64), new Vector2(0,0), ID.Asteroid));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        extendViewport.apply();

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        game.batch.begin();
        for (GameObject go : gameObjects) {
            drawGameObject(go); // Call helper to draw object
        }
        drawGameObject(player);// Draw last to be on top of robot
        // Draw hud at this step
        game.batch.end();

        // process user input
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            PlayerInput.handleKeyPressed(player, camera);
        }
        PlayerInput.updateCameraOnPlayer(player, camera);
        asteroidManager.checkForSpawn(); // Handle the
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
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
    }

    /**
     * Helper method holds rendering logic, takes game object
     * Expects to be called after game.batch.begin() and with a batch that has the correct camera location context
     */
    private void drawGameObject(GameObject gameObject) {
        // Get the texture of the game object and draw it based on the GameScreen Camera.
        String t = gameObject.getTexture();

        collisionDetection(gameObject);

        // Handle updates first
        gameObject.render(this.game);
        if (!Objects.equals(t, "none") && !Objects.equals(t, "")) { // If ID has associated string
            Texture texture = new Texture(Gdx.files.internal(t));
            Vector2 size = gameObject.getSize();
            game.batch.draw(texture, gameObject.getX(), gameObject.getY(), size.x, size.y);
        }
    }

    private void collisionDetection(GameObject gameObject) {
//        for(GameObject go: gameObjects){
//            if(gameObject.getBounds().intersects(go.getBounds())){
//                gameObject.collision(go);
//                go.collision(gameObject);
//            }
//        }
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
     * @param go
     */
    public void newGameObject(GameObject go){
        gameObjects.add(go);
    }

    /**
     * Finds the game object, removes and returns it from the game screen Array
     * @param gameObject
     * @return
     */
    public GameObject removeGameObject(GameObject gameObject){
        int i = gameObjects.indexOf(gameObject, true); // Get index of gameObject
        return gameObjects.removeIndex(i); // Returns the object reference and removes it.
    }

    /**
     * Returns a reference to the screens extended viewport
     * @return
     */
    public ExtendViewport getExtendViewport() {
        return extendViewport;
    }
}
