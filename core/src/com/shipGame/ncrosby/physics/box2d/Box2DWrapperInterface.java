package com.shipGame.ncrosby.physics.box2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.physics.collisions.CollisionListener;

// This interface is meant to provide a high level set of actions to code on a lower level.
public interface Box2DWrapperInterface {

    public abstract void drawDebug(OrthographicCamera orthographicCamera);

    public abstract void newBody(GameObject object);

    public abstract void deleteBody(Body body);

    // A way to get the related body for the game object to sync to.
    public abstract void updateGameObjectsToPhysicsSimulation();

    public abstract void stepPhysicsSimulation(float deltaTime);

    public abstract void setWorldContactListener(CollisionListener collisionListener);
}
