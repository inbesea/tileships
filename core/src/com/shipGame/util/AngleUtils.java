package com.shipGame.util;

import static com.badlogic.gdx.math.MathUtils.radDeg;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class AngleUtils {

    public static float normalize(float angle) {
        return (angle % 360f) + (angle < 0f ? 360f : 0f);
    }

    /**
     * Clamps an angle between a min and a max value.*
     * @param angle
     * @param min
     * @param max
     * @return
     */
    public static float circularClamp(float angle, float min, float max) {
        float diff = Math.abs(min - max) / 2f;
        angle = angle < min ? (angle + diff < min ? max : min) : (angle > max ? (angle - diff > max ? min : max) : angle);
        return angle;
    }

    /**
     * Calculates the angle between two points and multiplies by radians.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float degreesBetweenPoints(float x1, float y1, float x2, float y2) {
        return radDeg * MathUtils.atan2((y1 - y2), (x1 - x2));
    }

    /**
     * Calculates the angle between two points and multiplies by radians.
     * @param pointA
     * @param pointB
     * @return
     */
    public static float degreesBetweenPoints(Vector2 pointA, Vector2 pointB) {
        return degreesBetweenPoints(pointA.x, pointA.y, pointB.x, pointB.y);
    }
}