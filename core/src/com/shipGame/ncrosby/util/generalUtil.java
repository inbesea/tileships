package com.shipGame.ncrosby.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.GameObject;

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
    public static Vector3 returnUnprojectedMousePosition(OrthographicCamera camera){
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
}
