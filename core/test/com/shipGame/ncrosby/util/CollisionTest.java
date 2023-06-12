package com.shipGame.ncrosby.util;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.collisions.Collision;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.generalObjects.Ship.ShipTilesManager;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.StandardTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.StrongTile;
import junit.framework.TestCase;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class CollisionTest extends TestCase {

    GameObject gameObjectA;
    GameObject gameObjectB;
    Collision collision;
    Ship ship;
    ShipTilesManager manager;

    public void setUp(){

        ship = mock(Ship.class);
        manager = ship.getTileManager();

        // Going to need both types for testing different scenarios
        gameObjectA = new StandardTile(new Vector2(0,0), manager);
        gameObjectB = new StrongTile(new Vector2(0,0), manager);
        collision = new Collision(gameObjectA, gameObjectB);
    }

    @Test
    public void testIfGameObjectMatchesOneOrBoth_ReturnsTrue(){
        // Before - have two tiles colliding
        // When - checking types
        // Then - we get appropriate assertions back
        assertTrue(collision.hasType(ID.StrongTile));
        assertTrue(collision.hasType(ID.StandardTile));
        assertFalse(collision.hasType(ID.CoreTile));
    }
}