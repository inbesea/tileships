package com.ncrosby.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ncrosby.game.screens.MainMenuScreen;

/**
 * Entry point for libGDX framework to run the game.
 * We need the logic to be referenced from this point.
 */
public class tileShipGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	/**
	 * Initialization of the game stuff
	 */
	@Override
	public void create () {
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
	}
}
