package org.bitbucket.noahcrosby.shipGame.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Game objects associated with physics will need to implement this.
 * This provides an interface for the body of a game object to be set and handled uniquely for each object.
 *
 * Important for the Box2DWrapper system.
 */
public interface PhysicsObject {
    public Body setPhysics(World world);// Remember to dispose of the created shape dude.

    public void setBody(Body body);

    public Body getBody();
    public void setPosition(Vector2 position);
    public void setVelocity(Vector2 velocity);

}
