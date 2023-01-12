package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileOrienter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TileOrienterTest {

    TileOrienter orienter;
    Array<ShipTile> tileArray;
    ShipTile tile1;
    ShipTile tile2;

    @Before
    public void before(){
        tileArray = new Array<>();
    }


    @Test
    public void whenCheckingUpPointingTiles_Return0(){
        // Before - Give values to the objects to test
        tileArray = new Array<>();
        tileArray.add(new ShipTile(new Vector2(0,0), ID.StandardTile),  new ShipTile(new Vector2(0,64), ID.StandardTile));
        orienter = new TileOrienter(tileArray);
        tile1 = tileArray.get(0);
        tile2 = tileArray.get(1);

        // When - getting orientation
        int result = orienter.calculateOrientation();

        // Then
        Assert.assertEquals(0, result);
        Assert.assertEquals(0, tile1.getyIndex()); // Tile checks
        Assert.assertEquals(1, tile2.getyIndex());
    }

    @Test
    public void whenCheckingRightPointingTiles_Return1(){
        // Before - Add tiles in a right facing orientation
        tileArray = new Array<>();
        tileArray.add(new ShipTile(new Vector2(0,0), ID.StandardTile),  new ShipTile(new Vector2(64,0), ID.StandardTile));
        orienter = new TileOrienter(tileArray);
        tile1 = tileArray.get(0);
        tile2 = tileArray.get(1);

        // When - Getting the orientation value
        int result = orienter.calculateOrientation();

        // Then - Expect the value is 1 or right
        Assert.assertEquals(1, result);
        Assert.assertEquals(0, tile1.getxIndex()); // Tile checks
        Assert.assertEquals(1, tile2.getxIndex());
    }

    @Test
    public void whenCheckingDownPointingTiles_Return2(){
        // Before
        tileArray = new Array<>();
        tileArray.add(new ShipTile(new Vector2(0,0), ID.StandardTile),  new ShipTile(new Vector2(0,-64), ID.StandardTile));
        orienter = new TileOrienter(tileArray);
        tile1 = tileArray.get(0);
        tile2 = tileArray.get(1);

        // When
        int result = orienter.calculateOrientation();

        // Then - Assert 2 which is downwards.
        Assert.assertEquals(2, result);
    }

    @Test
    public void whenCheckingLeftPointingTiles_Return3(){
        // Before
        tileArray = new Array<>();
        tileArray.add(
                new ShipTile(new Vector2(0,0), ID.StandardTile),  // First nutral tile
                new ShipTile(new Vector2(-64,0), ID.StandardTile)); // Second left tile
        orienter = new TileOrienter(tileArray);
        tile1 = tileArray.get(0);
        tile2 = tileArray.get(1);

        // When
        int result = orienter.calculateOrientation();

        // Then - Return 3 == left
        Assert.assertEquals( 3, result);
    }
}