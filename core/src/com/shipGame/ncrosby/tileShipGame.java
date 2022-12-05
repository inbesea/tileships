package com.shipGame.ncrosby;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.screens.MainMenuScreen;

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

	public static int zoomSpeed = 5;

	/**
	 * Initialization of the game stuff
	 */
	@Override
	public void create () {
		assetManager = new AssetManager();
		batch = new SpriteBatch();
		font = new BitmapFont();
		//legacyGame game = new legacyGame(); // Creates game the old way. No longer necessary. Need to create a way to build game in new window.
		this.setScreen(new MainMenuScreen(this));
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
}
