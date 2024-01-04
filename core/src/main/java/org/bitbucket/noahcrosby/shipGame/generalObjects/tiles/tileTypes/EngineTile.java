package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.AdjacentTiles;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

/**
 * Classic engine. Gives the ship the power of movement.
 * Each engine can be given a block of fuel to produce a node of movement.
 */
public class EngineTile extends ShipTile{

    private boolean isFueled;

    public EngineTile(Vector2 position, TileTypeData typeData) {
        super(position, ID.EngineTile, typeData);
    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        setIsDeadTrue();
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    /**
     * Engine tiles are given fuel when they're placed.
     */
    @Override
    public void replaced() {
        AdjacentTiles adjacentTiles = this.getNeighbors();
        if(adjacentTiles.contain(ID.FuelTile)){
            this.setIsFuled(true);
        }else{
            this.setIsFuled(false);
        }
    }

    private boolean setIsFuled(boolean b) {
        this.isFueled = b;
        return this.isFueled;
    }

    public boolean isFueled() {
        return this.isFueled;
    }

    @Override
    public void pickedUp() {

    }

    @Override
    public void setVelocity(Vector2 velocity) {
        Gdx.app.log("EngineTile", "Velocity: " + velocity + " Should not have been set.");
    }
}
