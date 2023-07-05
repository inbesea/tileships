package com.shipGame.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Only game object that can be assocaited with physics will need to implement this.
 * This provides a way for the body of a game object to be set and handled uniquely for each object.
 */
public interface PhysicsObject {
    public Body setPhysics(World world);// Remember to dispose of the created shape dude.

    public void setBody(Body body);

    public Body getBody();

}
