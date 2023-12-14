package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.LambdaRecipe;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileRecipes;

/**
 * Class to check if criteria for building a tile recipe is met.
 * This includes what the ship has, and progression unlocks.
 *
 * Recipes are always oriented so the first two tiles are pointed north e.g. STD0STD...
 */
public class UnlockTracker {

    Array<TileRecipes> allStringRecipes = new Array<>();
    Array<LambdaRecipe> allLambdaRecipes = new Array<>();
    Array<LambdaRecipe> unlockedLambdaRecipes = new Array<>();
    Array<TileRecipes> unlockedStringRecipes = new Array<>();

    public UnlockTracker (){
        populateAllRecipies();
        unlockRecipe(ID.StrongTile);
    }

    private void populateAllRecipies() {
        // TODO : Replace this static approach with a new approach
        allStringRecipes.add(new TileRecipes("STD0STD1STD2STD", ID.StrongTile));
        allLambdaRecipes.add(TileRecipes.arcadeRemove);
    }

    public boolean unlockRecipe(ID newUnlock){
        TileRecipes tileRecipes;
        for(int i = 0; i < allStringRecipes.size ; i++){
            tileRecipes = allStringRecipes.get(i);
            if(tileRecipes.getId() == newUnlock){
                if(unlockedStringRecipes.contains(tileRecipes,true))System.out.println("ERROR : Attempting to unlock unlocked tile recipe");
                unlockedStringRecipes.add(tileRecipes);
                System.out.println(newUnlock + " successfully unlocked!");
                return true;
            }
        }
        return false;
    }

    /**
     * Method to unlock a new recipe by passing a TileRecipe in
     * @param newUnlock
     * @return
     */
    public boolean unlockRecipe(TileRecipes newUnlock){
        for(int i = 0; i < allStringRecipes.size ; i++){
            if(allStringRecipes.get(i).getId() == newUnlock.getId()){
                if(unlockedStringRecipes.contains(newUnlock,true))throw new RuntimeException("Attempting to unlock unlocked tile recipe");
                unlockedStringRecipes.add(newUnlock);
                System.out.println(newUnlock.getId() + " successfully unlocked!");
                return true;
            }
        }
        return false;
    }
}
