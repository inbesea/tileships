package com.shipGame.generalObjects.Ship.tiles.tileUtility;

/**
 * Used to hold unique data about tileable types of shiptiles.
 */
public enum TileTypeData {
    CoreTile("COR"),
    StandardTile("STD"),
    StrongTile("STR");

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
