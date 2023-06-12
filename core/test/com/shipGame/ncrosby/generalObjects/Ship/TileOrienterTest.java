package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileManagers.AdjacentTiles;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileTypes.StandardTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.tileManagers.TileOrienter;
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
        // Adding two tiles one above the other
        tileArray.add(new StandardTile(new Vector2(0,0)),  new StandardTile(new Vector2(0,ShipTile.TILESIZE)));
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
        tileArray.add(new StandardTile(new Vector2(0,0)),  new StandardTile(new Vector2(ShipTile.TILESIZE,0)));
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
        tileArray.add(new StandardTile(new Vector2(0,0)),  new StandardTile(new Vector2(0,0 - ShipTile.TILESIZE)));
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
                new StandardTile(new Vector2(0,0)),  // First nutral tile
                new StandardTile(new Vector2(0 - ShipTile.TILESIZE,0))); // Second left tile
        orienter = new TileOrienter(tileArray);
        tile1 = tileArray.get(0);
        tile2 = tileArray.get(1);

        // When
        int result = orienter.calculateOrientation();

        // Then - Return 3 == left
        Assert.assertEquals( 3, result);
    }

    @Test
    public void testWhenOrientingNumbersUpFromRightFacingArray_GetAppropriateNumbers(){
        // Before - Add tiles in a right facing orientation
        tileArray = new Array<>();
        tileArray.add(new StandardTile(new Vector2(0,0)),  new StandardTile(new Vector2(0 + ShipTile.TILESIZE,0)));
        orienter = new TileOrienter(tileArray);

        // When - Getting the orientation value
        int result0 = orienter.directionRemap(AdjacentTiles.RIGHT, AdjacentTiles.UP);
        int result1 = orienter.directionRemap(AdjacentTiles.DOWN, AdjacentTiles.UP);
        int result2 = orienter.directionRemap(AdjacentTiles.LEFT, AdjacentTiles.UP);
        int result3 = orienter.directionRemap(AdjacentTiles.UP, AdjacentTiles.UP);

        // Then - Expect the value is 1 or right
        Assert.assertEquals(AdjacentTiles.UP, result0);
        Assert.assertEquals(AdjacentTiles.RIGHT, result1);
        Assert.assertEquals(AdjacentTiles.DOWN, result2);
        Assert.assertEquals(AdjacentTiles.LEFT, result3);
    }

    @Test
    public void testWhenOrientingNumbersUpFromLeftFacingArray_GetAppropriateNumbers(){
        // Before - Add tiles in a right facing orientation
        tileArray = new Array<>();
        tileArray.add(new StandardTile(new Vector2(0,0)),  new StandardTile(new Vector2(0 - ShipTile.TILESIZE,0)));
        orienter = new TileOrienter(tileArray); // Give left facing array to orienter

        // When - Getting the new reoriented values (facing up)
        int result0 = orienter.directionRemap(AdjacentTiles.RIGHT, AdjacentTiles.UP);
        int result1 = orienter.directionRemap(AdjacentTiles.DOWN, AdjacentTiles.UP);
        int result2 = orienter.directionRemap(AdjacentTiles.LEFT, AdjacentTiles.UP);
        int result3 = orienter.directionRemap(AdjacentTiles.UP, AdjacentTiles.UP);

        // Then - Expect the value is 1 or right
        Assert.assertEquals(AdjacentTiles.DOWN, result0);
        Assert.assertEquals(AdjacentTiles.LEFT, result1);
        Assert.assertEquals(AdjacentTiles.UP, result2);
        Assert.assertEquals(AdjacentTiles.RIGHT, result3);
    }

    @Test
    public void testWhenOrientingNumbersUpFromDownFacingArray_GetAppropriateNumbers(){
        // Before - Add tiles in a right facing orientation
        tileArray = new Array<>();
        tileArray.add(new StandardTile(new Vector2(0,0)),  new StandardTile(new Vector2(0,0 - ShipTile.TILESIZE)));
        orienter = new TileOrienter(tileArray); // Give left facing array to orienter

        // When - Getting the new reoriented values (facing up)
        int result0 = orienter.directionRemap(AdjacentTiles.RIGHT, AdjacentTiles.UP);
        int result1 = orienter.directionRemap(AdjacentTiles.DOWN, AdjacentTiles.UP);
        int result2 = orienter.directionRemap(AdjacentTiles.LEFT, AdjacentTiles.UP);
        int result3 = orienter.directionRemap(AdjacentTiles.UP, AdjacentTiles.UP);

        // Then - Expect the value is 1 or right
        Assert.assertEquals(AdjacentTiles.LEFT, result0);
        Assert.assertEquals(AdjacentTiles.UP, result1);
        Assert.assertEquals(AdjacentTiles.RIGHT, result2);
        Assert.assertEquals(AdjacentTiles.DOWN, result3);
    }
}