package com.shipGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shipGame.tileShipGame;

public class MainMenuScreen implements Screen {
    public static final String[] spritesToLoad = {"RobotV2.png","asteroid_purple.png","ShipTile_Red.png",
            "ShipTile_Core.png","HoverIndicator.png", "ToBeCollapsed.png",
            "ShipTile_Strong.png"};

    public static final String[] soundsToLoad = {
            "tilePlacement.wav", "collectTileSound.mp3", "buildTileSound.mp3"
    };

    public static final String soundPath = "Sound Effects/";
    public static final String spritePath = "Sprites/";
    public static String ignoreLoad = "Sprites/none"; // Used to ignore IDs without associated sprite references
    final tileShipGame game;

    OrthographicCamera camera;

    Music mainMenuMusic;
    AssetManager assetManager;

    /**
     * Constructs the mainmenu object
     * @param game - Game instance for screen to have context 
     */
    public MainMenuScreen(final tileShipGame game) {
        this.game = game;

        assetManager = game.assetManager;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileShipGame.defaultViewportSizeX, tileShipGame.defaultViewportSizeY);
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune/Audio Export/MainMenuTune.wav"));
        mainMenuMusic.play();
        mainMenuMusic.setVolume(0.5f);
        mainMenuMusic.setLooping(true);

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }

    @Override
    public void show() {}

    /**
     * Renders out the data for the mainmenu
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Call to load textures to asset manager
        initAssestManager();

        game.batch.begin();
        if(assetManager.update()){
            game.font.getData().setScale(0.04f, 0.04f);
            game.font.draw(game.batch, "Welcome to tileships!!! ", 0, 3.9f);
            game.font.draw(game.batch, "Tap anywhere to begin!", 3.125f, 3.125f);
        } else {
            game.font.draw(game.batch, "~~~Loading Assets " + assetManager.getProgress() +" ~~~",3.125f,3.125f);
        }
        game.batch.end();

        if (Gdx.input.isTouched() && assetManager.update()) {
            mainMenuMusic.setLooping(false);
            mainMenuMusic.dispose();
            GameScreen gameScreen = new GameScreen(game);
            game.setScreen(gameScreen);
            game.setGameScreen(gameScreen);
            dispose();
        }
    }


    /**
     * Loads in the assets needed to run the game into the asset manager.
     */
    private void initAssestManager() {
        for (int i = 0 ; i < spritesToLoad.length ; i++){
            assetManager.load(spritePath + spritesToLoad[i], Texture.class);
        }
        for (int i = 0; i < soundsToLoad.length ; i++){
            assetManager.load(soundPath + soundsToLoad[i], Sound.class);
        }
    }

    @Override
    public void resize(int width, int height) {

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

}
