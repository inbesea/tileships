package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

/**
 * Not meant to be a physical part of the ship. Only used for communicating tile condensations that are complicated.
 * Like fueling the ship for example.
 */
public class CommunicationTile extends ShipTile {

    public static int FUELING_SHIP = 0;


    int identity;
    int intValue;

    public CommunicationTile() {
        super(new Vector2(0, 0),  ID.CommunicationTile, TileTypeData.CommunicationTile);
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }

    @Override
    public void newNeighbor(ShipTile newNeighbor) {

    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public int getIdentity() {
        return identity;
    }

    public int getIntValue() {
        return intValue;
    }
}
