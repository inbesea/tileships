package org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris;

import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.AncientTile;

public class AncientAsteroid extends Asteroid{

    @Override
    public Class productionOutput() {
        return AncientTile.class;
    }
}
