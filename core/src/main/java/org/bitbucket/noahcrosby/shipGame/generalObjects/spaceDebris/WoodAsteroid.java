package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.StrongTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.WoodTile;

public class WoodAsteroid extends Asteroid {
    public WoodAsteroid() {
        super(new Vector2(0,0), new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid);
    }
    public WoodAsteroid(Vector2 position, Vector2 size, ID id) {
        super(position, size, id);
    }

    // What tile is made
    @Override
    public Class getTileType() {
        return WoodTile.class;
    }

    @Override
    public ID productionOutputID(){
        Gdx.app.debug("Production Output ID", "Warning, default production output ID return");
        return ID.WoodTile;
    }
    // What texture this is
    @Override
    public Texture getTexture() {
        return Resources.WoodAsteroidTexture;
    }
}
