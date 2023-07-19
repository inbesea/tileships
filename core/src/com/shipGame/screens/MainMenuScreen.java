package com.shipGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.javapoet.Resources;
import com.shipGame.TileShipGame;
import com.shipGame.util.SoundTextBubble;
import com.shipGame.util.TextBubble;

public class MainMenuScreen extends ScreenAdapter {
    public static final String spritePath = "Textures/";
    final TileShipGame game;

    OrthographicCamera camera;

    Music mainMenuMusic;

    private SoundTextBubble welcome;

    /**
     * Constructs a MainMenu object
     *
     * @param game - Game instance for screen to have context
     */
    public MainMenuScreen(final TileShipGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune/Audio Export/MainMenuTune.wav"));
        mainMenuMusic.play();
        mainMenuMusic.setVolume(0.5f);
        mainMenuMusic.setLooping(true);

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Resources.loadAssets();
    }

    /**
     * Renders out the data for the MainMenu
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.00f, 0.00f, 0.10f, 1);

        camera.update();
        TileShipGame.batch.setProjectionMatrix(camera.combined);

        // Call to load textures to asset manager
        Resources.updateAssets();

        TileShipGame.batch.begin();
        if(Resources.assetManager.update()){
            String str = "Welcome to tileships!!!";
            if(welcome == null)welcome = new SoundTextBubble(str, 100, Resources.sfxCollectTileSound, null);

            welcome.update(new Vector2(250,350));
            game.font.draw(TileShipGame.batch, "Tap anywhere to begin!", 150, 250);
        } else {
            game.font.draw(TileShipGame.batch, "~~~Loading Assets " + Resources.assetManager.getProgress() + " ~~~", 175, 275);
        }
        TileShipGame.batch.end();

        if (Gdx.input.isTouched() && Resources.assetManager.update()) {
            mainMenuMusic.setLooping(false);
            mainMenuMusic.dispose();
            GameScreen gameScreen = new GameScreen(game);
            game.setScreen(gameScreen);
            game.setGameScreen(gameScreen);
            dispose();
        }
    }
}