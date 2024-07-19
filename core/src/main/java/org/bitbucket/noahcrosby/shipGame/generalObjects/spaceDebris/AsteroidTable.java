package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;
import java.util.Map;

public class AsteroidTable {
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
    public static final Map<Class, Integer> EntryNodeStd(){
        Map<Class, Integer> map = new java.util.HashMap<>();
        map.put(MetalAsteroid.class, 50);
        map.put(Asteroid.class, 50);
        map.put(WoodAsteroid.class, 50);
        map.put(GlassAsteroid.class, 50);
        return map;
    }
}

