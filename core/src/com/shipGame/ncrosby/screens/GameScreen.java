package com.shipGame.ncrosby.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.physics.collisions.CollisionHandler;
import com.shipGame.ncrosby.physics.collisions.CollisionListener;
import com.shipGame.ncrosby.player.PlayerInput;
import com.shipGame.ncrosby.generalObjects.Asteroid;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.HUD;
import com.shipGame.ncrosby.generalObjects.Player;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.player.SimpleTouch;
import com.shipGame.ncrosby.tileShipGame;
import com.shipGame.ncrosby.managers.AsteroidManager;

import java.util.Objects;

/**
 * Main game screen - where the game happens
 */
public class GameScreen implements Screen {
    final tileShipGame game;
    final AsteroidManager asteroidManager;
    private HUD hud;
    ExtendViewport extendViewport;
    private final Player player;

    // Represents each side's size
    public static final Vector2 playerSize = new Vector2(tileShipGame.convertPixelsToMeters(47),tileShipGame.convertPixelsToMeters(53));

    OrthographicCamera camera;
    SimpleTouch st;
    AssetManager assetManager;
    private final Array<GameObject> gameObjects;
    private final Ship playerShip;
    public static final float spawnAreaMax = tileShipGame.convertPixelsToMeters(300);
    Music gameScreenMusic;
    CircleShape circle = new CircleShape();
    public Array<Body> bodies = new Array<Body>();
    public World world;

    private CollisionListener collisionListener;
    private CollisionHandler collisionHandler;

    public GameScreen(final tileShipGame game) {
        this.game = game;
        this.assetManager = game.assetManager;
        game.setGameScreen(this); // Give this to be disposed at exit
        this.world = game.world;
        this.bodies = game.bodies;

        gameScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune/MainMenu Extended Messingaround.wav"));
        gameScreenMusic.play();
        gameScreenMusic.setVolume(0.5f);
        gameScreenMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileShipGame.defaultViewportSizeX, tileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(tileShipGame.defaultViewportSizeX,tileShipGame.defaultViewportSizeY, camera);

        // init ship
        playerShip = new Ship(new Vector2(-1, -1), assetManager);
        game.setPlayerShip(playerShip);
        playerShip.setScreen(this);
        playerShip.initialize();

        // init player
        // Give player the game reference
        player = new Player(new Vector2(playerShip.getX(), playerShip.getY()), playerSize, ID.Player, camera, this.game);

        // Place init game objects into the array
        gameObjects = new Array<>();
        gameObjects.add(player);
        gameObjects.add(playerShip);

        // Add input event handling
        st = new SimpleTouch(this);
        Gdx.input.setInputProcessor(st);

        asteroidManager = new AsteroidManager(this);
        hud = new HUD(game.assetManager, game);

        // Create collision listener
        collisionHandler = new CollisionHandler(asteroidManager, world);// Handler has manager to manage stuff
        collisionListener = new CollisionListener(collisionHandler);// Listener can give collisions to collision handler
        world.setContactListener(collisionListener);
    }

    @Override
    public void show() {

    }

    /**
     * Method to update game objects with the bodies object position
     *
     * Called before drawing
     */
    private void updateGameObjectsForPhysics(){
        for (Body b : this.bodies) {
            // Get the body's user data - in this example, our user
            // data is an instance of the Entity class
            GameObject gameObject = (GameObject) b.getUserData();

            if (gameObject != null) {
                // Meant to move the reference point to the bottom left a bit to allign with the physics objects.
                Vector2 gameObjectNewPosition = new Vector2(b.getPosition().x - gameObject.getSize().x/2, b.getPosition().y - gameObject.getSize().y/2);
//                Vector2 gameObjectNewPosition = new Vector2(b.getPosition().x, b.getPosition().y);

                if(b.getType().equals(BodyDef.BodyType.StaticBody)){
                    continue;
                }
                // Update the entities/sprites position and angle
                gameObject.setPosition(gameObjectNewPosition);

                try{
                    gameObject.getBounds().setPosition(gameObjectNewPosition);
                }catch (NullPointerException nullPointerException){

                }

                try{
                    gameObject.getCircleBounds().setPosition(gameObjectNewPosition);
                } catch (NullPointerException nullPointerException){
                    // shouldn't do this probably but w/e
                }

//                Vector2 bodyPosition = new Vector2(gameObject.getX(), gameObject.getY());

                // We need to convert our angle from radians to degrees
                gameObject.setRotation(MathUtils.radiansToDegrees * b.getAngle());
            }
        }
    }

