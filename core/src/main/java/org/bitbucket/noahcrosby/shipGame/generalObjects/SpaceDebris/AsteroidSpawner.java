package org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.util.ObjectRoller;

public class AsteroidSpawner extends ObjectRoller {
    protected int limitedAsteroidGoal;
    protected int replaceAsteroidMinimum; // Keep asteroids here
    // protected
    public AsteroidSpawner(){

    }

    /**
     * Determines an asteroid from possibilities and returns an instance.
     * @return - Asteroid
     */
    public Asteroid generate(){
        Asteroid asteroid = null;
//        asteroid = new Asteroid(new Vector2(10,10), ID.Asteroid, null);
        return asteroid;
    }
}
