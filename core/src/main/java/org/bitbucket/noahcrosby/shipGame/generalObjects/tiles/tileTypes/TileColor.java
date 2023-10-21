package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

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
}
