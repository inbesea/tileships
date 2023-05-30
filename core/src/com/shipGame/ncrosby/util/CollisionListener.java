package com.shipGame.ncrosby.util;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;

/**
 * Collision listener is used to handle collision events based on
 */
public class CollisionListener implements ContactListener {

    // We need this to give us access to all game objects. Without this we cannot properly affect all game objects.
    Array<GameObject> gameObjects;

    public CollisionListener(Array<GameObject> gameObjects){
        this.gameObjects = gameObjects;
        System.out.println("~ Collision Listener created ~");
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        GameObject GameObjectA = (GameObject) fixtureA.getUserData();
        GameObject GameObjectB = (GameObject) fixtureB.getUserData();

        System.out.println("Begin contact between " + fixtureA.toString() + " and " + fixtureB.toString());
        if(matchID(fixtureA, fixtureB, ID.StandardTile, ID.Asteroid)){

        } else if (matchID(fixtureA, fixtureB, ID.CoreTile, ID.Asteroid)){
            // Unsorted values. We need to be able to know which is which to work with these fucking fixtures.
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        System.out.println( "between " + fixtureA.toString() + " and " + fixtureB.toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    /**
     * Checks the associated IDs and returns true if they are both the IDs checked against
     * @param fixtureA
     * @param fixtureB
     * @param firstID
     * @param secondID
     * @return
     */
    private boolean matchID(Fixture fixtureA, Fixture fixtureB, ID firstID, ID secondID) {
        GameObject a = (GameObject) fixtureA.getUserData();
        GameObject b = (GameObject) fixtureB.getUserData();

        if(a == null || b == null){
            System.out.println("a or b is null");
            return false;
        }
        return (a.getID() == firstID && b.getID() == secondID) ||
                (a.getID() == secondID && b.getID() == firstID);
    }

    /**
     * Checks the associated IDs and returns true if they are both the IDs checked against
     * @param firstID
     * @param secondID
     * @return
     */
    private boolean matchID(GameObject a, GameObject b, ID firstID, ID secondID) {
        if(a == null || b == null){
            System.out.println("a or b is null");
            return false;
        }
        return (a.getID() == firstID && b.getID() == secondID) ||
                (a.getID() == secondID && b.getID() == firstID);
    }

    /**
     * Takes two mystery game objects and returns the game object that matches the type checked for
     * @param type
     * @return
     * @param <T>
     */
    private static <T> T returnValue(GameObject gameObject, Class<T> type){
        return (type.cast(gameObject));
    }

}
