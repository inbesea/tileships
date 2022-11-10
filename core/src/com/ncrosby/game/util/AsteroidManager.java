package com.ncrosby.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.ncrosby.game.BasicEnemy;
import com.ncrosby.game.screens.GameScreen;

import java.util.Arrays;

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
    private int numberOfAsteroids;
    private int asteroidLimit;
    Rectangle zoneOfPlay;
    // Keep asteroid references for simplicity
    private Array<BasicEnemy> asteroids;
    public AsteroidManager(GameScreen screen){
        this.screen = screen;
        asteroidLimit = 30;
        numberOfAsteroids = 0;
        initSpawn();
    }

    public AsteroidManager(GameScreen screen, boolean spawning){
        this.screen = screen;
        this.spawning = spawning;
        asteroidLimit = 30;
        numberOfAsteroids = 0;
        initSpawn();
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
    }

    /**
     * Check asteroids and remove any outside of the "zoneOfPlay" - Cleaning up the asteroids that stray too far
     * @return - marking this as unfinished
     */
    private void cleanup() {
        // Check asteroids and remove any outside of the "zoneOfPlay"
        // how to handle that... We could have that be an explicitly set value defining a box that removes asteroids
        // Or it could be a value that tells you how much space will be outside the viewport that can be used.

    }

    public void spawnAsteroid(){
        // Check if active
        if(spawning){

        }
        /*
         * > Spawn only outside the viewport to hide spawns.
         * > Give a random velocity
         * > cleanUpOutOfBounds()
         */
    }

    /**
     * Checks if the limit of asteroids has been met
     * @return
     */
    public boolean canSpawn(){
        return numberOfAsteroids > asteroidLimit;
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
            screen.removeGameObject(asteroids.removeIndex(i));
        }
        if(asteroids.size == 0) throw new RuntimeException("Did not remove all asteroids in removeAllAsteroids() : \n"
                + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
