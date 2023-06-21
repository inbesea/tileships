package com.shipGame.ncrosby.physics.box2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.ShipTile;

import static com.shipGame.ncrosby.util.generalUtil.newStaticBodyDef;

// Handles drawing debug,
public class Box2DWrapper implements Box2DWrapperInterface{

    private boolean drawDebug = false;
    public static final float physicsFrameRate = 1/60f;
    public static final int velocityIterations = 6;
    public static final int positionIterations = 2;
    private float accumulator = 0;

    private static Box2DWrapper INSTANCE;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Array<Body> bodies = new Array<>();

    public Box2DWrapper(Vector2 gravity, boolean doSleep){
        if(INSTANCE != null) throw new RuntimeException("Box2DWrapper already exists!");
        Box2D.init();
        this.world = new World(gravity, doSleep);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        INSTANCE = this;
    }

    /**
     * Removes body from simulation
     * @param body
     */
    public void removeObject(Body body) {
        bodies.removeValue(body, true);
        world.destroyBody(body);
    }

    /**
     * Draws the debug shapes.
     * @param orthoCamera
     */
    @Override
    public void drawDebug(OrthographicCamera orthoCamera) {
        // Need to scale this lol. This will not work on the scale we are drawing.
        if(drawDebug)box2DDebugRenderer.render(world, orthoCamera.combined);
    }

    @Override
    public void newBody() {

    }

    @Override
    public void deleteBody(Body body) {

    }

    @Override
    public void updateGameObjectsToPhysicsSimulation() {
        for (Body b : this.bodies) {
            // Get the body's user data - in this example, our user
            // data is an instance of the Entity class
            GameObject gameObject = (GameObject) b.getUserData();
            Vector2 gameObjectNewPosition;

            if (gameObject != null) {

                // Meant to move the reference point to the bottom left a bit to allign with the physics objects.
                gameObjectNewPosition = new Vector2(b.getPosition().x - gameObject.getSize().x/2, b.getPosition().y - gameObject.getSize().y/2);
                // Vector2 gameObjectNewPosition = new Vector2(b.getPosition().x, b.getPosition().y);

                //
                if(b.getType().equals(BodyDef.BodyType.StaticBody)){
                    continue;
                }
                // Update the entities/sprites position and angle
                gameObject.setPosition(gameObjectNewPosition);

                try{
                    gameObject.getBounds().setPosition(gameObjectNewPosition);
                }catch (NullPointerException nullPointerException){

                }

                try{
                    gameObject.getCircleBounds().setPosition(gameObjectNewPosition);
                } catch (NullPointerException nullPointerException){
                    // shouldn't do this probably but w/e
                }


                // We need to convert our angle from radians to degrees
                gameObject.setRotation(MathUtils.radiansToDegrees * b.getAngle());
            }
        }
    }

    @Override
    public void stepPhysicsSimulation(float deltaTime) {
        /**
         * Steps the physics simulation.
         */
//        world.step(game.physicsFrameRate, velocityIterations, positionIterations);
            // fixed time step
            // max frame time to avoid spiral of death (on slow devices)
            float frameTime = Math.min(deltaTime, 0.25f);
            accumulator += frameTime;
            while (accumulator >= physicsFrameRate) {
                world.step(physicsFrameRate, velocityIterations, positionIterations);
                accumulator -= physicsFrameRate;
            }
    }

    public void drawDebug(){
        drawDebug = true;
    }

    public void stopDrawDebug(){
        drawDebug = false;
    }

    /**
     * Gives objects their physics attributes and hands the body values to the wrapper list.
     * @param object
     */
    public void setObjectPhysics(GameObject object) {
        Vector2 position = object.getPosition();

        // Adjust init position to center the box on the tile
        BodyDef bodyDef = newStaticBodyDef(position.x + ShipTile.TILESIZE/2, position.y + ShipTile.TILESIZE/2);
        Body body = world.createBody(bodyDef);

        PolygonShape tileShape = new PolygonShape();

        tileShape.setAsBox(ShipTile.TILESIZE/2, ShipTile.TILESIZE/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = tileShape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(object);

        body.setUserData(object);
        object.setBody(body);

        // This step makes the body available to use on the screen level.
        this.bodies.add(body); // This need to be eliminated so the calls can be done elsewhere.
        // This means we need to create the replacement and then remove the old system.

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        tileShape.dispose();
    }

    public static Box2DWrapper getInstance(){
        if(INSTANCE == null)throw new RuntimeException("Box2DWrapper not initialized!");
        else return INSTANCE;
    }


    /**
     * Reviews and removes dead physics bodies
     * */
    public void sweepForDeadBodies() {
        for (Body body : this.bodies) {
            if (body != null) {
                GameObject gameObject = (GameObject) body.getUserData();
                if (gameObject.isDead()) {
                    // We have not removed the object from the game.
                    // We might need to just bite the bullet and create a gameobject call for deleting the object from an agnostic point of view.
                    System.out.println("Removing a " + gameObject.getID().toString() + " instance from the game.");
                    attemptGameObjectRemoval(gameObject);
                }
            }
        }
    }

    /**
     * Meant to filter out the possible use of
     * @param gameObject
     */
    private void attemptGameObjectRemoval(GameObject gameObject) {
        ID id = gameObject.getID();
        if(id.isTileType()) {
            System.out.println("Deleting a tile");
            ShipTile tile = (ShipTile) gameObject;
            tile.destroySelf();
        } else {
            System.out.println("Deleting not a tile");
            gameObject.deleteFromGame();
        }
    }
}
