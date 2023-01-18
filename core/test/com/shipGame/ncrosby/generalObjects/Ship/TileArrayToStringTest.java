package com.shipGame.ncrosby.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.ncrosby.ID;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.CoreTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.ShipTile;
import com.shipGame.ncrosby.generalObjects.Ship.tiles.StandardTile;
import junit.framework.TestCase;
import org.junit.Test;

public class TileArrayToStringTest extends TestCase {

    @Test
    public void testWhenGiveSTD0STD1STDTileSetToStringBuilder_GetSTD0STD1STDStringBack(){
        // Before
        Array<ShipTile> tiles = new Array<>();
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0));
        StandardTile standardTile2 = new StandardTile(new Vector2(0,64));
        StandardTile standardTile3 = new StandardTile(new Vector2(64,64));
        // Set neighbors manually
        standardTile1.setNeighbors(standardTile2,null,null,null);
        standardTile2.setNeighbors(null,standardTile3,null,null);
        // Add tiles to array for passing to stringifyer
        tiles.add(standardTile1,standardTile2, standardTile3);
        TileArrayToString tileArrayToString = new TileArrayToString(tiles);

        // When
        String result = tileArrayToString.tilesToString();

        // Then
        assertEquals("STD0STD1STD", result);
    }

    /**
     * Testing if given a right facing array STD AdjacentTiles.RIGHT STD AT.DOWN then compare string conversion should
     * turn the orientation.
     */
    @Test
    public void testWhenGiveSTD1STD2STDToCompareString_GetSTD0STD1STDStringBack(){
        // Before
        Array<ShipTile> tiles = new Array<>();
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0));
        StandardTile standardTile2 = new StandardTile(new Vector2(64,0));
        StandardTile standardTile3 = new StandardTile(new Vector2(64,-64));
        // Set neighbors manually
        standardTile1.setNeighbors(null,standardTile2,null,null);
        standardTile2.setNeighbors(null,null ,standardTile3,null);
        // Add tiles to array for passing to stringifyer
        tiles.add(standardTile1,standardTile2, standardTile3);
        TileArrayToString tileArrayToString = new TileArrayToString(tiles);

        // When
        String result = tileArrayToString.toCompareString();

        // Then
        assertEquals("STD0STD1STD", result);
    }

    /**
     * Testing if given a left facing array STD AdjacentTiles.RIGHT STD AT.DOWN then compare string conversion should
     * turn the orientation.
     */
    @Test
    public void testWhenGiveSTD3COR0STDToCompareString_GetSTD0COR1STDStringBack(){
        // Before
        Array<ShipTile> tiles = new Array<>();
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0));
        CoreTile coreTile1 = new CoreTile(new Vector2(-64,0));
        StandardTile standardTile3 = new StandardTile(new Vector2(-64,64));
        // Set neighbors manually
        standardTile1.setNeighbors(null,null,null, coreTile1);
        coreTile1.setNeighbors(standardTile3,null,null ,null);
        // Add tiles to array for passing to stringifyer
        tiles.add(standardTile1,coreTile1, standardTile3);
        TileArrayToString tileArrayToString = new TileArrayToString(tiles);

        // When
        String result = tileArrayToString.toCompareString();

        // Then
        assertEquals("STD0COR1STD", result);
    }
}