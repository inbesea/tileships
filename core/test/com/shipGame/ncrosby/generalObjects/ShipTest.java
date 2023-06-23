package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.physics.box2d.Box2DWrapper;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ShipTest extends TestCase {
    public Ship ship;
    public AssetManager assetManager;
    Box2DWrapper box2DWrapper;

    @Before
    public void setUp() throws Exception{
        box2DWrapper = mock(Box2DWrapper.class);
//        when(box2DWrapper.).thenReturn(null);
        ship = new Ship(new Vector2(0,0), assetManager);
    }

    @Test
    public void testWhenShipCreated_Exists(){
        System.out.println("Asserting ship exists");
        assert(ship != null);
    }

//    @Test
//    public void whenShipCreated_HasTiles(){
//        System.out.println("Running test : whenShipCreated_HasTiles()");
//        // Before - Ship exists
//        System.out.println("Trying to add one tile");
//        ship.addTileToShip(0,0, ID.CoreTile);
//
//        // Then
//
//        // Therefore
//        System.out.println("Asserting ship has tiles");
//        assert (ship.getEdgeTiles().size > 0);
//        assert true;
//    }
}