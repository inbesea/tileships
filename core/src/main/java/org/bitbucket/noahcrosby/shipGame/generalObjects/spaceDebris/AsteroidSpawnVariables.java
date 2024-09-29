package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;
import java.util.Map;

public class AsteroidSpawnVariables {
    // AMOUNTS OF ASTEROIDS
    public static final int NONE = 0;

    /**
     * Generates between 1 and 6 asteroids
     * @return
     */
    public static Integer LESS() {
        return generalUtil.getRandomNumber(1, 6);
    }

    /**
     * Generates between 6 and 15 asteroids
     * @return
     */
    public static Integer FEW() {
        return generalUtil.getRandomNumber(6, 15);
    }

    /**
     * Generates between 15 and 20 asteroids
     * @return
     */
    public static Integer SOME() {
        return generalUtil.getRandomNumber(15, 20);
    }

    /**
     * Generates between 20 and 35 asteroids
     * @return
     */
    public static Integer MORE() {
        return generalUtil.getRandomNumber(20, 35);
    }

    /**
     * Generates between 35 and 50 asteroids
     * @return
     */
    public static Integer MOST() {
        return generalUtil.getRandomNumber(35, 50);
    }

    // ROLL TABLES
    // Maps of space junk to spawn with percentages

    /**
     * Spawns asteroids with 50% metal and 50% default
     * @return
     */
    public static final Map<Class, Integer> EntryPlanetStandard(){
        Map<Class, Integer> entryPlanet = new java.util.HashMap<>();
        entryPlanet.put(MetalAsteroid.class, 50);
        entryPlanet.put(Asteroid.class, 50);
        entryPlanet.put(WoodAsteroid.class, 50);
        entryPlanet.put(GlassAsteroid.class, 50);
        entryPlanet.put(SprayPaintAsteroid.class, 50);
        return entryPlanet;
    }
}

