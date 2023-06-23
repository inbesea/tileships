package com.shipGame.ncrosby.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public interface PhysicsObject {
    public Body setPhysics(World world);

    public void setBody(Body body);

    public Body getBody();

}
