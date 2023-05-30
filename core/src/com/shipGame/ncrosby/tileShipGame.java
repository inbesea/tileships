package com.shipGame.ncrosby;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.screens.MainMenuScreen;
import com.shipGame.ncrosby.util.CollisionListener;

/**
 * Entry point for libGDX framework to run the game.
 * We need the logic to be referenced from this point.
 */
public class tileShipGame extends Game {
	public AssetManager assetManager; // Assetmanager vital for optimization of reused assets!
	Screen gs;
	public SpriteBatch batch; // Draws the textures and fonts etc.
	public BitmapFont font;
	private Ship playerShip;
	public World world;
	public Box2DDebugRenderer debugRenderer;

	public static final float physicsFrameRate = 1/60f;
	public static final int velocityIterations = 6;
	public static final int positionIterations = 2;
	private float accumulator = 0;
	public static int zoomSpeed = 5;
	public static float defaultViewportSizeX = 12.5f, defaultViewportSizeY = 7.5f;
	public static float meterLength = 64f;
	public Array<Body> bodies = new Array<Body>();

	private Array<GameObject> gameObjects = new Array<>();

	/**
	 * Initialization of the game stuff
	 */
	@Override
	public void create () {
		assetManager = new AssetManager();
		batch = new SpriteBatch();
		font = new BitmapFont();
		Box2D.init();
		world = new World(new Vector2(0,0), true);
		createCollisionListener();
		debugRenderer = new Box2DDebugRenderer();

		//legacyGame game = new legacyGame(); // Creates game the old way. No longer necessary. Need to create a way to build game in new window.
		this.setScreen(new MainMenuScreen(this));
		setGameScreen(this.getScreen());
	}

	/**
	 * Init the collision listener
	 * Listener should coordinate collisions, but other object would facilitate what they do. 
	 */
	private void createCollisionListener() {
		// TODO : Create this listener and give it access to all game objects.
		// To allow collisions to have a game-wide effect we will need a list of game objects we can work on.
//		world.setContactListener(new CollisionListener(gameObjects));
	}

	/**
	 * Method to convert a pixel length to meters.
	 * @param pixelLength - pixels to divide by meterLength as defined in tileShipGame
	 * @return - float representing meters from passed pixel length
	 */
	public static float convertPixelsToMeters(float pixelLength){
		return pixelLength / meterLength; // Gives an easy way to swap pixel lengths for the physics simulation
	}

	/**
	 * Steps the physics simulation.
	 */
	public void stepPhysicsWorld(float deltaTime){
//        world.step(game.physicsFrameRate, velocityIterations, positionIterations);
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= physicsFrameRate) {
			world.step(physicsFrameRate, velocityIterations, positionIterations);
			accumulator -= physicsFrameRate;
		}
	}

	/**
	 * Do this each loop
	 */
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		font.dispose();
		batch.dispose();
		assetManager.dispose();
		gs.dispose();
	}

	/**
	 * Used to set current screen in game context
	 *
	 * @param screen - Screen object to set in game object
	 */
	public void setGameScreen(Screen screen){
		this.gs = screen;
	}

	public void setPlayerShip(Ship playerShip) {
		this.playerShip = playerShip;
	}

	public Ship getPlayerShip() {
		return playerShip;
	}

	/**
	 * Easy way to add bodies to the game
	 * @param bodyDef
	 */
	public void addBodyToWorld(BodyDef bodyDef){
		Body body = world.createBody(bodyDef);
		bodies.add(body);
	}

	/**
	 * Removes a body from the bodies array
	 *
	 * Returns a boolean representing if the body was removed
	 * @param body
	 * @return
	 */
	public boolean removeBodyFromWorld(Body body){
		boolean removed = bodies.removeValue(body,true);

		return removed;
	}

	public Array<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(Array<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}
}
