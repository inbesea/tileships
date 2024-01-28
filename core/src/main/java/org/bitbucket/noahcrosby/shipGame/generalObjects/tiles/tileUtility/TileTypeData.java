package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;

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

    /**
     * Returns a string of [abbreviation - position, abbrv. - pos., ... ]
     * @param tiles - tiles to describe
     * @return - string of tile abbreviations and positions
     */
    public static String generateTileDescriptionString(Array<ShipTile> tiles){
        ShipTile tile;
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        for(int i = 0 ; i < tiles.size ; i++){
            tile = tiles.get(i);
            builder.append(tile.getAbbreviation() + " - " + tile.getPositionAsString());
            if(i != tiles.size-1)builder.append(", ");
        }
        builder.append("]");

        return builder.toString();
    }
}
