package com.shipGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.javapoet.Resources;
import com.shipGame.TileShipGame;
import com.shipGame.util.TextBubble;

public class MainMenuScreen implements Screen {
    public static final String[] spritesToLoad = {"RobotV2.png","asteroid_purple.png","ShipTile_Red.png",
            "ShipTile_Core.png","HoverIndicator.png", "ToBeCollapsed.png",
            "ShipTile_Strong.png"};

    public static final String[] soundsToLoad = {
            "tilePlacement.wav", "collectTileSound.mp3", "buildTileSound.mp3"
    };

    public static final String soundPath = "Sound Effects/";
    public static final String spritePath = "Textures/";
    public static String ignoreLoad = "Textures/none"; // Used to ignore IDs without associated sprite references
    final TileShipGame game;

    OrthographicCamera camera;

    Music mainMenuMusic;

    private TextBubble welcome;

    /**
     * Constructs the mainmenu object
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

    @Override
    public void show() {}

    /**
     * Renders out the data for the mainmenu
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
            String str = "Welcome to tileships!!! ";
            if(welcome == null)welcome = new TextBubble("Welcome to tileships!!!!", 100, Resources.sfxCollectTileSound);

            welcome.print(new Vector2(250,350));
            game.font.draw(TileShipGame.batch, "Tap anywhere to begin!", 150, 250);
        } else {
            game.font.draw(TileShipGame.batch, "~~~Loading Assets " + Resources.assetManager.getProgress() +" ~~~",175,275);
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
