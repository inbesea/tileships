package com.shipGame.ncrosby.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shipGame.ncrosby.tileShipGame;

public class MainMenuScreen implements Screen {

    final tileShipGame game;

    OrthographicCamera camera;

    Music mainMenuMusic;

    /**
     * Constructs the mainmenu object
     * @param game - Game instance for screen to have context 
     */
    public MainMenuScreen(final tileShipGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune/Audio Export/MainMenuTune.wav"));
        mainMenuMusic.play();
        mainMenuMusic.setLooping(true);
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

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to tileships!!! ", 200, 250);
        game.font.draw(game.batch, "Tap anywhere to begin!", 200, 200);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            mainMenuMusic.setLooping(false);
            mainMenuMusic.dispose();
            game.setScreen(new GameScreen(game));
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
