package com.shipGame.ncrosby.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Asteroid;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;

import java.util.Arrays;

import static com.shipGame.ncrosby.util.generalUtil.getRandomNumber;
import static com.shipGame.ncrosby.util.generalUtil.getRandomlyNegativeNumber;

/*
* Have a way to call this during render that will handle adding more asteroids.
* Asteroids will handle themselves when added, but this will decide if they need to be added or removed.
*/

/**
 * Handles the creation and upkeep of a gameobject type.
 * Is a way to move the responsibilities of spawning to an object outside of the Gamescreen
 */
public class AsteroidManager {
    private boolean spawning;
    private GameScreen screen;
    private int asteroidLimit;
    Rectangle zoneOfPlay;
    // Keep asteroid references for simplicity
    private Array<Asteroid> asteroids = new Array<>();
    private int numberOfAsteroids = asteroids.size;
    // Needed for physics simulation
    World world;
    Circle circle;
    float spawnRadius;

    /**
     * Spawns asteroids.
     * Assumes spawning is true if using this constructor.
     *
     * @param screen
     */
    public AsteroidManager(GameScreen screen){
        this.screen = screen;
        asteroidLimit = 20;
        numberOfAsteroids = 0;
        spawning = true; // Assume spawning if using this constructor.
        this.world = screen.getGame().world;

        this.circle = new Circle();
        spawnRadius = screen.getCamera().viewportWidth;
        circle.setRadius(spawnRadius);
//        initSpawn();
    }

    public AsteroidManager(GameScreen screen, boolean spawning){
        this.screen = screen;
        this.spawning = spawning;
        asteroidLimit = 30;
        numberOfAsteroids = 0;
        this.world = screen.getGame().world;

        this.circle = new Circle();
        spawnRadius = screen.getCamera().viewportWidth;
        circle.setRadius(spawnRadius);
    }

    /**
     * Method to tick to check for asteroid cleanups and additions
     */
    public void checkForSpawn(){
        cleanup();
        while(canSpawn()){
            spawnAsteroid();
        }
    }

    /**
     * Check asteroids and remove any outside of the "zoneOfPlay" - Cleaning up the asteroids that stray too far
     * @return - marking this as unfinished
     */
    private void cleanup() {
        // Check asteroids and remove any outside of the "zoneOfPlay"
        // how to handle that... We could have that be an explicitly set value defining a box that removes asteroids
        // Or it could be a value that tells you how much space will be outside the viewport that can be used.
        for(Asteroid asteroid: asteroids){
            if(!screen.getGameObjects().contains(asteroid, true)){
                throw new RuntimeException("Aw jeez");
            }
            if(outOfBounds(asteroid)){
//                System.out.println("Removing Out of bounds! : " + asteroid.getX() +  ", " + asteroid.getY());
                removeAsteroid(asteroid);
                screen.removeGameObject(asteroid);
                numberOfAsteroids = asteroids.size;
//                System.out.println("numberOfAsteroids " + numberOfAsteroids);
            }
        }
    }

    /**
     * removes asteroid instance by identity
     * @param asteroid - asteroid to remove
     */
    public void removeAsteroid(Asteroid asteroid){
        asteroids.removeValue(asteroid, true);
        world.destroyBody(asteroid.getBody());
        screen.bodies.removeValue(asteroid.getBody(), true);
    }

    /**
     * Returns true if asteroid instance has larger abs x,y than allowed.
     *
     * @param asteroid - asteroid instance to check
     * @return - true if x,y bigger than bounds
     */
    private boolean outOfBounds(Asteroid asteroid) {
        Circle circleBigRadius = new Circle(circle);
        circleBigRadius.setRadius(circleBigRadius.radius + screen.getCamera().zoom + 3);
        boolean oob = !asteroid.getCircleBounds().overlaps(circleBigRadius);
        return oob;
    }

    /**
     * Method that spawns an asteroid and adds it to the gameObject list and local asteroids list
     */
    public void spawnAsteroid(){

        // Check if active
        if(spawning){
            Vector2 spawnLocation = getVectorInValidSpawnArea();

            Asteroid asteroid = new Asteroid(spawnLocation, new Vector2(1f,1f), ID.Asteroid);
            setAsteroidPhysics(asteroid);

            screen.newGameObject(asteroid);
            asteroids.add(asteroid);
            numberOfAsteroids = asteroids.size;
            int i = screen.getGameObjects().indexOf(asteroid, true); // Get index of gameObject
            if(i < 0){
                throw new RuntimeException("gameObject not found in GameScreen existing game objects - number of objects... : " + screen.getGameObjects().size +
                        " location of gameObject " + asteroid.getX() + ", " + asteroid.getY() + " GameObject ID : " +asteroid.getID());
            }
        }
//        System.out.println("Spawned Asteroid : asteroids.size " + asteroids.size);
    }

    /**
     * Easy way to add physics attributes to asteroid instance
     */
    private void setAsteroidPhysics(Asteroid asteroid) {
        Vector2 position = asteroid.getPosition();
        BodyDef bodyDef = generalUtil.newDynamicBodyDef(position.x + asteroid.getCircleBounds().radius, position.y + asteroid.getCircleBounds().radius);
        Body body = screen.world.createBody(bodyDef);

        body.setLinearVelocity(generalUtil.getRandomlyNegativeNumber(Asteroid.minSpeed, Asteroid.maxSpeed),
                getRandomlyNegativeNumber(Asteroid.minSpeed, Asteroid.maxSpeed));

        // Create circle to add to asteroid
        CircleShape circle = new CircleShape();
        circle.setRadius(Asteroid.radius);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.0f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        body.setUserData(asteroid);
        asteroid.setBody(body);
        screen.bodies.add(body);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();
    }

    /**
     * Returns a random point within the non-visible play area
     * @return - Vector outside screen, bound by the spawn area size
     */
    private Vector2 getVectorInValidSpawnArea(){
        // Get a value to use to find a random point on the circle of spawning
        double betweenZeroAnd2PI = getRandomNumber(0d, 2d * Math.PI);

        // Scale up the radius of spawning to hide the spawning.
        // get x,y
        float x = (circle.radius + screen.getCamera().zoom) * (float) Math.sin(betweenZeroAnd2PI);
        float y = (circle.radius + screen.getCamera().zoom) * (float) Math.cos(betweenZeroAnd2PI);

        Vector3 position = new Vector3(x,y,0);
        return new Vector2(position.x,position.y);
    }

    /**
     * Checks if the limit of asteroids has been met
     * @return
     */
    public boolean canSpawn(){
        return numberOfAsteroids <= asteroidLimit;
    }

    /**
     * Sets asteroid spawning to false
     */
    public void spawningOff(){
        spawning = false;
    }

    /**
     * Sets asteroid spawning to true
     */
    public void spawningOn(){
        spawning = true;
    }

    /**
     * Call this to remove all asteroids.
     * Need additional call to remove asteroids as soon as they leave the limit of the viewport
     */
    public void removeAllAsteroids(){
        // Go through the asteroids and remove their reference from the local list and the screen object list
        for(int i = 0 ; i < asteroids.size ; i++){
            screen.removeGameObject(asteroids.removeIndex(i)); // Compact method to remove locally and in GameScreen
        }
        if(asteroids.size == 0) throw new RuntimeException("Did not remove all asteroids in removeAllAsteroids() : \n"
                + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
