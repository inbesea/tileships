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
import com.shipGame.ncrosby.physics.box2d.Box2DWrapper;
import com.shipGame.ncrosby.screens.MainMenuScreen;

/**
 * Entry point for libGDX framework to run the game.
 * We need the logic to be referenced from this point.
 */
public class tileShipGame extends Game {
	public AssetManager assetManager; // Assetmanager vital for optimization of reused art assets!
	Screen gs;
	public SpriteBatch batch; // Draws the textures and fonts etc.
	public BitmapFont font;
	private Ship playerShip;
	public Box2DWrapper physicsWrapper;
	public World world;

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

		this.physicsWrapper = new Box2DWrapper(new Vector2(0,0), true);

		//legacyGame game = new legacyGame(); // Creates game the old way. No longer necessary. Need to create a way to build game in new window.
		this.setScreen(new MainMenuScreen(this));
		setGameScreen(this.getScreen());
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
