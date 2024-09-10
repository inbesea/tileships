package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.StandardTile;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship ship;

    @BeforeEach
    void setUp() {
        // Skip debug rendering for unit testing
        Box2DWrapper box2DWrapper = new Box2DWrapper(false);
        ship = new Ship(new Vector2(0,0), box2DWrapper);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testShipExists() {
        assertNotNull(ship);
    }

    @Test
    void testAddTile() {
        ship.getTileManager().addTile(300, 300, new StandardTile(new Vector2(0,0)));
        ship.getTileManager().addTile(-300, -300, new StandardTile(new Vector2(0,0)));

        assertEquals(2, ship.getTileManager().getExistingTiles().size);
        assertEquals(2, ship.getExistingTiles().size);

        assertEquals(1 , ship.getTileManager().getExistingTiles().get(0).numberOfNeighbors());
    }

    @Test
    void testAddTileNoSnap() {
        ship.getTileManager().addTileNoSnap(300, 300, new StandardTile(new Vector2(0,0)));
        ship.getTileManager().addTileNoSnap(-300, -300, new StandardTile(new Vector2(0,0)));

        assertNotEquals(0, ship.getTileManager().getExistingTiles().size);
        assertEquals(0 , ship.getTileManager().getExistingTiles().get(0).numberOfNeighbors());
        assertEquals(2, ship.getExistingTiles().size);
    }

    @Test
    void test_addTileNoSnap_ID() {
        ship.getTileManager().addTileNoSnap(300, 300, ID.StandardTile);
        ship.getTileManager().addTileNoSnap(-300, -300, new StandardTile(new Vector2(0,0)));

        assertNotEquals(0, ship.getTileManager().getExistingTiles().size);
        assertEquals(0 , ship.getTileManager().getExistingTiles().get(0).numberOfNeighbors());
        assertEquals(2, ship.getExistingTiles().size);
    }

    // TODO : Add test and implement add no snap tile with class
    // TODO : Refactor all tile adding to use tile data objects.
}
