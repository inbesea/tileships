package org.bitbucket.noahcrosby.shipGame.effects;

import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.jetbrains.annotations.Nullable;

public class EffectContent {

    public static int BLOCK_PLACEMENT = 0;
    private int effectType = -1;
    @Nullable private ShipTile tile;

    public EffectContent(int effectType, ShipTile tile) {
        this.effectType = effectType;
        this.tile = tile;
    }

    public int getEffectType() {
        return effectType;
    }

    public void setEffectType(int effectType) {
        this.effectType = effectType;
    }
}
