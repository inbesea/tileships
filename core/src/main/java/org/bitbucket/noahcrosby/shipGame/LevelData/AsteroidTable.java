package org.bitbucket.noahcrosby.shipGame.LevelData;

import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.Asteroid;
import org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris.MetalAsteroid;
import org.bitbucket.noahcrosby.shipGame.util.generalUtil;
import java.util.Map;

public class AsteroidTable {
    // AMOUNTS OF ASTEROIDS
    public static final int Some = generalUtil.getRandomNumber(15, 20);

    // ROLL TABLES

//    public static final Map<Class, Integer> EntryNodeStd = {null->};
    public static final Map<Class, Integer> EntryNodeStd(){
        Map<Class, Integer> map = new java.util.HashMap<>();
        map.put(MetalAsteroid.class, 50);
        map.put(Asteroid.class, 50);
        return map;
    }
}

