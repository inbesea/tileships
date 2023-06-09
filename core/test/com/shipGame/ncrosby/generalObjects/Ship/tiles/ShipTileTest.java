package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Vector2;
import junit.framework.TestCase;
import org.junit.Test;

public class ShipTileTest extends TestCase {

    @Test
    public void testWhenStandardTileTest(){
        ShipTile tile = new StandardTile(new Vector2(0,0));
        System.out.println("Here is the tile name : \n" +
                tile.getClass().getName());

    }

}