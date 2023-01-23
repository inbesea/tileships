package com.shipGame.ncrosby.generalObjects.Ship.tiles;

import com.badlogic.gdx.utils.Json;
import com.shipGame.ncrosby.ID;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class to hold lambda expression to compare and produce a tile ID.
 */
public class TileRecipes {
    private String recipe;

    private ID id;
    // Lambda that returns an ID based on a recipe string
    private Function<String, ID> recipeExpression;

    public TileRecipes(String recipe, ID id) {
        this.id = id;
        this.recipe = recipe;
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
