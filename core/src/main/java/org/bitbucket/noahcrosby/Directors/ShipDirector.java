package org.bitbucket.noahcrosby.Directors;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;
import org.bitbucket.noahcrosby.shipGame.util.ShipBuilder;

public class ShipDirector {
    ShipBuilder shipBuilder;

    public ShipDirector(){
        shipBuilder = new ShipBuilder();
    }

    public Ship buildClassicShip(Box2DWrapper box2DWrapper, Vector2 center){
        shipBuilder.reset();

        shipBuilder.setCenter(center);
        shipBuilder.setBox2DWrapper(box2DWrapper);

        shipBuilder.addTile((int)center.x, (int)center.y, ID.CoreTile);
        shipBuilder.addTile((int)center.x + 1, (int)center.y, ID.StandardTile);
        shipBuilder.addTile((int)center.x - 1, (int)center.y, ID.StandardTile);
        shipBuilder.addTile((int)center.x, (int)center.y - 1, ID.StandardTile);
        shipBuilder.addTile((int)center.x, (int)center.y + 1, ID.StandardTile);

        return shipBuilder.buildProduct();
    }

    public Ship buildArcadeShip(Box2DWrapper box2DWrapper, Vector2 center){
        shipBuilder.reset();

        shipBuilder.setCenter(center);
        shipBuilder.setBox2DWrapper(box2DWrapper);

        shipBuilder.addTile((int)center.x, (int)center.y, ID.CoreTile);

        return shipBuilder.buildProduct();
    }
}
