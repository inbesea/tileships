package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.HUD;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Player;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.input.*;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;

import java.util.ArrayList;

public class ArcadeModeScreen extends ScreenAdapter  implements Screen {
    // Varibles here
    private Ship arcadeShip;
    TileCollectHandler tileCollectHandler;
    private TileDragHandler tileDragHandler;
    ZoomHandler zoomHandler;
    DebugInputHandler debugInputHandler;
    private Player player;
    final TileShipGame game;
    ExtendViewport extendViewport;
    static OrthographicCamera camera;
    private final HUD hud;
    InputPreProcessor input;
    Box2DWrapper box2DWrapper;

    final AsteroidManager asteroidManager;
    private final Array<GameObject> arcadeGameObjects; // Used to render the objects?

    // Constructor
    public ArcadeModeScreen(final TileShipGame game) {
        this.game = game;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        this.extendViewport = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY, camera);

        // Create HUD
        hud = new HUD(game);

        // Create Box2D
        box2DWrapper = new Box2DWrapper(new Vector2(0, 0), true);


        arcadeShip = new Ship(new Vector2(0,0), box2DWrapper);
        arcadeShip.initialize();
        asteroidManager = new AsteroidManager(box2DWrapper, camera);

        player = new Player(new Vector2(arcadeShip.getX(), arcadeShip.getY()), GameScreen.playerSize, ID.Player, camera, this.game);

        initializeInputEventHandling();

        arcadeGameObjects = new Array<>();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.00f, 0.00f, 0.10f, 1));

        // Sets focus to drawn on extended viewport
        extendViewport.apply();

        // Update game object positions
        box2DWrapper.updateGameObjectsToPhysicsSimulation();
        asteroidManager.checkForSpawn(); // Handle the asteroid spawning

        // Render asteroids
        TileShipGame.batch.begin();
        box2DWrapper.drawDebug(camera);
        TileShipGame.batch.end();

        drawGameObjects(asteroidManager.getAsteroids());

        // Don't forget to step the simulation
        box2DWrapper.stepPhysicsSimulation(Gdx.graphics.getDeltaTime());
        System.out.println("First Asteroid position " + asteroidManager.getAsteroids().get(0).getPosition().toString());

    }

    private void initializeInputEventHandling() {
        input = new InputPreProcessor(camera);
        input.addProcessor(tileCollectHandler = new TileCollectHandler(arcadeShip));
        tileDragHandler = new TileDragHandler(player);
        input.addProcessor(tileDragHandler);
        input.addProcessor(debugInputHandler = new DebugInputHandler(game, this.arcadeShip, this.tileDragHandler));
        input.addProcessor(zoomHandler = new ZoomHandler(camera));
        Gdx.input.setInputProcessor(input);
    }

    /**
     * Draws specific array[] of type-identical game objects
     * @param gameObjects
     */
    private void drawGameObjects(ArrayList<? extends GameObject> gameObjects) {
        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        TileShipGame.batch.setProjectionMatrix(extendViewport.getCamera().combined);

        // begin a new batch
        // Loops through the game objects and draws them.
        // Uses the game and camera context to handle drawing properly.
        TileShipGame.batch.begin();

        System.out.println("First Asteroid position " + gameObjects.get(0).getPosition().toString());

        for (GameObject go : gameObjects) {

            // This render should happen after a sweep attempt
            if (go.isDead()) {
                System.out.println("ERROR : GameObject " + go.getID() + " is dead and didn't get swept");
                continue;
            }

            if (go.getTexture() != null) {
                Vector2 size = go.getSize();
                TileShipGame.batch.draw(go.getTexture(), go.getX(), go.getY(), size.x, size.y);
            }
            go.render(this.game);
        }

        System.out.println("First Asteroid position " + gameObjects.get(0).getPosition().toString());

        TileShipGame.batch.end();
    }

    // Methods
    @Override
    public void show() {
        Resources.MainMenuExtendedMessingaroundMusic.play();
        Resources.MainMenuExtendedMessingaroundMusic.setVolume(game.getPreferences().getMusicVolume());
        Resources.MainMenuExtendedMessingaroundMusic.setLooping(true);
    }

    @Override
    public void resize(int width, int height) {

        extendViewport.update(width, height);
        hud.update(width, height);
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
        Resources.MainMenuExtendedMessingaroundMusic.pause();
    }

    @Override
    public void resume() {
        System.out.println("Resuming " + this.getClass().getSimpleName());
        Gdx.input.setInputProcessor(input);
        Resources.MainMenuExtendedMessingaroundMusic.play();
    }

}
