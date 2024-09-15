package org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileUtility.TileTypeData;

public class ScreenSwapTile extends ShipTile{
    private TileShipGame game;
    private int screenNumber;

    public ScreenSwapTile(Vector2 vector2, TileShipGame game, int screenNumber){
        super(vector2, ID.ScreenSwapTile, TileTypeData.ScreenSwapTile);
        this.game = game; // Might be better to use a static reference.
        this.screenNumber = screenNumber;
    }

    @Override
    public void replaced() {

    }

    @Override
    public void pickedUp() {

    }

    @Override
    public Texture getTexture() {
        return Resources.AncientAsteroidTexture;
    }

    @Override
    public boolean deleteFromGame() {
        return false;
    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }

    @Override
    public void newNeighbor(ShipTile newNeighbor){
        if(newNeighbor != null){
            swapScreen();
        }
    }

    private void swapScreen(int screenNumber){
        game.changeScreen(screenNumber);
    }

    public void swapScreen(){
        game.changeScreen(this.screenNumber);
    }
}
