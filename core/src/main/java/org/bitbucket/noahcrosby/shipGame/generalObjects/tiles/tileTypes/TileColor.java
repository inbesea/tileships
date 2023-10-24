package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import org.bitbucket.noahcrosby.javapoet.Resources;

/**
 * This reflects an optional color for a tile to have.
 * Currently used only in Arcade mode
 */
public enum TileColor {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    BLUE("blue"),
    PURPLE("purple"),
    SILVER("silver");
    private String color;
    TileColor(String color){
        this.color = color;
    }

    public static Texture getTexture(TileColor tileColor) {
        switch (tileColor) {
            case RED:
                return Resources.ShipTileRedTexture;
            case ORANGE:
                return Resources.ShipTileOrangeTexture;
            case YELLOW:
                return Resources.ShipTileYellowTexture;
            case GREEN:
                return Resources.ShipTileGreenTexture;
            case BLUE:
                return Resources.ShipTileBlueTexture;
            case PURPLE:
                return Resources.ShipTilePurpleTexture;
            case SILVER:
                return Resources.ShipTileSilverTexture;
            default:
                return null;
        }
    }
}
