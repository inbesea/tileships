package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.HUD;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.managers.AsteroidManager;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;

public class ArcadeModeScreen extends ScreenAdapter  implements Screen {
    // Varibles here
    private Ship arcadeShip;
    final TileShipGame game;
    ExtendViewport extendViewport;
    static OrthographicCamera camera;
    private final HUD hud;

    Box2DWrapper box2DWrapper;

    private AsteroidManager asteroidManager;
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
        asteroidManager = new AsteroidManager(box2DWrapper, camera);

        arcadeGameObjects = new Array<>();
    }

    @Override
    public void render(float delta) {

    }

    // Methods
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

        extendViewport.update(width, height);
        hud.update(width, height);
    }

}
