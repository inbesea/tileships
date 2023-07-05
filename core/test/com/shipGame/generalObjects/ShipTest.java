package com.shipGame.generalObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.physics.box2d.Box2DWrapper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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