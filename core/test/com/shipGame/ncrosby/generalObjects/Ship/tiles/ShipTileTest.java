package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.StandardTile;
import junit.framework.TestCase;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ShipTileTest extends TestCase {

    @Test
    public void testWhenStandardTileTest(){
        Ship ship = mock(Ship.class);
        ShipTile tile = new StandardTile(new Vector2(0,0), ship.getTileManager());
        System.out.println("Here is the tile name : \n" +
                tile.getClass().getName());

    }

}