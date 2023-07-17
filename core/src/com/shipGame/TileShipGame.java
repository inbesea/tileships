package com.shipGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.javapoet.Resources;
import com.shipGame.generalObjects.GameObject;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.physics.box2d.Box2DWrapper;
import com.shipGame.screens.MainMenuScreen;

/**
 * Entry point for libGDX framework to run the game.
 * We need the logic to be referenced from this point.
 */
public class TileShipGame extends Game {
	public static float zoomMax = 5;
	public static float zoomMin = 0.5f;
	Screen gs;
	public static SpriteBatch batch; // Draws the textures and fonts etc.
	public static BitmapFont font;
	private Ship playerShip;
	public Box2DWrapper physicsWrapper;
	public static int zoomSpeed = 5;
	public static float defaultViewportSizeX = 700f, defaultViewportSizeY = 500f;
	public static float meterLength = 64f;
	private Array<GameObject> gameObjects = new Array<>();

    /**
     * Initialization of the game stuff
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        this.physicsWrapper = new Box2DWrapper(new Vector2(0, 0), true);

        //legacyGame game = new legacyGame(); // Creates game the old way. No longer necessary. Need to create a way to build game in new window.
        this.setScreen(new MainMenuScreen(this));
        setGameScreen(this.getScreen());
    }

    /**
     * Method to convert a pixel length to meters.
     *
     * @param pixelLength - pixels to divide by meterLength as defined in tileShipGame
     * @return - float representing meters from passed pixel length
     */
    public static float convertPixelsToMeters(float pixelLength) {
        return pixelLength / meterLength; // Gives an easy way to swap pixel lengths for the physics simulation
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        Resources.assetManager.dispose();
        gs.dispose();
    }

    /**
     * Used to set current screen in game context
     *
     * @param screen - Screen object to set in game object
     */
    public void setGameScreen(Screen screen) {
        this.gs = screen;
    }

    public void setPlayerShip(Ship playerShip) {
        this.playerShip = playerShip;
    }

    public Ship getPlayerShip() {
        return playerShip;
    }


    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(Array<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}