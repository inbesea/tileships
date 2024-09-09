package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class generalUtil {

    /**
     * Handles getting mouse position in terms of the camera.
     * Unprojects the mouse position as a Vector3
     *
     * @param camera - Orthographic camera for context
     * @return - Vector3 of unprojected mouse position based on Gdx.input.getX/Y();
     */
    public static Vector3 returnUnprojectedInputPosition(OrthographicCamera camera) {
        Vector3 position = new Vector3();
        position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(position);
        return position;
    }

    /**
     * Unprojects the mouse position as a Vector3
     *
     * @param camera - Orthographic camera for context
     * @return - Vector3 of unprojected mouse position based on Gdx.input.getX/Y();
     */
    public static Vector2 returnUnprojectedInputVector2(OrthographicCamera camera) {
        Vector3 position = new Vector3();
        position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(position);
        return flattenVector(position);
    }

    /**
     * Method to find the closest tile to a point given an Array of ShipTiles
     *
     * @param location - Vector to check prox. to
     * @param vectors  - Array of Vectors to search in
     * @return - closest tile
     */
    public static Vector2 closestVector2(Vector2 location, Array<Vector2> vectors) {
        if (vectors.size == 0) return null;
        if (vectors.size == 1) return vectors.get(0);

        double minDistance = Double.POSITIVE_INFINITY; // First check will always be true
        Vector2 tempV;
        Vector2 closestVector = null;
        Vector3 location3 = new Vector3(location.x, location.y, 0);
        Vector3 position;

        //Loop through ship to find the closest tile
        for (int i = 0; i < vectors.size; i++) {
            tempV = vectors.get(i);
            position = new Vector3(tempV.x, tempV.y, 0);
            float distance;
            distance = location3.dst(position);

            // Check if distance between position and current tile is shorter
            if (distance < minDistance) {
                minDistance = distance;
                closestVector = tempV;
            }
        }
        return closestVector;
    }

    /**
     * Creates a random value between min and max
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Creates a random value between min and max
     *
     * @param min
     * @param max
     * @return
     */
    public static float getRandomNumber(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

    /**
     * Creates a random value between min and max
     *
     * @param min
     * @param max
     * @return
     */
    public static double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    /**
     * Creates a random value between min and max
     *
     * @param min
     * @param max
     * @return
     */
    public static long getRandomNumber(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }

    /**
     * Returns float within range  with randomly determined positivity/negativity.
     * <p>
     * NOTE : CAN be given negative values, but will cause possible output space to just flip instead of forming a hole
     *
     * @param min - upper bounds in absolute value terms
     * @param max - lower bounds in absolute value terms
     * @return
     */
    public static float getRandomlyNegativeNumber(float min, float max) {
        float value = getRandomNumber(min, max);
        float possiblyNegativeValue = value * getNegativeOneRandomly();
        return possiblyNegativeValue;
    }

    /**
     * Returns a negative one or positive one with a 50/50  chance
     *
     * @return int of -1 or 1 value
     */
    public static int getNegativeOneRandomly() {
        double random = Math.random();
        if (random < 0.5f) return -1;
        else return 1;
    }

    /**
     * Returns true if circle intersects the rectangle
     * Works by expanding the rectangle by the circles diameter and checking to see if the circle is within that larger rectangle
     *
     * @param circle
     * @param rectangle
     * @return
     */
    public static boolean circleIntersectsRectangle(Circle circle, Rectangle rectangle) {
        Rectangle bigger = new Rectangle(rectangle.x - (circle.radius * 2), rectangle.y - (circle.radius * 2),
            rectangle.width + (circle.radius * 4), rectangle.height + (circle.radius * 4));
//        game.batch.begin();
//        screen.g
        if (bigger.contains(circle)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Util to reverse general array with Generic contents.
     *
     * @param array
     * @return
     */
    public static <T> Array<T> reverseArray(Array<T> array) {
        Array<T> result = new Array<T>();
        for (int i = array.size - 1; i >= 0; i--) {
            result.add(array.get(i));
        }
        return result;
    }

    /**
     * Returns a quadrant of a point relative to an origin on a 2D plane
     *
     * @param orgn - The origin of the calculation
     * @param pt   - The point to find the quadrant of
     * @return - An int representing a quadrant:
     * 0, 1 ,2 ,3 == North, East, South, West respectively.
     */
    public static int getQuadrant(Vector2 orgn, Vector2 pt) {

        // Should return the difference between the placed position and middle of the close tile.
        float normalPtX =
            pt.x -
                (orgn.x);// Divide here to get center of tile for comparison
        float normalPtY =
            pt.y -
                (orgn.y);

        // the point is above y = x if the y is larger than x
        boolean abovexEy = normalPtY > normalPtX;
        // the point is above y = -x if the y is larger than the negation of x
        boolean aboveNxEy = normalPtY > -normalPtX;

        // We can conceptualize this as four triangles converging in the center of the "closest tile"
        // We can use this framing to decide the side to place the tile.
        if (abovexEy) { // Check at halfway point of tile
            if (aboveNxEy) { // North = 0
                return 0;
            } else { // West = 3
                return 3;
            }
        } else { // location is to the right of the closest tile
            if (aboveNxEy) { // East = 1
                return 1;
            } else { // South = 2
                return 2;
            }
        }
    }

    /**
     * Creates new dynamic body definition using the passed location values.
     * @param x - x location
     * @param y - y location
     * @return - Dynamic BodyDef
     */
    public static BodyDef newDynamicBodyDef(float x, float y) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(x, y);

        return bodyDef;
    }

    public static BodyDef newStaticBodyDef(float x, float y) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set our body's starting position in the world
        bodyDef.position.set(x, y);

        return bodyDef;
    }


    /**
     * Gets a point on a line
     *
     * @param p1 - starting point
     * @param p2 - ending point
     * @param t  - how far along the line (0 to 1)
     * @return - point on the line as Vector2
     */
    public static Vector2 getPointOnLine(Vector2 p1, Vector2 p2, float t) {
        return p1.add(p2.sub(p1).scl(t));
    }

    /**
     * Returns a scalar that scales the line to intersect the passed limit
     * The passed line will intersect a horizontal or vertical line drawn from the limit
     *
     * @param p1    - starting point
     * @param p2    - ending point
     * @param limit - limit point
     * @return - scalar (can be negative)
     */
    public static float findLineScalarToGoal(Vector2 p1, Vector2 p2, Vector2 limit) {
        float tX;
        float tY;

        tX = (limit.x - p1.x) / (p2.x - p1.x);
        tY = (limit.y - p1.y) / (p2.y - p1.y);
        return Math.abs(tX) < Math.abs(tY) ? tX : tY;
    }

    /**
     * Returns new instance of Vector2, dropping the z dimension.
     *
     * @param playerControlPosition
     * @return
     */
    public static Vector2 flattenVector(Vector3 playerControlPosition) {
        return new Vector2(playerControlPosition.x, playerControlPosition.y);
    }

    /**
     * Returns the closest object extending GameObject in the array passed.
     *
     * @param position - Position to compare array positions with
     * @param array    - array of generic GameObjects utilizing a positional Vector2
     * @param <T>      - A generic object bounded to extend GameObject
     * @return - @Nullable returns the closest node if there is one.
     */
    public static <T extends GameObject> T getClosestObject(Vector2 position, Array<T> array) {
        if (array.size == 0) return null;
        if (array.size == 1) return array.get(0);

        T tempObj;
        T closestObject = null;
        Vector3 location3 = new Vector3(position.x, position.y, 0);
        Vector3 objectMiddle;
        double minDistance = Double.POSITIVE_INFINITY; // First check will always be true


        //Loop through ship to find the closest tile
        for (int i = 0; i < array.size; i++) {
            tempObj = array.get(i);
            // Get middle of tile to check distance
            objectMiddle = new Vector3(tempObj.getPosition().x + tempObj.getSize().x / 2.0f,
                tempObj.getPosition().y + tempObj.getSize().y / 2.0f, 0);
            Float distance = location3.dst(objectMiddle);

            // Check if distance between position and current tile is shorter
            if (distance < minDistance) {
                minDistance = distance;
                closestObject = tempObj;
            }
        }
        return closestObject;
    }

    /**
     * Clamps an integer value between a minimum and maximum value.
     *
     * @param value the integer value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped integer value
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static Vector2 moveTowardsPoint(Vector2 currentPosition,
                                           Vector2 targetPoint, float speed,
                                           float deltaTime) {
        float dx = targetPoint.x - currentPosition.x;
        float dy = targetPoint.y - currentPosition.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            return currentPosition;
        }

        float normalizedDirectionX = dx / distance;
        float normalizedDirectionY = dy / distance;
        float displacementX = normalizedDirectionX * (speed * deltaTime);
        float displacementY = normalizedDirectionY * (speed * deltaTime);
        float newX = currentPosition.x + displacementX;
        float newY = currentPosition.y + displacementY;

        return new Vector2(newX, newY);

    }

    public static Double clamp(Double value, Double max, Double min) {

        if (value > max) value = max;
        if (value < min) value = min;
        return value;
    }

    public static float round(float d, int decimalPlace)
    {
        return BigDecimal.valueOf(d).setScale(decimalPlace, RoundingMode.HALF_UP ).floatValue();
    }
}
