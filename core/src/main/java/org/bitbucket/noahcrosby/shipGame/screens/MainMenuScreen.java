package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapDrawer;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapNode;
import org.bitbucket.noahcrosby.shipGame.LevelData.SpaceMap;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.util.SoundTextBubble;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import com.badlogic.gdx.utils.Timer;

public class MainMenuScreen extends ScreenAdapter implements Screen {
    final TileShipGame game;

    OrthographicCamera camera;

    Music mainMenuMusic;

    private SoundTextBubble welcome;
    private Stage stage;
    private TextButton arcadeMode;
    private TextButton classicMode;
    private TextButton preferences;
    private TextButton exit;
    private CheckBox debug;
    private Table table;
    SpaceMap map = new SpaceMap();
    MapDrawer mapDrawer;
    boolean afterLoadingMap = false;


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

//        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Resources.loadAssets();

        setMapValues();
        mapDrawer = new MapDrawer(map);
    }

    private void setMapValues() {
        map.addNode(new MapNode(new Vector2(100, 100)), new int[]{});
        map.addNode(new MapNode(new Vector2(200, 100)), new int[]{0});
        map.addNode(new MapNode(new Vector2(200, 200)), new int[]{1});
        map.addNode(new MapNode(new Vector2(100, 200)), new int[]{0, 2});
        map.addNode(new MapNode(new Vector2(150, 150)), new int[]{0,1,2,3});
        addToMap(2);
        addToMap(5);
        addToMap(8);
        addToMap(13);
        addToMap(17);
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

        arcadeMode = new TextButton("Endless Mode", skin);
        classicMode = new TextButton("Classic Mode", skin);
        preferences = new TextButton("Preferences", skin);
        exit = new TextButton("Exit", skin);
        debug = new CheckBox("Debug Mode", skin);

        table.add(classicMode).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(arcadeMode).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
        table.row().bottom();
        table.add(debug).align(Align.bottom).align(Align.left);
    }

    private void setButtonBehavior(){
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        classicMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(Resources.assetManager.update()){
                    mainMenuMusic.setLooping(false);
                    mainMenuMusic.dispose();
                    game.changeScreen(TileShipGame.CLASSIC_MODE);
                    dispose();}
            }
        });

        arcadeMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(Resources.assetManager.update()){
                    mainMenuMusic.setLooping(false);
                    mainMenuMusic.dispose();
                    game.changeScreen(TileShipGame.ARCADE_MODE);
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
        this.table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        setUpMenuButtons();
        setButtonBehavior();
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        table.setDebug(AppPreferences.getAppPreferences().getIsDebug());

        mapDrawer.drawMap(camera.combined);


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
            TileShipGame.font.draw(TileShipGame.batch, "T\nI\nL\n" +
                "E\nS\nH\nI\nP\nS", 500, 350);
            if(!afterLoadingMap){
                map.addNode(460, 370);
                map.addNode(new MapNode(new Vector2(520, 370)), new int[]{map.getMapNodes().size - 1});
                map.addNode(new MapNode(new Vector2(520, 150)), new int[]{map.getMapNodes().size - 1});
                map.addNode(new MapNode(new Vector2(460, 150)), new int[]{map.getMapNodes().size - 1, map.getMapNodes().size - 3});
                afterLoadingMap = true;
            }

        } else {
            TileShipGame.font.draw(TileShipGame.batch, "~~~Loading Assets " + Resources.assetManager.getProgress() + " ~~~", 0, camera.viewportHeight - 100);
        }
        TileShipGame.batch.end();
    }

    /**
     * Adds a map node to mainmenu after delay and in random location
     * @param delay
     */
    private void addToMap(int delay) {

        Timer.schedule(new Timer.Task(){
            public void run () {
                map.addNode(new MapNode(new Vector2(generalUtil.getRandomNumber(0,TileShipGame.defaultViewportSizeX),
                        generalUtil.getRandomNumber(0, TileShipGame.defaultViewportSizeY))),
                    new int[]{generalUtil.getRandomNumber(0, map.getMapNodes().size - 1)});
                Gdx.app.postRunnable(new Runnable(){
                    public void run () {
                        Gdx.app.log("addedMapNode", "Posted runnable to Gdx.app");
                    }
                });
            }}, delay);
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
