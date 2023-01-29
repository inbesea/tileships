package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;
import com.shipGame.ncrosby.util.AsteroidManager;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.jvm.hotspot.utilities.Assert;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ShipTest extends TestCase {
    public tileShipGame game;
    public GameScreen gameScreen;
    public OrthographicCamera camera;
    public ExtendViewport extendViewport;
    public Ship ship;

    @Before
    public void setUp() throws Exception{
        ship = new Ship(new Vector2(0,0), ID.Ship);
        ship = mock(Ship.class);
        doNothing().when(ship.playBuildSound());
    }

    @Test
    public void whenShipCreated_HasTiles(){
        System.out.println("Running test : whenShipCreated_HasTiles()");
        // Before - Ship exists
        Assert.that(ship.id == ID.Ship, "Ship Exists");

        // Then

        // Therefore
        assert (ship.getEdgeTiles().size > 0);
        assert true;
    }
}