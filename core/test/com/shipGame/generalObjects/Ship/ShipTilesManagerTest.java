package com.shipGame.generalObjects.Ship;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.StandardTile;
import junit.framework.TestCase;
import org.junit.Test;

public class ShipTilesManagerTest extends TestCase {

    ShipTilesManager tilesManager;
    Ship ship;
    StandardTile tile;
    Vector3 westMousePosition;

    @Test
    public void testWhenGettingClosestSide_GetExpectedResult(){
        // Before
        ship = new Ship(new Vector2(0,0), new AssetManager());
        tilesManager = new ShipTilesManager(ship);
        tile = new StandardTile(new Vector2(0,0),tilesManager);
        westMousePosition = new Vector3(0, ShipTile.TILESIZE/2f,0);

        // When
        Vector2 closestSide = tilesManager.getVectorOfClosestSide(tile, westMousePosition);

        // Then
        assertEquals(new Vector2(tile.getX() - (ShipTile.TILESIZE/2.0f), tile.getY()+(ShipTile.TILESIZE/2.0f)),
                    closestSide);
    }

}