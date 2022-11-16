package com.shipGame.ncrosby.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
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
            return var = max;
        else if (var <= min)
            return var = min;
        else
            return var;
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
}
