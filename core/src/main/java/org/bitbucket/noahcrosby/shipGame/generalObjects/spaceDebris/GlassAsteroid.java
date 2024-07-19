package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.GlassTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.WoodTile;

public class GlassAsteroid extends Asteroid{
    public GlassAsteroid() {
        super(new Vector2(0,0), new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid);
    }
    public GlassAsteroid(Vector2 position, Vector2 size, ID id) {
        super(position, size, id);
    }

    // What tile is made
    @Override
    public Class getTileType() {
        return GlassTile.class;
    }

    @Override
    public ID productionOutputID(){
        Gdx.app.debug("Production Output ID", "Warning, default production output ID return");
        return ID.WoodTile;
    }
    // What texture this is
    @Override
    public Texture getTexture() {
        return Resources.GlassAsteroidTexture;
    }
}
