package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.StrongTile;

public class MetalAsteroid extends Asteroid {
    public MetalAsteroid() {
    super(new Vector2(0,0), new Vector2(ShipTile.TILE_SIZE,ShipTile.TILE_SIZE), ID.Asteroid);
    }
    public MetalAsteroid(Vector2 position, Vector2 size, ID id) {
        super(position, size, id);
    }

    // What tile is made
    @Override
    public Class getTileType() {
        return StrongTile.class;
    }

    // What texture this is
    @Override
    public Texture getTexture() {
        return Resources.AsteroidSilverTexture;
    }
}
