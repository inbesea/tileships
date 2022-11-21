package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class ShipTest extends TestCase {
    public tileShipGame game;
    public GameScreen gameScreen;
    public OrthographicCamera camera;
    public ExtendViewport extendViewport;
    public Ship ship;

    @Before
    public void setUp() throws Exception{
//        game = new tileShipGame();
//        game = mock(tileShipGame.class);
//        Gdx gdx = mock(Gdx.class);
//        when(Gdx.graphics.getWidth())
        camera = new OrthographicCamera(  800,480);
//        camera.setToOrtho(false, 800, 480);
        this.extendViewport = new ExtendViewport(600,500, camera);
//        camera.setToOrtho(false, 800, 480);
//        gameScreen = new GameScreen(game);
//        game.setGameScreen(gameScreen);
//
//        // create the camera and the SpriteBatch
//        ship = new Ship(new Vector2(camera.position.x,camera.position.y), ID.Ship, camera);
    }

    @Test
    public void TestTest(){
        assert true;
    }

    @Test
    public void whenShipCreated_HasTiles(){
        System.out.println("Running test : whenShipCreated_HasTiles()");
        // Before
//        Ship ship = mock(Ship.class);
        Ship ship;
        ship = new Ship(new Vector2(800,480), ID.Ship, camera);

        // Then

        // Therefore
        assert(ship.numberOfShipTiles() > 0);
    }
}