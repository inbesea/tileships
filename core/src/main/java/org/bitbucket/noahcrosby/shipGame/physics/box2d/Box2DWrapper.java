package org.bitbucket.noahcrosby.shipGame.physics.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.physics.PhysicsObject;
import org.bitbucket.noahcrosby.shipGame.physics.collisions.CollisionListener;

import static org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.Asteroid.maxSpeed;
import static org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris.Asteroid.minSpeed;
import static org.bitbucket.noahcrosby.shipGame.util.generalUtil.getRandomlyNegativeNumber;

/**
 * Class meant to make creating game objects and drawing the debug easier
 * wrapping a lot of the functionality, including :
 *  - drawing debug
 *  - creating bodies
 *  - etc.
 */
public class Box2DWrapper implements Box2DWrapperInterface {

    public static final float physicsFrameRate = 1 / 60f;
    public static final int velocityIterations = 6;
    public static final int positionIterations = 2;
    private float accumulator = 0;

    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final Array<Body> bodies = new Array<>();

    public Box2DWrapper(Vector2 gravity, boolean doSleep) {
        Box2D.init();
        this.world = new World(gravity, doSleep);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
    }


    /**
     * Draws the debug shapes.
     * DO NOT CALL IN A BATCH OR IT WILL RUIN THE RENDER
     *
     * @param orthoCamera gives the projection matrix
     */
    @Override
    public void drawDebug(OrthographicCamera orthoCamera) {
        // Need to scale this lol. This will not work on the scale we are drawing.
        box2DDebugRenderer.render(world, orthoCamera.combined);
    }

    @Override
    public void newBody(GameObject object) {
        Gdx.app.debug("spawnAsteroid", "NOT IMPLEMENTED");
    }

    /**
     * Draws the physics object shapes
     *  *NOTE* : Does not scale to the game size.
     * @param matrix4
     */
    public void drawDebug(Matrix4  matrix4) {
        // Need to scale this lol. This will not work on the scale we are drawing.
        box2DDebugRenderer.render(world, matrix4);
    }

    public BodyDef newBody(PhysicsObject physicsObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = physicsObject.getBodyType();
        bodyDef.position.set(physicsObject.getPhysicsPosition());
        return bodyDef;
    }

    /**
     * Removes body from simulation
     *
     * @param body The body to remove
     */
    @Override
    public void deleteBody(Body body) {
        if(bodies.contains(body, true)){
            bodies.removeValue(body, true);
            world.destroyBody(body);
        }
    }

    /**
     * Sets game objects to physics simulation positions/angles
     */
    @Override
    public void updateGameObjectsToPhysicsSimulation() {
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            // Get the body's user data - in this example, our user
            // data is an instance of the Entity class
            GameObject gameObject = (GameObject) b.getUserData();
            Vector2 gameObjectNewPosition;

            if (gameObject != null) {

                // Meant to move the reference point to the bottom left a bit to align with the physics objects.
                gameObjectNewPosition = new Vector2(b.getPosition().x - gameObject.getSize().x / 2, b.getPosition().y - gameObject.getSize().y / 2);

                // Scale up to actual game size
                gameObjectNewPosition.x = gameObjectNewPosition.x * ShipTile.TILE_SIZE;
                gameObjectNewPosition.y = gameObjectNewPosition.y * ShipTile.TILE_SIZE;

                // Continue if the body is static (unmovable)
                if (b.getType().equals(BodyDef.BodyType.StaticBody)) {
                    continue;
                }
                // Update the entities/sprites position and angle
                gameObject.setPosition(gameObjectNewPosition);

                // We need to convert our angle from radians to degrees
                gameObject.setRotation(MathUtils.radiansToDegrees * b.getAngle());
            }
        }
    }

    /**
     * Steps the physics simulation.
     */
    @Override
    public void stepPhysicsSimulation(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= physicsFrameRate) {
            world.step(physicsFrameRate, velocityIterations, positionIterations);
            accumulator -= physicsFrameRate;
        }
        updateGameObjectsToPhysicsSimulation();
    }

    /**
     * Sets the collision Listener to this Box2D system. This will define how to handle collision events within this system.
     * @param collisionListener
     */
    @Override
    public void setWorldContactListener(CollisionListener collisionListener) {
        world.setContactListener(collisionListener);
    }

    /**
     * Gives physicsObjects their physics attributes and hands the body values to the simulation.
     *
     * @param physicsObject the PhysicsObject to set
     */
    public void initPhysicsObject(PhysicsObject physicsObject) {

        // Set up bodyDef
        BodyDef bodyDef = newBody(physicsObject);

        // Create body
        Body body = this.world.createBody(bodyDef);
        if(body.getType().equals(BodyDef.BodyType.DynamicBody)){
            body.setLinearVelocity(physicsObject.getInitVelocity());
        }
        Shape shape = physicsObject.getShape();

        // Set up fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = physicsObject.getDensity();
        fixtureDef.friction = physicsObject.getFriction();
        fixtureDef.restitution = physicsObject.getRestitution();
        fixtureDef.filter.categoryBits = physicsObject.getCategoryBits();
        fixtureDef.filter.maskBits = physicsObject.getMaskBits();
        Fixture fixture = body.createFixture(fixtureDef);

        // Finish up and clean up.
        fixture.setUserData(physicsObject);
        body.setUserData(physicsObject);
        physicsObject.setBody(body);

        shape.dispose();

        // This step makes the body available to use on the screen level.
        this.bodies.add(body);
    }

    /**
     * Reviews and removes dead physics bodies
     */
    public void sweepForDeadBodies() {
        for (int i = 0; i < bodies.size; i++) {
            Body body = bodies.get(i);
            if (body != null) {
                GameObject gameObject = (GameObject) body.getUserData();
                if (gameObject.isDead()) {
                    // We have not removed the object from the game.
                    // We might need to just bite the bullet and create a GameObject call for deleting the object from an agnostic point of view.
                    System.out.println("Removing a " + gameObject.getID().toString() + " instance from the game.");
                    attemptGameObjectRemoval(gameObject);
                }
            }
        }
    }

    /**
     * Meant to filter out the possible use of
     *
     * @param gameObject the game object to remove
     */
    private void attemptGameObjectRemoval(GameObject gameObject) {
        ID id = gameObject.getID();
        if (id.isTileType()) {
            System.out.println("Deleting a tile");
            ShipTile tile = (ShipTile) gameObject;
            tile.setIsDeadTrue();
        } else {
            System.out.println("Deleting not a tile");
            gameObject.deleteFromGame();
        }
    }

    public void resetPhysicsObject(PhysicsObject physicsObject, Vector2 position) {
        this.deleteBody(physicsObject.getBody());
        physicsObject.setPosition(position);
        physicsObject.setVelocity(new Vector2((int) getRandomlyNegativeNumber(minSpeed,maxSpeed),
            (int) getRandomlyNegativeNumber(minSpeed,maxSpeed)));
        this.initPhysicsObject(physicsObject);
    }
}
