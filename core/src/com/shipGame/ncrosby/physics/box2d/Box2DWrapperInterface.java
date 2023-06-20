package com.shipGame.ncrosby.physics.box2d;

import com.badlogic.gdx.physics.box2d.Body;

// This interface is meant to provide a high level set of actions to code on a lower level.
public interface Box2DWrapperInterface {

    public abstract void drawDebug();

    public abstract void newBody();

    public abstract void deleteBody(Body body);

    // A way to get the related body for the game object to sync to.
    public abstract void updateGameObjectsToPhysicsSimulation();
}
