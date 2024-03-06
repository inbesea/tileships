package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.interfaces.ShipBuilderInterface;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is meant to hold instances to general ship types that can be built out more if needed.
 */
public class ShipBuilder implements ShipBuilderInterface {
    Box2DWrapper box2DWrapper;
    Vector2 center;
    List<TileInit> tileInitList;

    @Override
    public void reset() {
        center = null;
        box2DWrapper = null;
        tileInitList = new ArrayList<>();
    }

    @Override
    public void setCenter(Vector2 center) {
        this.center = center;
    }

    @Override
    public void setBox2DWrapper(Box2DWrapper box2DWrapper) {
        this.box2DWrapper = box2DWrapper;
    }

    @Override
    public void addTile(int x, int y, ID id) {
        tileInitList.add(new TileInit(id, x, y));
    }

    @Override
    public Ship buildProduct() {
        if(box2DWrapper == null || center == null){
            System.out.println("Unable to build ship : Box2DWrapper or center is null");
            return null;
        } else if(tileInitList.size() == 0){
            System.out.println("Unable to build ship : tileInitList is empty");
            return null;
        }

        // Create the ship and return it, initializing the tiles.
        Ship ship = new Ship(center, box2DWrapper);
        for (TileInit tileInit : tileInitList){
            ship.addTileToShip((tileInit.getX() * ShipTile.TILE_SIZE) + 2, (tileInit.getY() * ShipTile.TILE_SIZE) + 2, tileInit.getId());
        }
        return ship;
    }
    /* example:
    * BASICSHIP() = {
    * tile at 0,0 - 0,1 - 1,0 - etc.
    */
}
