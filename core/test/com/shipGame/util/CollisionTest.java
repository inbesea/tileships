package com.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ID;
import com.shipGame.generalObjects.GameObject;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.generalObjects.Ship.ShipTilesManager;
import com.shipGame.physics.collisions.Collision;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.StandardTile;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.StrongTile;
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