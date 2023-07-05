package com.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class generalUtilTest extends TestCase {

    @Test
    public void testWhenEast_return1(){
        Vector2 first = new Vector2(0,0);
        Vector2 second = new Vector2(1.5f,0);
        // When
        int quadrant = generalUtil.getQuadrant(first,second);

        // Then
        Assert.assertEquals(1, quadrant);
    }

    @Test
    public void testWhenNorth_return0(){
        Vector2 first = new Vector2(0,0);
        Vector2 second = new Vector2(0,1.5f);
        // When
        int quadrant = generalUtil.getQuadrant(first,second);

        // Then
        Assert.assertEquals(0, quadrant);
    }

    @Test
    public void testWhenSouth_return2(){
        Vector2 first = new Vector2(0,0);
        Vector2 second = new Vector2(0,-1.5f);
        // When
        int quadrant = generalUtil.getQuadrant(first,second);

        // Then
        Assert.assertEquals(2, quadrant);
    }

    @Test
    public void testWhenWest_return3(){
        Vector2 first = new Vector2(0,0);
        Vector2 second = new Vector2(-1.5f,0);
        // When
        int quadrant = generalUtil.getQuadrant(first,second);

        // Then
        Assert.assertEquals(3, quadrant);
    }

    @Test
    public void testWhenTwoNegativeVectors_ReturnCorrectClosestVector(){
        Array<Vector2> vector2Array = new Array<>();
//        vector2Array.add(new Vector2(), new Vector2(-1.0));
    }
}