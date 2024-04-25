package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

/**
 * Classic engine. Gives the ship the power of movement.
 * Each engine can be given a block of fuel to produce a node of movement.
 */
public class FurnaceTile extends ShipTile{

    private boolean isFueled;

    public FurnaceTile(Vector2 position) {
        super(position, ID.FurnaceTile, TileTypeData.FurnaceTile);
    }

    @Override
    public Texture getTexture() {
        return Resources.FurnaceTexture;
    }

    @Override
    public boolean deleteFromGame() {
        setIsDeadTrue();
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    /**
     * Engine tiles are given fuel when they're placed.
     */
    @Override
    public void replaced() {

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
