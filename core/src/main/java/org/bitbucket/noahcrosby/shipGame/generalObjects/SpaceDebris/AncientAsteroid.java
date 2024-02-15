package org.bitbucket.noahcrosby.shipGame.generalObjects.SpaceDebris;

import com.badlogic.gdx.graphics.Texture;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.AncientTile;

public class AncientAsteroid extends Asteroid{

    @Override
    public Texture getTexture() {
        return Resources.AncientAsteroidTexture;
    }

    @Override
    public Class productionOutput() {
        return AncientTile.class;
    }

    @Override
    public ID productionOutputID(){
        return ID.AncientTile;
    }
}
