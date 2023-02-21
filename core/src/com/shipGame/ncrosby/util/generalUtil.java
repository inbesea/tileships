package com.shipGame.ncrosby.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.GameObject;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.screens.GameScreen;
import com.shipGame.ncrosby.tileShipGame;

public class generalUtil {

    /**
     *  Checks for out of bounds amounts and returns to inbounds
     *
     *  @param var - Variable to check
     *  @param min - value to return when var is smaller than min
     *  @param max - value to return when var is larger than max
     */
    public static int clamp(int var, int min, int max) {
        if (var >= max)
            return max;
        else return Math.max(var, min);
    }

    /**
     *
     * Renders a texture at normalized x,y
     *
     * @param gameObject - object to hold all data needed to render a thing.
     */
    public static void render(GameObject gameObject, OrthographicCamera camera){
        Vector3 vector = new Vector3(gameObject.getX(), gameObject.getY(), 0);
        camera.unproject(vector);

        // Restrictions, need a reference to the game context to render.
        // game.batch.draw begin and all that.
        // Need the normalized x ,y  and the texture.
    }

    /**
     * Handles getting mouse position in terms of the camera.
     * Unprojects the mouse position as a Vector3
     *
     * @param camera - Orthographic camera for context
     * @return - Vector3 of unprojected mouse position based on Gdx.input.getX/Y();
     */
    public static Vector3 returnUnprojectedPosition(OrthographicCamera camera){
        Vector3 position = new Vector3();
        position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(position);
        return position;
    }

    /**
     * Method to find the closest tile to a point given an Array of ShipTiles
     *
     * @param location - Vector to check prox. to
     * @param vectors - Array of Vectors to search in
     * @return - closest tile
     */
    public static Vector2 closestVector2(Vector2 location , Array<Vector2> vectors) {
        if(vectors.size == 0)return null;
        if(vectors.size == 1)return vectors.get(0);

        double minDistance = Double.POSITIVE_INFINITY; // First check will always be true
        Vector2 tempV;
        Vector2 closestVector = null;
        Vector3 location3 = new Vector3(location.x, location.y, 0);
        Vector3 position;

        //Loop through ship to find closest tile
        for (int i = 0 ; i < vectors.size ; i++){
            tempV = vectors.get(i);
            position = new Vector3(tempV.x, tempV.y , 0);
            float distance;
            distance = location3.dst(position);

            // Check if distance between position and current tile is shorter
            if(distance < minDistance){
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
    public static float getRandomNumber(int min, int max) {
        return (float) ((Math.random() * (max - min)) + min);
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
     * Returns float within range  with randomly determined positivity/negativity.
     *
     * NOTE : CAN be given negative values, but will cause possible output space to just flip instead of forming a hole
     *
     * @param min - upper bounds in absolute value terms
     * @param max - lower bounds in absolute value terms
     * @return
     */
    public static float getRandomlyNegativeNumber(float min, float max){
        float value = getRandomNumber(min, max);
        float possiblyNegativeValue = value * getNegativeOneRandomly();
        return possiblyNegativeValue;
    }

    /**
     * Returns a negative one or positive one with a 50/50  chance
     *
     * @return int of -1 or 1 value
     */
    public static int getNegativeOneRandomly(){
        double random = Math.random();
        if(random < 0.5f)return -1;
        else return 1;
    }

    /**
     * Returns true if circle intersects the rectangle
     * Works by expanding the rectangle by the circles diameter and checking to see if the circle is within that larger rectangle
     * @param circle
     * @param rectangle
     * @return
     */
    public static boolean circleIntersectsRectangle(Circle circle, Rectangle rectangle){
        Rectangle bigger = new Rectangle(rectangle.x - (circle.radius*2), rectangle.y - (circle.radius*2),
                rectangle.width + (circle.radius*4), rectangle.height + (circle.radius*4));
//        game.batch.begin();
//        screen.g
        if (bigger.contains(circle)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Util to reverse general array with Generic contents.
     * @param array
     * @return
     */
    public static <T> Array<T> reverseArray(Array<T> array){
        Array<T> result = new Array<T>();
        for(int i = array.size - 1 ; i >= 0 ; i--){
            result.add(array.get(i));
        }
        return result;
    }

    /**
     * Returns what quadrant the boat is in relative to the anchor.
     *
     * @param anchor - The origin of the calculation
     * @param boat - The point to find the quadrant of
     * @return - An int representing a quadrant:
     * 0, 1 ,2 ,3 == North, East, South, West respectively.
     */
    public static int getQuadrant(Vector2 anchor, Vector2 boat){
        float closeX = anchor.x;
        float closeY = anchor.y;

        // Should return the difference between the placed position and middle of the close tile.
        float normalX =
                boat.x -
                        (closeX + (ShipTile.TILESIZE/2.0f));
        float normalY =
                boat.y -
                        (closeY + (ShipTile.TILESIZE/2.0f));

        // Set vector such that the center of tile is equal to (0,0) and mouse position is a point relative to that
        Vector2 normalizedMousePosition = new Vector2(normalX,normalY); // Find center of block by adding to the x,y

        // the point is above y = x if the y is larger than x
        boolean abovexEy = normalizedMousePosition.y > normalizedMousePosition.x;
        // the point is above y = -x if the y is larger than the negation of x
        boolean aboveNxEy = normalizedMousePosition.y > (-normalizedMousePosition.x);

        // We can conceptualize this as as a four triangles converging in the center of the "closest tile"
        // We can use this framing to decide the side to place the tile.
        if(abovexEy){ // Check at halfway point of tile
            if(aboveNxEy){ // North = 0
                return  0;
            } else { // West = 3
                return 3;
            }
        } else { // location is to the right of the closest tile
            if(aboveNxEy){ // East = 1
                return 1;
            } else { // South = 2
                return 2;
            }
        }
    }
}
