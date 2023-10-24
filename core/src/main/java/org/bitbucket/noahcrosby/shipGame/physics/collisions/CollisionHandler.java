package org.bitbucket.noahcrosby.shipGame.physics.collisions;

import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

import java.util.ArrayList;

/**
 * Handles basic actions of collisions
 * Loops collisions, can take collisions, and handle them however it is implemented.
 */
public abstract class CollisionHandler {
    GameObject a;
    GameObject b;
    ArrayList<Collision> collisions = new ArrayList<>();
    public CollisionHandler() {

    }

    public void handleCollisions() {
        Collision c;
        for (int i = 0 ; i < collisions.size() ; i++){
            c = collisions.get(i);
            handleCollision(c);
        }
        collisions.clear();
    }

    public void push(Collision newCollision) {
        collisions.add(newCollision);
    }

    abstract public void handleCollision(Collision collision);
}
