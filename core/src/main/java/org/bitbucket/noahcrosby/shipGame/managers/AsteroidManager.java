package org.bitbucket.noahcrosby.shipGame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.AsteroidSpawner;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.ColorAsteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.util.ObjectRoller;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

import static org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.Asteroid.maxSpeed;
import static org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.Asteroid.minSpeed;
import static org.bitbucket.noahcrosby.shipGame.util.generalUtil.getRandomlyNegativeNumber;

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
    private Array<Asteroid> asteroids = new Array<>();
    // Needed for physics simulation
    Circle asteroidSpawnZone;
    float spawnRadius = TileShipGame.defaultViewportSizeX;
    OrthographicCamera camera;
    Box2DWrapper box2DWrapper;
    ObjectRoller asteroidRoller;
    Array<Asteroid> finiteAsteroids;
    boolean arcadeMode = false;

    /**
     * Spawns/cleans up asteroids and adds/removes them from passed physics simulation
     * Assumes spawning is true if using this constructor.
     *
     * @param box2DWrapper - physics simulation for asteroids to use
     * @param camera - Orthographic camera used to see edges of screen
     */
    public AsteroidManager(Box2DWrapper box2DWrapper, OrthographicCamera camera){
        asteroidLimit = 0;
        spawning = true; // Assume spawning if using this constructor.
        this.camera = camera;  // Needed for seeing edges of screen?
        this.box2DWrapper = box2DWrapper;

        this.asteroidSpawnZone = new Circle();
        asteroidSpawnZone.setRadius(spawnRadius);
    }

    public AsteroidManager(Box2DWrapper box2DWrapper, OrthographicCamera camera, boolean spawning){
        this.spawning = spawning; // Set initial spawning state
        asteroidLimit = 0;
        this.camera = camera;  // Needed for seeing edges of screen?
        this.box2DWrapper = box2DWrapper;

        this.asteroidSpawnZone = new Circle();
        asteroidSpawnZone.setRadius(spawnRadius);
    }

    /**
     * Method to tick to check for asteroid cleanups and additions
     */
    public void checkForSpawn(){

        // Set asteroid spawn zone to be center of the screen at least.
        asteroidSpawnZone.setPosition(camera.position.x, camera.position.y);

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
        Asteroid temp;
        Array<Asteroid> tempArray = getAllAsteroids();
        for(int i = 0; i < tempArray.size; i++){
            temp = tempArray.get(i);

            if(temp.getIsDead()){
                deleteMember(temp);
            }

            if(outOfBounds(temp)){
                if(finiteAsteroids.contains(temp, false)){
                    respawn(temp);
                } else {
                    deleteMember(temp);
                }
            }
        }
    }

    /**
     * Reasserts an asteroid's speed and position while resetting the physics body.
     * @param temp
     */
    private void respawn(Asteroid temp) {
        Body body = temp.getBody();
        if(body == null) {
            Gdx.app.error("respawn", "body is null");
            return;
        }
        // TODO : Encapsulate this behavior so we can update a physics game object's position and velocity
        box2DWrapper.resetPhysicsObject(temp, getVectorInValidSpawnArea());
//        box2DWrapper.deleteBody(body);
//        temp.setPosition(getVectorInValidSpawnArea());
//        temp.setVelX((int) getRandomlyNegativeNumber(minSpeed,maxSpeed));
//        temp.setVelY((int) getRandomlyNegativeNumber(minSpeed,maxSpeed));
//        box2DWrapper.setObjectPhysics(temp);
    }

    public void spawnAsteroid(Class<Asteroid> clazz) {
        Vector2 spawnLocation = getVectorInValidSpawnArea();

        final Asteroid asteroid;

        try {
            asteroid = clazz.getDeclaredConstructor().newInstance();
            asteroid.setPosition(spawnLocation);
        } catch (Exception e) {
            Gdx.app.error("Error", e.getMessage());
            return;
        }

        box2DWrapper.setObjectPhysics(asteroid);
        //setAsteroidPhysics(asteroid);

        asteroids.add(asteroid);

    }

    /**
     * Removes asteroid instance by identity.
     * Meant to be called from an agnostic point of view allowing us to remove objects from a generic GameObject call.
     * @param gameObject
     * @return
     */
    @Override
    public boolean deleteMember(GameObject gameObject) {
        if(gameObject.getID() != ID.Asteroid){
            Gdx.app.debug("AsteroidManager", "Object is not an Asteroid");
            return false;
        }
        try{
            Asteroid asteroid = (Asteroid) gameObject;
            finiteAsteroids.removeValue(asteroid, true);
            asteroids.removeValue(asteroid, true);
            box2DWrapper.deleteBody(asteroid.getBody());
            return true;
        } catch (ClassCastException cce){
            System.out.println(cce);
            return false;
        }
    }

    /**
     * Returns the asteroid after attempting to remove it from the list and box2d simulation.
     * @param asteroid
     * @return
     */
    public Asteroid removeMember(Asteroid asteroid){
        if(asteroid.getID() != ID.Asteroid)return null;
        asteroids.removeValue(asteroid, true);
        box2DWrapper.deleteBody(asteroid.getBody());
        return asteroid;
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
        Vector2 spawnLocation = getVectorInValidSpawnArea();

        final Asteroid asteroid;

        if(isArcadeMode()){
            asteroid = new ColorAsteroid(spawnLocation, new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid);
        } else {
            asteroid = new Asteroid(spawnLocation, new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid);
        }

        box2DWrapper.setObjectPhysics(asteroid);
        //setAsteroidPhysics(asteroid);

        asteroids.add(asteroid);
    }

    public void spawnAsteroid(Asteroid asteroid){
        Gdx.app.debug("spawnAsteroid", "NOT IMPLEMENTED");
//        Vector2 spawnLocation = getVectorInValidSpawnArea();
//
//        box2DWrapper.setObjectPhysics(asteroid);
//        asteroids.add(asteroid);
    }

    /**
     * Adds existing asteroid to the manager and physics sim
     * Returns the asteroid to a valid spawn area.
     * @param asteroid
     * @return
     */
    public Asteroid addAsteroid(Asteroid asteroid, boolean isFinite){
        if(asteroids.contains(asteroid, true) || finiteAsteroids.contains(asteroid, true)){
            if(asteroid.getBody() == null){
                Vector2 spawnLocation = getVectorInValidSpawnArea();

                asteroid.setPosition(spawnLocation);
                box2DWrapper.setObjectPhysics(asteroid);
                ;
            }
            return asteroid; // Don't double add an asteroid
        }
        Vector2 spawnLocation = getVectorInValidSpawnArea();

        asteroid.setPosition(spawnLocation);
        box2DWrapper.setObjectPhysics(asteroid);

        if(isFinite){
            finiteAsteroids.add(asteroid);
        }else {
            asteroids.add(asteroid);
        }

        return asteroid;
    }



    /**
     * Returns a random point within the non-visible play area
     * @return - Vector outside screen, bound by the spawn area size
     */
    private Vector2 getVectorInValidSpawnArea(){
        // Random radian value
        double betweenZeroAnd2PI = generalUtil.getRandomNumber(0d, 2d * Math.PI);

        // Scale up the radius of spawning to hide the spawning.
        // get x,y
        float x = ((asteroidSpawnZone.radius + camera.zoom) * (float) Math.sin(betweenZeroAnd2PI)) + asteroidSpawnZone.x;
        float y = ((asteroidSpawnZone.radius + camera.zoom) * (float) Math.cos(betweenZeroAnd2PI)) + asteroidSpawnZone.y;

        Vector2 position = new Vector2(x, y);
        return position;

        // TODO : Check for spawing on a tile dude wtf
    }

    /**
     * Checks if the limit of continuous asteroids has been met
     * @return
     */
    public boolean canSpawn(){
        return getContinuousAsteroidCount() < asteroidLimit;
    }

    /**
     * Gets the number of continuous asteroids in the manager
     * @return
     */
    public int getContinuousAsteroidCount() {
        return asteroids.size;
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
    public void deleteAllAsteroids(){
        // Go through the asteroids and remove their reference from the local list and the screen object list
        for(int i = 0; i < asteroids.size; i++){
            deleteMember(asteroids.get(i));
        }
        if(asteroids.size > 0){
            System.out.println("ERROR : removeAllAsteroids() did not clear asteroid array in AsteroidManager");
        }
    }



    /**
     * Returns array of asteroids
     *
     * @return - Array of asteroids
     */
    public Array<Asteroid> getAsteroids() {
        return asteroids;
    }


    public boolean isArcadeMode() {
        return arcadeMode;
    }

    public void setArcadeMode(boolean arcadeMode) {
        this.arcadeMode = arcadeMode;
    }

    public Circle getAsteroidSpawnZone() {
        return asteroidSpawnZone;
    }

    public Array<Asteroid> getFiniteAsteroids() {
        return finiteAsteroids;
    }

    /**
     * set the array of limited asteroids that can be diminished.
     * @param asteroids
     */
    public void setFiniteAsteroids(Array<Asteroid> asteroids) {
        finiteAsteroids = asteroids;
        if(finiteAsteroids != null){
            for(int i = 0; i < finiteAsteroids.size; i++){
                addAsteroid(finiteAsteroids.get(i), true);
            }
        }
    }

    public void setSpawner(AsteroidSpawner asteroidSpawner) {

    }

    public Array<Asteroid> getAllAsteroids() {
        Array<Asteroid> newList = new Array<>();
        newList.addAll(this.finiteAsteroids);
        newList.addAll(this.asteroids);

        return newList;
    }
}
