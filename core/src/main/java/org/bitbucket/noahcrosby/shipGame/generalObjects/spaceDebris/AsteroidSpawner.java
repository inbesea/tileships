package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.util.ObjectRoller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Called to produce asteroids at a specific location, at a specific amount.
     * @param objectsAndPercentages
     * @param count
     * @return
     */
    public static Array<Asteroid> generateRandomAsteroidsStatic(Map<Class, Integer> objectsAndPercentages, Integer count){
        Array<Asteroid> generatedObjects = new Array<>();
        List<Class<?>> classes = new ArrayList<>();
        List<Integer> percentages = new ArrayList<>();

//        System.out.println(objectsAndPercentages.);
        for (Map.Entry<Class, Integer> entry : objectsAndPercentages.entrySet()) {
            classes.add(entry.getKey());
            percentages.add(entry.getValue());
        }

        for (int i = 0; i < count; i++) {
            generatedObjects.add((Asteroid) generateRandomObjectStatic(classes, percentages));
        }

        return generatedObjects;
    }

    // generate Asteroids for nodes returns node for more? We would like the nodes to generally
}
