package com.ncrosby.game.util;

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
}