    /**
     * Draw the game
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        extendViewport.apply();

        // Update game object positions
        updateGameObjectsForPhysics();

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
        game.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        game.batch.begin();
        GameObject go ;

        for(int i = 0 ; i < gameObjects.size ; i++){
            go = gameObjects.get(i);

            // This render should happen after a sweep attempt
            if(go.isDead())throw new RuntimeException("Game Object was not swept " + go.getID().toString());

            drawGameObject(go); // Call helper to draw object
            //collisionDetection(go);
        }

        if(playerShip.isCollectingTiles() && playerShip.isHoverDrawing()){
            drawGameObject(playerShip.getTileHoverIndicator());
        }
        drawGameObject(player);// Draw last to be on top of robot
        // Draw hud at this step
        game.batch.end();

        game.debugRenderer.render(game.world, camera.combined);

        game.stepPhysicsWorld(Gdx.graphics.getDeltaTime());

        // Call collision handling first and then sweep as objects are marked during this step lol
        collisionHandler.handleCollisions();
        collisionHandler.sweepForDeadBodies(this.bodies);
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
        // Get the texture of the game object and draw it based on the GameScreen Camera.
        String textureString = gameObject.getTexture();

        // Updates objects here.
        gameObject.render(this.game);
        if (!Objects.equals(textureString, MainMenuScreen.ignoreLoad) && !Objects.equals(textureString, "")) { // If ID has associated string
            Texture texture = assetManager.get(textureString,Texture.class);
            Vector2 size = gameObject.getSize();
            game.batch.draw(texture, gameObject.getX(), gameObject.getY(), size.x, size.y);
        }
    }


    /**
     * Routes game objects to be checked against other game objects for collisions
     * @param gameObject
     */
    private void collisionDetection(GameObject gameObject) {
        if(gameObject.getID() == ID.Ship){ // Is GO Ship object?
            // Ships don't directly have collision with anything. Things check if they collide with tiles in the ship
        }else{ // All other objects
            GameObject go;
            for(int i = 0 ; i < gameObjects.size ; i++){ // Pass by reference for loop

                go = gameObjects.get(i); // Get a go from all game objects

                if(go.getID() == ID.Ship && gameObject.getID() == ID.Asteroid){ // If the object checked and the possible collision is
                    Ship ship = (Ship) go;
                    // with an asteroid then sent the asteroid into method of the ship.
                    ship.collision(gameObject, this); // Give object to ship to check collision for tiles in ship
                    break;
                } else { // tiles are in ship so we can take that on it's own.
//                    if(gameObject == go)continue;
//                    if(gameObject.getBounds().contains(go.getBounds())){
//                        gameObject.collision(go);
//                        go.collision(gameObject);
//                    }
                }
            }
        }

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

        if(i < 0){
            throw new RuntimeException("gameObject not found in GameScreen existing game objects - number of objects... : " + gameObjects.size +
                    " location of gameObject " + gameObject.getX() + ", " + gameObject.getY() + " GameObject ID : " +gameObject.getID());
        }
        return gameObjects.removeIndex(i); // Returns the object reference and removes it.
    }

    /**
     * Returns a reference to the screens extended viewport
     * @return
     */
    public ExtendViewport getExtendViewport() {
        return extendViewport;
    }

    public tileShipGame getGame() {
        return game;
    }


    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }


    /**
     * removes an asteroid instance from the asteroid manager
     * @param asteroid
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

    /**
     * Method to call ending the game execution
     */
    public void quitGame() {
        Gdx.app.exit();
    }
}
