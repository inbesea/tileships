package org.bitbucket.noahcrosby.shipGame.LevelData;

import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.MetalAsteroid;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;
import java.util.Map;

public class AsteroidTable {
    // AMOUNTS OF ASTEROIDS
    public static final int NONE = 0;
    public static Integer LESS() {
        return generalUtil.getRandomNumber(1, 6);
    }
    public static Integer FEW() {
        return generalUtil.getRandomNumber(6, 15);
    }
    public static Integer SOME() {
        return generalUtil.getRandomNumber(15, 20);
    }
    public static Integer MORE() {
        return generalUtil.getRandomNumber(20, 35);
    }
    public static Integer MOST() {
        return generalUtil.getRandomNumber(35, 50);
    }

    // ROLL TABLES
    // Maps of space junk to spawn with percentages
    public static final Map<Class, Integer> EntryNodeStd(){
        Map<Class, Integer> map = new java.util.HashMap<>();
        map.put(MetalAsteroid.class, 50);
        map.put(Asteroid.class, 50);
        return map;
    }
}

