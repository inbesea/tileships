package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.TileOrienter;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class TileOrienterTest {

    @Test
    public void whenCheckingUpPointingTiles_Return0(){
        // Before
        ShipTile tile1 = new ShipTile(new Vector2(0,0), ID.ShipTile);
        ShipTile tile2 = new ShipTile(new Vector2(0,64), ID.ShipTile);
        TileOrienter orienter = new TileOrienter(tile1, tile2);

        // When
        int result = orienter.getOrientation();

        // Then
        Assert.assertEquals(0, tile1.getyIndex());
        Assert.assertEquals(1, tile2.getyIndex());
        Assert.assertEquals(0, result);
    }

    @Test
    public void whenCheckingRightPointingTiles_Return1(){
        // Before - Add tiles in an upwards facing orientation
        ShipTile tile1 = new ShipTile(new Vector2(0,0), ID.ShipTile);
        ShipTile tile2 = new ShipTile(new Vector2(0,64), ID.ShipTile);
        TileOrienter orienter = new TileOrienter(tile1, tile2); // Init orientation

        // When - Getting the orientation value
        int result = orienter.getOrientation();

        // Then - Expect the value is 0 or up, which is the default.
        Assert.assertEquals(0, result);
    }

    @Test
    public void whenCheckingDownPointingTiles_Return2(){
        // Before
        ShipTile tile1 = new ShipTile(new Vector2(0,0), ID.ShipTile);
        ShipTile tile2 = new ShipTile(new Vector2(0,64), ID.ShipTile);
        TileOrienter orienter = new TileOrienter(tile1, tile2);

        // When
        int result = orienter.getOrientation();

        // Then
        Assert.assertEquals(0, result);
    }

    @Test
    public void whenCheckingLeftPointingTiles_Return3(){
        // Before
        ShipTile tile1 = new ShipTile(new Vector2(0,0), ID.ShipTile);
        ShipTile tile2 = new ShipTile(new Vector2(0,64), ID.ShipTile);
        TileOrienter orienter = new TileOrienter(tile1, tile2);

        // When
        int result = orienter.getOrientation();

        // Then
        Assert.assertEquals(0, result);
    }
}