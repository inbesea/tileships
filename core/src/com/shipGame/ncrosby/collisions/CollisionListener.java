package com.shipGame.ncrosby.collisions;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;

/**
 * Collision listener gets collisions and passes the collision object to a handler
 */
public class CollisionListener implements ContactListener {

    private CollisionHandler collisionHandler;

    public CollisionListener(CollisionHandler collisionHandler){
        this.collisionHandler = collisionHandler;
        System.out.println("~ Collision Listener created ~");
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // We can cast to gameObjects because all collisions will extend from gameObjects.
        GameObject gameObjectA = (GameObject) fixtureA.getUserData();
        GameObject gameObjectB = (GameObject) fixtureB.getUserData();

        System.out.println("Begin contact between " + gameObjectA.getID().toString() + " and " + gameObjectB.getID().toString());

        // Add collision object to list to resolve after the physics-simulation step.
        Collision collision = new Collision(gameObjectA, gameObjectB);
        collisionHandler.push(collision);
    }

    @Override
    public void endContact(Contact contact) {
        GameObject gameObjectA = (GameObject) contact.getFixtureA().getUserData();
        GameObject gameObjectB = (GameObject) contact.getFixtureB().getUserData();
        System.out.println( "End contact between " + gameObjectA.getID().toString() + " and " + gameObjectB.getID().toString());
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
