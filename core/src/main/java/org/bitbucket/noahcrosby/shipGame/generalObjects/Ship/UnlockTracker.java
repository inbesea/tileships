package org.bitbucket.noahcrosby.shipGame.generalObjects.Ship;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileRecipes;

/**
 * Class to check if criteria for building a tile recipe is met.
 * This includes what the ship has, and progression unlocks.
 *
 * Recipes are always oriented so the first two tiles are pointed north e.g. STD0STD...
 */
public class UnlockTracker {

    Array<TileRecipes> allRecipes = new Array<>();
    Array<TileRecipes> unlockedRecipes = new Array<>();

    public UnlockTracker (){
        populateAllRecipies();
        unlockRecipe(ID.StrongTile);
    }

    private void populateAllRecipies() {
        allRecipes.add(new TileRecipes("STD0STD1STD2STD", ID.StrongTile));
    }

    public boolean unlockRecipe(ID newUnlock){
        TileRecipes tileRecipes;
        for(int i = 0 ; i < allRecipes.size ; i++){
            tileRecipes = allRecipes.get(i);
            if(tileRecipes.getId() == newUnlock){
                if(unlockedRecipes.contains(tileRecipes,true))throw new RuntimeException("Attempting to unlock unlocked tile recipe");
                unlockedRecipes.add(tileRecipes);
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
        for(int i = 0 ; i < allRecipes.size ; i++){
            if(allRecipes.get(i).getId() == newUnlock.getId()){
                if(unlockedRecipes.contains(newUnlock,true))throw new RuntimeException("Attempting to unlock unlocked tile recipe");
                unlockedRecipes.add(newUnlock);
                System.out.println(newUnlock.getId() + " successfully unlocked!");
                return true;
            }
        }
        return false;
    }
}
