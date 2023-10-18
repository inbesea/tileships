package org.bitbucket.noahcrosby.shipGame.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import java.util.ArrayList;

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
    private int asteroidLimit;
    // Keep managed asteroid within manager
    private ArrayList<Asteroid> asteroids = new ArrayList<>();
    private int numberOfAsteroids;
    // Needed for physics simulation
    Circle asteroidSpawnZone;
    float spawnRadius = TileShipGame.defaultViewportSizeX;
    OrthographicCamera camera;
    Box2DWrapper box2DWrapper;

    /**
     * Spawns/cleans up asteroids and adds/removes them from passed physics simulation
     * Assumes spawning is true if using this constructor.
     *
     * @param box2DWrapper - physics simulation for asteroids to use
     * @param camera - Orthographic camera used to see edges of screen
     */
    public AsteroidManager(Box2DWrapper box2DWrapper, OrthographicCamera camera){
        asteroidLimit = 20;
        numberOfAsteroids = 0;
        spawning = true; // Assume spawning if using this constructor.
        this.camera = camera;  // Needed for seeing edges of screen?
        this.box2DWrapper = box2DWrapper;

        this.asteroidSpawnZone = new Circle();
        asteroidSpawnZone.setRadius(spawnRadius);
    }

    public AsteroidManager(Box2DWrapper box2DWrapper, OrthographicCamera camera, boolean spawning){
        this.spawning = spawning; // Set initial spawning state
        asteroidLimit = 30;
        numberOfAsteroids = 0;
        this.camera = camera;  // Needed for seeing edges of screen?
        this.box2DWrapper = box2DWrapper;

        this.asteroidSpawnZone = new Circle();
        asteroidSpawnZone.setRadius(spawnRadius);
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
//        spawnRadius = spawnRadius + screen.getCamera().zoom;

        // Check asteroids and remove any outside of the "zoneOfPlay"
        // how to handle that... We could have that be an explicitly set value defining a box that removes asteroids
        // Or it could be a value that tells you how much space will be outside the viewport that can be used.
        Asteroid temp;
        for(int i = 0; i < asteroids.size(); i++){
            temp = asteroids.get(i);
            temp.updatePosition();
            if(outOfBounds(temp)){
//                System.out.println("Removing Out of bounds! : " + asteroid.getX() +  ", " + asteroid.getY());
                deleteMember(temp);
                numberOfAsteroids = asteroids.size();
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
            asteroids.remove(asteroid);
            box2DWrapper.removeObjectBody(asteroid.getBody());
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

        // This is dumb, but the circles aren't being updated properly, while the position is. Deal with it
        asteroid.setPosition(asteroid.getPosition());

        Circle inBoundsRadius = new Circle(asteroidSpawnZone);
        inBoundsRadius.setRadius(spawnRadius);
//        circleBigRadius.setRadius(circleBigRadius.radius + screen.getCamera().zoom + 3);

        boolean inBounds = asteroid.getCircleBounds().overlaps(inBoundsRadius);
        return !inBounds;
    }

    /**
     * Method that spawns an asteroid and adds it to the gameObject list and local asteroids list
     */
    public void spawnAsteroid(){
        // Check if active
        Vector2 spawnLocation = getVectorInValidSpawnArea();

        Asteroid asteroid = new Asteroid(spawnLocation, new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid, this);
        box2DWrapper.setObjectPhysics(asteroid);
        //setAsteroidPhysics(asteroid);

        asteroids.add(asteroid);
        numberOfAsteroids = asteroids.size();
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
        float x = (asteroidSpawnZone.radius + camera.zoom) * (float) Math.sin(betweenZeroAnd2PI);
        float y = (asteroidSpawnZone.radius + camera.zoom) * (float) Math.cos(betweenZeroAnd2PI);

        Vector3 position = new Vector3(x,y,0);
        return new Vector2(position.x,position.y);

        // TODO : Check for spawing on a tile dude wtf
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
     * Removes all asteroids in manager and physics simulation
     */
    public void removeAllAsteroids(){
        // Go through the asteroids and remove their reference from the local list and the screen object list
        for(int i = 0; i < asteroids.size(); i++){
            deleteMember(asteroids.get(i));
        }
        if(asteroids.size() > 0){
            System.out.println("ERROR : removeAllAsteroids() did not clear asteroid array in AsteroidManager");
        }
    }

    /**
     * Returns array of asteroids
     *
     * @return - Array of asteroids
     */
    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
}
