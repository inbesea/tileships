package com.shipGame.generalObjects.Ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.CoreTile;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.StandardTile;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class TileArrayToStringTest extends TestCase {

    Ship ship;
    ShipTilesManager manager;

    @Before
    public void before(){
        ship = mock(Ship.class);
        manager = ship.getTileManager();
    }

    @Test
    public void testWhenGiveSTD0STD1STDTileSetToStringBuilder_GetSTD0STD1STDStringBack(){
        // Before
        Array<ShipTile> tiles = new Array<>();
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0), manager);
        StandardTile standardTile2 = new StandardTile(new Vector2(0,64), manager);
        StandardTile standardTile3 = new StandardTile(new Vector2(64,64), manager);
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
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0), manager);
        StandardTile standardTile2 = new StandardTile(new Vector2(0 + ShipTile.TILESIZE,0), manager);
        StandardTile standardTile3 = new StandardTile(new Vector2(0 + ShipTile.TILESIZE,0 - ShipTile.TILESIZE), manager);
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
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0), manager);
        CoreTile coreTile1 = new CoreTile(new Vector2(-ShipTile.TILESIZE,0), manager);
        StandardTile standardTile3 = new StandardTile(new Vector2(-ShipTile.TILESIZE,ShipTile.TILESIZE), manager);
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

    @Test
    public void testWhenGiveSTD3COR0STDToReverseCompareString_GetSTD0COR3STDStringBack(){
        // Before
        Array<ShipTile> tiles = new Array<>();
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0), manager);
        CoreTile coreTile1 = new CoreTile(new Vector2(-ShipTile.TILESIZE,0), manager);
        StandardTile standardTile3 = new StandardTile(new Vector2(-ShipTile.TILESIZE,ShipTile.TILESIZE), manager);
        // Set neighbors manually
        standardTile1.setNeighbors(null,null,null, coreTile1);
        coreTile1.setNeighbors(standardTile3,standardTile1,null ,null);
        standardTile3.setNeighbors(null,null,coreTile1, null);
        // Add tiles to array for passing to stringifyer
        tiles.add(standardTile1,coreTile1, standardTile3);
        TileArrayToString tileArrayToString = new TileArrayToString(tiles);

        // When - Running reverse compare string
        String result = tileArrayToString.reverseToCompareString();

        // Then
        assertEquals("STD0COR3STD", result);
    }

    @Test
    public void testWhenGiveSTD3COR2STDToReverseCompareString_GetSTD0COR1STDStringBack(){
        // Before
        Array<ShipTile> tiles = new Array<>();
        StandardTile standardTile1 = new StandardTile(new Vector2(0,0), manager);
        CoreTile coreTile1 = new CoreTile(new Vector2(-ShipTile.TILESIZE,0), manager);
        StandardTile standardTile3 = new StandardTile(new Vector2(-ShipTile.TILESIZE,-ShipTile.TILESIZE), manager);
        // Set neighbors manually
        standardTile1.setNeighbors(null,null,null, coreTile1);
        coreTile1.setNeighbors(null,standardTile1, standardTile3 ,null);
        standardTile3.setNeighbors(coreTile1,null,null, null);
        // Add tiles to array for passing to stringifyer
        tiles.add(standardTile1,coreTile1, standardTile3);
        TileArrayToString tileArrayToString = new TileArrayToString(tiles);

        // When - Running reverse compare string
        String result = tileArrayToString.reverseToCompareString();

        // Then
        assertEquals("STD0COR1STD", result);
    }
}