package com.shipGame.ncrosby;

public class Camera{

    public int x;
    public int y;

    /**
     * Moves context of game. Needs to be passed to objects that render and move.
     *
     * @param x
     * @param y
     */
    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
