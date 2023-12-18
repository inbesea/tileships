package org.bitbucket.noahcrosby.shipGame.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Can be given object types to generate new objects from based on probabilities.
 */
public class ObjectRoller {

    public class RandomObjectGenerator {

        private List<Class<?>> classes;
        private List<Integer> percentages;

        public RandomObjectGenerator() {
            classes = new ArrayList<>();
            percentages = new ArrayList<>();
        }

        public void addClass(Class<?> clazz, int percentage) {
            classes.add(clazz);
            percentages.add(percentage);
        }

        public Object generateRandomObject() throws IllegalAccessException, InstantiationException {
            if (classes.isEmpty() || percentages.isEmpty() || classes.size() != percentages.size()) {
                throw new IllegalStateException("Invalid configuration");
            }

            int totalPercentage = percentages.stream().mapToInt(Integer::intValue).sum();
            int randomValue = new Random().nextInt(totalPercentage);

            int cumulativePercentage = 0;
            for (int i = 0; i < classes.size(); i++) {
                cumulativePercentage += percentages.get(i);
                if (randomValue < cumulativePercentage) {
                    return classes.get(i).newInstance();
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


    public ObjectRoller(){

    }
}
