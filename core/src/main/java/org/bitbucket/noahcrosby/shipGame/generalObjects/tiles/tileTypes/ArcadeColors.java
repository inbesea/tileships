package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import org.bitbucket.noahcrosby.javapoet.Resources;

/**
 * This reflects an optional color for a tile to have.
 * Currently used only in Arcade mode
 */
public enum ArcadeColors {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    BLUE("blue"),
    PURPLE("purple"),
    SILVER("silver");
    private String color;
    ArcadeColors(String color){
        this.color = color;
    }

    public static Texture getTileTexture(ArcadeColors arcadeColors) {
        switch (arcadeColors) {
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

    public static Texture getAsteroidTexture(ArcadeColors arcadeColors) {
        switch (arcadeColors) {
            case RED:
                return Resources.AsteroidRedTexture;
            case ORANGE:
                return Resources.AsteroidOrangeTexture;
            case YELLOW:
                return Resources.AsteroidYellowTexture;
            case GREEN:
                return Resources.AsteroidGreenTexture;
            case BLUE:
                return Resources.AsteroidBlueTexture;
            case PURPLE:
                return Resources.AsteroidPurpleTexture;
            case SILVER:
                return Resources.AsteroidSilverTexture;
            default:
                return null;
        }
    }

    /**
     * Returns a random color
     * @return
     */
    public static ArcadeColors getRandomColor() {
        return values()[(int) (Math.random() * values().length)];
    }
}
