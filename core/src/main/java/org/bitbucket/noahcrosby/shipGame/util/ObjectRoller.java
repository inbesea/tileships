package org.bitbucket.noahcrosby.shipGame.util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Can be given object types to generate new objects from based on probabilities.
 */
public class ObjectRoller {

    private List<Class<?>> classes;
    private List<Integer> percentages;

    public ObjectRoller() {
        classes = new ArrayList<>();
        percentages = new ArrayList<>();
    }

    public void addClass(Class<?> clazz, int percentage) {
        classes.add(clazz);
        percentages.add(percentage);
    }

    /**
     * Generates an array of objects based on the given map of objects and percentages
     * @param objectsAndPercentages - a map of objects and their percentages
     * @param count - the number of objects to generate
     * @return an array of objects
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Array<Object> generateRandomObjects(Map<Object, Integer> objectsAndPercentages, Integer count) throws IllegalAccessException {
            reset();

            Array<Object> generatedObjects = new Array<>();

            for (Map.Entry<Object, Integer> entry : objectsAndPercentages.entrySet()) {
                classes.add(entry.getKey().getClass());
                percentages.add(entry.getValue());
            }

            for (int i = 0; i < count; i++) {
                try {
                    generatedObjects.add(generateRandomObject());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            return generatedObjects;
    }

    public static Array<Object> generateRandomObjectsStatic(Map<Object, Integer> objectsAndPercentages, Integer count) {
        Array<Object> generatedObjects = new Array<>();
        List<Class<?>> classes = new ArrayList<>();
        List<Integer> percentages = new ArrayList<>();

        for (Map.Entry<Object, Integer> entry : objectsAndPercentages.entrySet()) {
            classes.add(entry.getKey().getClass());
            percentages.add(entry.getValue());
        }

        for (int i = 0; i < count; i++) {
            generatedObjects.add(generateRandomObjectStatic(classes, percentages));
        }

        return generatedObjects;
    }

    public static Object generateRandomObjectStatic(List<Class<?>> classes, List<Integer> percentages) {
        if (classes.isEmpty() || percentages.isEmpty() || classes.size() != percentages.size()) {
            throw new IllegalStateException("Invalid configuration");
        }

        //
        int totalPercentage = percentages.stream().mapToInt(Integer::intValue).sum();
        int randomValue = new Random().nextInt(totalPercentage);

        // Scales percentage up to summed value of all percentages
        int cumulativePercentage = 0;
        for (int i = 0; i < classes.size(); i++) { // Its stupid, but I don't like that we iterate through a list and add. That doesn't scale. :(
            cumulativePercentage += percentages.get(i);
            if (randomValue < cumulativePercentage) {
                try {
                    Class<?> clazz = classes.get(i);
//                    Object object1 = clazz.
                    Constructor constructor = ClassReflection.getDeclaredConstructor(
                        clazz
                    );
                        Object object = constructor.newInstance();
                    return object;
                } catch (Exception e) {
                    Gdx.app.error("ObjectRoller", "Failed to generate a random object!! \n" + e.getMessage());
                    return null;
                }
            }
        }

        throw new IllegalStateException("Failed to generate a random object");
    }

    /**
     * Set the configuration of the roller from a map
     * Note : resets the roller
     * @param objectsAndPercentages - a map of objects and their percentages
     */
    public void setValues(Map<Object, Integer> objectsAndPercentages) {
        reset();

        for (Map.Entry<Object, Integer> entry : objectsAndPercentages.entrySet()) {
                classes.add(entry.getKey().getClass());
                percentages.add(entry.getValue());
            }
    }

    public void reset() {
            classes.clear();
            percentages.clear();
        }

        public Object generateRandomObject() throws IllegalAccessException {
            if (classes.isEmpty() || percentages.isEmpty() || classes.size() != percentages.size()) {
                throw new IllegalStateException("Invalid configuration");
            }

            //
            int totalPercentage = percentages.stream().mapToInt(Integer::intValue).sum();
            int randomValue = new Random().nextInt(totalPercentage);

            // Scales percentage up to summed value of all percentages
            int cumulativePercentage = 0;
            for (int i = 0; i < classes.size(); i++) { // Its stupid, but I don't like that we iterate through a list and add. That doesn't scale. :(
                cumulativePercentage += percentages.get(i);
                if (randomValue < cumulativePercentage) {
                    try{
                        return ClassReflection.newInstance(classes.get(i));
                    } catch (ReflectionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            throw new IllegalStateException("Failed to generate a random object");
        }

        /* Example use:
        public static void main(String[] args) {
            RandomObjectGenerator generator = new RandomObjectGenerator();

            // Add classes and their respective percentages (percentages don't have to sum to 100)
            generator.addClass(String.class, 30);
            generator.addClass(Integer.class, 40);
            generator.addClass(Double.class, 15);
            generator.addClass(Boolean.class, 15);

            // Generate random objects
            for (int i = 0; i < 10; i++) {
                try {
                    Object randomObject = generator.generateRandomObject();
                    System.out.println("Generated object: " + randomObject);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }*/
}
