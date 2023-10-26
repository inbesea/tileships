package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility;

/**
 * Used to hold unique data about tileable types of shiptiles.
 */
public enum TileTypeData {
    CoreTile("COR"),
    StandardTile("STD"),
    StrongTile("STR"),
    ColorTile("CLR");

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    private String abbreviation;
    TileTypeData(String abbreviation){
        this.abbreviation = abbreviation;
    }


}
