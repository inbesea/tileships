package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ArcadeColors;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ColorTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.CommunicationTile;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ShipTile;
import org.bitbucket.noahcrosby.shipGame.arcadeMode.ArcadeModeScreen;

/**
 * Class to hold lambda expression to compare and produce a tile ID.
 *
 * TileRecipes holds a simple matching String. The recipe String assumes the input to match is oriented upwards. F or example : STD0COR
 */
public class TileRecipes {
    public static LambdaRecipe arcadeRemove = (Array<ShipTile> tiles) -> {
        Array<ArcadeColors> arcadeColors = new Array<>();
        ColorTile colorTile;
        // Check for color tiles and no duplicate colors.
        if(tiles.size < ArcadeModeScreen.tetrisSize) return null;
        for (int i = 0; i < tiles.size; i++) {
            if(tiles.get(i).getID() == ID.ColorTile){
                colorTile = (ColorTile) tiles.get(i);
                for(int n = 0 ; n < arcadeColors.size ; n++){ // Check for duplicate colors each addition
                    if(arcadeColors.get(n).equals(colorTile.getColor()))return null;
                }
                arcadeColors.add(colorTile.getColor());
            }
        }
        System.out.println("Tetris found!!!");
        for(int i = 0 ; tiles.size > 0 ; ){
            ShipTile tile = tiles.get(i);
            System.out.println("Tetris match removing  : " + tile.getPositionAsString());
            tile.setIsDeadTrue();
        }
        return null;
    };
    public static LambdaRecipe addFuel = (Array<ShipTile> tiles) -> {
        Array<ShipTile> fuel = new Array<>();
        int furnace = 0;
        for (int i = 0; i < tiles.size; i++) {
            if (tiles.get(i).getID() == ID.FurnaceTile) {
                furnace++;
            } else if (tiles.get(i).isFuel()) {
                fuel.add(tiles.get(i));
            }
        }
        if (fuel.size > 0 && furnace > 0) { // If there is fuel and a furnace
            int fuelAmt = 0;
            for(int i = 0 ; i < fuel.size ; i++){
                fuelAmt++; // Could make different tiles produce different amounts of fuel
                // This is a complicated way to say fuelamt = fuel.size currently. It will probably change later.
            }
            CommunicationTile communicationTile = new CommunicationTile();
            communicationTile.setIdentity(CommunicationTile.FUELING_SHIP);
            communicationTile.setIntValue(fuelAmt);
            return communicationTile; // Add a communication tile
        }
        return null;
    };
    private String recipe;

    private ID id;
    // Lambda that returns an ID based on a recipe string

    public TileRecipes(String recipe, ID id) {
        initValidation(recipe, id);
        this.id = id;
        this.recipe = recipe;
    }

    private void initValidation(String recipe, ID id) {
        try{
            String firstDirection =  recipe.substring(3,4); // We want to check that the first directional int is 0, example : STD0COR
            int firstDirectionInt = Integer.parseInt(firstDirection); // To confirm the formatting is followed
            if(firstDirectionInt != AdjacentTiles.UP){
                throw new IllegalArgumentException("Cannot init tileRecipe with disoriented recipe");
            }
        }catch (ClassCastException classCastException){

        }

    }


    /**
     * Recipe check to get ID if match
     * @param compareString
     * @return
     */
    public ID tileIfMatch(String compareString) {
        if (compareString.equals(recipe)) {
            return this.id;
        } else {
            return null;
        }
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
