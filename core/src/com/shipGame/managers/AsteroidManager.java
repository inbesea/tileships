package com.shipGame.managers;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ID;
import com.shipGame.generalObjects.Asteroid;
import com.shipGame.generalObjects.GameObject;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.physics.box2d.Box2DWrapper;
import com.shipGame.screens.GameScreen;
import com.shipGame.util.generalUtil;

import static com.shipGame.util.generalUtil.getRandomNumber;

/*
* Have a way to call this during render that will handle adding more asteroids.
* Asteroids will handle themselves when added, but this will decide if they need to be added or removed.
*/

/**
 * Handles the creation and upkeep of a gameobject type.
 * Is a way to move the responsibilities of spawning to an object outside of the Gamescreen
 */
public class AsteroidManager implements Manager {
    private boolean spawning;
    private GameScreen screen;
    private int asteroidLimit;
    Rectangle zoneOfPlay;
    // Keep asteroid references for simplicity
    private Array<Asteroid> asteroids = new Array<>();
    private int numberOfAsteroids = asteroids.size;
    // Needed for physics simulation
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

        this.circle = new Circle();
        spawnRadius = screen.getCamera().viewportWidth;
        circle.setRadius(spawnRadius);
    }

    /**
     * Method to tick to check for asteroid cleanups and additions
     */
    public void checkForSpawn(){
        cleanup();
        while(canSpawn() && spawning){
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
                deleteMember(asteroid);
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
        Box2DWrapper.getInstance().removeObjectBody(asteroid.getBody());
    }

    /**
     * Removes asteroid instance by identity.
     * Meant to be called from an agnostic point of view allowing us to remove objects from a generic GameObject call.
     * @param gameObject
     * @return
     */
    @Override
    public boolean deleteMember(GameObject gameObject) {
        if(gameObject.getID() != ID.Asteroid)return false;
        try{
            Asteroid asteroid = (Asteroid) gameObject;
            asteroids.removeValue(asteroid, true);
            Box2DWrapper.getInstance().removeObjectBody(asteroid.getBody());
            screen.removeGameObject(asteroid);
            return true;
        } catch (ClassCastException cce){
            System.out.println(cce);
            return false;
        }
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
            Vector2 spawnLocation = getVectorInValidSpawnArea();

            Asteroid asteroid = new Asteroid(spawnLocation, new Vector2(ShipTile.TILESIZE,ShipTile.TILESIZE), ID.Asteroid, this);
            Box2DWrapper.getInstance().setObjectPhysics(asteroid);
            //setAsteroidPhysics(asteroid);

            screen.newGameObject(asteroid);
            asteroids.add(asteroid);
            numberOfAsteroids = asteroids.size;
            int i = screen.getGameObjects().indexOf(asteroid, true); // Get index of gameObject
            if(i < 0){
                throw new RuntimeException("gameObject not found in GameScreen existing game objects - number of objects... : " + screen.getGameObjects().size +
                        " location of gameObject " + asteroid.getX() + ", " + asteroid.getY() + " GameObject ID : " +asteroid.getID());
            }
//        System.out.println("Spawned Asteroid : asteroids.size " + asteroids.size);
    }

    /**
     * Returns a random point within the non-visible play area
     * @return - Vector outside screen, bound by the spawn area size
     */
    private Vector2 getVectorInValidSpawnArea(){
        // Get a value to use to find a random point on the circle of spawning
        double betweenZeroAnd2PI = generalUtil.getRandomNumber(0d, 2d * Math.PI);

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

        System.out.println("REMOVE ALL ASTEROIDS NOT IMPLEMENTED");
        return;

//        for(int i = 0 ; i < asteroids.size ; i++){
//            screen.removeGameObject(asteroids.removeIndex(i)); // Compact method to remove locally and in GameScreen
//        }
//        if(asteroids.size == 0) throw new RuntimeException("Did not remove all asteroids in removeAllAsteroids() : \n";
    }
}
