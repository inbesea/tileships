package com.shipGame.ncrosby.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Asteroid;
import com.shipGame.ncrosby.screens.GameScreen;

import java.util.Arrays;

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
//        initSpawn();
    }

    public AsteroidManager(GameScreen screen, boolean spawning){
        this.screen = screen;
        this.spawning = spawning;
        asteroidLimit = 30;
        numberOfAsteroids = 0;
//        initSpawn();
    }

    /**
     * Call in constructor to populate the screen with asteroids
     */
    private void initSpawn(){
        // Need an x,y where x,y = x,y of camera - (0.5 * the width of the viewport) and + the same with the height (Y goes up)
        // The width and  height can grow as the x,y adjust.
        // This is only a little complex because the x will shrink while the y grows as the box grows, and vice versa when shrinking.
//        zoneOfPlay = new Rectangle(screen.getCamera().position.x, screen.getCamera().position.y);
        // If initalized while set to false init will not happen.
        if(spawning){
             while(canSpawn()){
              spawnAsteroid();
             }
        }
    }

    /**
     * Method to tick to check for asteroid cleanups and additions
     */
    public void checkForSpawn(){
        cleanup();
        while(canSpawn()){
            spawnAsteroid();
        }
        System.out.println("Asteroid 1 : " + asteroids.get(0).getX() + ", " + asteroids.get(0).getY());
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
            if(outOfBounds(asteroid)){
                System.out.println("Removing Out of bounds! : " + asteroid.getX() +  ", " + asteroid.getY());
                asteroids.removeValue(asteroid,true);
                screen.removeGameObject(asteroid);
                numberOfAsteroids = asteroids.size;
                System.out.println("numberOfAsteroids " + numberOfAsteroids);
            }
        }
    }

    /**
     * Returns true if asteroid instance has larger abs x,y than allowed.
     *
     * @param asteroid - asteroid instance to check
     * @return - true if x,y bigger than bounds
     */
    private boolean outOfBounds(Asteroid asteroid) {
        boolean isOutOfValidArea = (Math.abs(asteroid.getX()) > GameScreen.spawnAreaMax + screen.getCamera().viewportWidth)
                ||
                Math.abs(asteroid.getY()) > GameScreen.spawnAreaMax + screen.getCamera().viewportHeight;
        return isOutOfValidArea;
    }

    public void spawnAsteroid(){

        // Check if active
        if(spawning){
            Vector2 spawnLocation = getVectorInValidSpawnArea();

            Asteroid asteroid = new Asteroid(spawnLocation, new Vector2(64,64), ID.Asteroid);
            screen.newGameObject(asteroid);
            asteroids.add(asteroid);
            numberOfAsteroids = asteroids.size;
        }
//        System.out.println("Spawned Asteroid : asteroids.size " + asteroids.size);
    }

    /**
     * Returns a random point within the non-visible play area
     * @return - Vector outside screen, bound by the spawn area size
     */
    private Vector2 getVectorInValidSpawnArea(){
        ExtendViewport ev =  screen.getExtendViewport();

        int screenWidthHalf = (int) (ev.getScreenWidth()* 0.7f);
        int screenHightHalf = (int) (ev.getScreenHeight()* 0.7f);

        // Bad fix for init spawn when worldsize is 0 :/
        if (screenHightHalf == 0) screenWidthHalf = 400;
        if (screenWidthHalf == 0) screenWidthHalf = 400;

        // Get x,y centered on screen, and scaled up to provide band of spawning
        float x = getRandomlyNegativeNumber(screenWidthHalf , screenWidthHalf + GameScreen.spawnAreaMax); // Add to scale with ViewPort
        float y = getRandomlyNegativeNumber(screenHightHalf, screenHightHalf + GameScreen.spawnAreaMax);

        if (outOfBounds(new Asteroid(new Vector2(x,y), new Vector2(64,64), ID.Asteroid))){
            System.out.println("Creating Asteroid out of bounds lol");
        }

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
