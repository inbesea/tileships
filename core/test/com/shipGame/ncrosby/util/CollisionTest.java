package com.shipGame.ncrosby.util;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.collisions.Collision;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.StandardTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.StrongTile;
import junit.framework.TestCase;
import org.junit.Test;

public class CollisionTest extends TestCase {

    GameObject gameObjectA;
    GameObject gameObjectB;
    Collision collision;

    public void setUp(){
        // Going to need both types for testing different scenarios
        gameObjectA = new StandardTile(new Vector2(0,0));
        gameObjectB = new StrongTile(new Vector2(0,0));
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