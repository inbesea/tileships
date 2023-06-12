package com.shipGame.ncrosby.generalObjects.Ship.tiles.tileManagers;

import com.shipGame.ncrosby.ID;

import java.util.function.Function;

/**
 * Class to hold lambda expression to compare and produce a tile ID.
 *
 * TileRecipes holds a simple matching String. The recipe String assumes the input to match is oriented upwards. F or example : STD0COR
 */
public class TileRecipes {
    private String recipe;

    private ID id;
    // Lambda that returns an ID based on a recipe string
    private Function<String, ID> recipeExpression;

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
