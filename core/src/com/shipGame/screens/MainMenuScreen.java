package com.shipGame.screens;

import com.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javapoet.Resources;
import com.shipGame.TileShipGame;
import com.shipGame.util.SoundTextBubble;
import com.shipGame.util.TextBubble;

public class MainMenuScreen extends ScreenAdapter implements Screen {
    public static final String spritePath = "Textures/";
    final TileShipGame game;

    OrthographicCamera camera;

    Music mainMenuMusic;

    private SoundTextBubble welcome;
    private Stage stage;
    private TextButton newGame;
    private TextButton preferences;
    private TextButton exit;
    private CheckBox debug;
    private Table table;

    /**
     * Constructs a MainMenu object
     *
     * @param game - Game instance for screen to have context
     */
    public MainMenuScreen(final TileShipGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);

        stage = new Stage(new ScreenViewport());

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Resources.loadAssets();
    }

    /**
     * Holds logic to build the main menu buttons
     */
    private void setUpMenuButtons(){
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        // Create a table that fills the screen. Everything else will go inside this table.
        this.table = new Table();
        table.setFillParent(true);
        table.setDebug(AppPreferences.getAppPreferences().getIsDebug());
        stage.addActor(table);
        // TODO : Fix this direct file access so it uses the Automated version
        Skin skin = new Skin(Gdx.files.internal("skin/neon/skin/neon-ui.json"));

        newGame = new TextButton("New Game", skin);
        preferences = new TextButton("Preferences", skin);
        exit = new TextButton("Exit", skin);
        debug = new CheckBox("Debug Mode", skin);

        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
        table.row().bottom();
        table.add(debug).align(Align.bottom).align(Align.left);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        // Need to call it with file string so the remaining files can be loaded
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/MainMenuTune.wav"));
        mainMenuMusic.play();
        mainMenuMusic.setVolume(game.getPreferences().getMusicVolume());
        mainMenuMusic.setLooping(true);

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Each time we show this the table is reasserted to update values
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        setUpMenuButtons();
        setButtonBehavior();
    }

    private void setButtonBehavior(){
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(Resources.assetManager.update()){
                    mainMenuMusic.setLooping(false);
                    mainMenuMusic.dispose();
                    game.changeScreen(TileShipGame.APPLICATION);
                    dispose();}
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(TileShipGame.PREFERENCES);
            }
        });
        debug.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AppPreferences.getAppPreferences().toggleIsDebug();
            }

        });
    }

    /**
     * Renders out the data for the MainMenu
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        mainMenuMusic.setVolume(game.getPreferences().getMusicVolume());

        Gdx.gl.glClearColor(0.00f, 0.00f, 0.10f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        table.setDebug(AppPreferences.getAppPreferences().getIsDebug());

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        camera.update();
        TileShipGame.batch.setProjectionMatrix(camera.combined);

        // Call to load textures to asset manager
        Resources.updateAssets();

        TileShipGame.batch.begin();
        if(Resources.assetManager.update()){
//            if(welcome == null)welcome = new SoundTextBubble(str, 100, Resources.sfxCollectTileSound, null);

//            welcome.update(new Vector2(250,350));
            TileShipGame.font.draw(TileShipGame.batch, "Tap anywhere to begin!", 150, 250);
        } else {
            TileShipGame.font.draw(TileShipGame.batch, "~~~Loading Assets " + Resources.assetManager.getProgress() + " ~~~", 0, camera.viewportHeight - 100);
        }
        TileShipGame.batch.end();
    }

    @Override
    public void pause() {
        mainMenuMusic.pause();
    }

    @Override
    public void resume() {
        mainMenuMusic.play();
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

    public Music getMainMenuMusic() {
        return mainMenuMusic;
    }
}