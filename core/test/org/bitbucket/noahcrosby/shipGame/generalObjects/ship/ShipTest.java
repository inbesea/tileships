package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import com.badlogic.gdx.math.Vector2;
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
    void testAddTileNoSnap() {
        ship.getTileManager().addTileNoSnap(300, 300, new StandardTile(new Vector2(0,0)));
        ship.getTileManager().addTileNoSnap(-300, -300, new StandardTile(new Vector2(0,0)));

        assertNotEquals(0, ship.getTileManager().getExistingTiles().size);
        assertEquals(0 , ship.getTileManager().getExistingTiles().get(0).numberOfNeighbors());
        assertEquals(2, ship.getExistingTiles().size);
    }
}
