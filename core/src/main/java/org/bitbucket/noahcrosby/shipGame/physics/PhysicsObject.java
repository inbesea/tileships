package org.bitbucket.noahcrosby.shipGame.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Game objects associated with physics will need to implement this.
 * This provides an interface for the body of a game object to be set and handled uniquely for each object.
 *
 * Important for the Box2DWrapper system.
 */
public interface PhysicsObject {
    public Shape getShape();
    public BodyDef.BodyType getBodyType();
    public Vector2 getPhysicsPosition();
    public void setBody(Body body);

    public Body getBody();
    public void setPosition(Vector2 position);
    public void setVelocity(Vector2 velocity);

    float getDensity();

    float getFriction();

    float getRestitution();

    // These values for cat., mask, and group are defaults in the Filter.class implementation.
    default short getCategoryBits(){
        return 1;
    };

    default short getMaskBits(){
        return -1;
    };

    default short getGroupIndex() {
        return 0;
    }

    // Only for dynamic objects.
    Vector2 getInitVelocity();
}
