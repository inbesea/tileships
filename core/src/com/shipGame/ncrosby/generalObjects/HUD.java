package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.tileShipGame;

/**
 * Layer of HUD elements. Is on a FitViewport to keep hud elements scaled correctly.
 */
public class HUD {
    private ExtendViewport HUDScreenLayer;
    private tileShipGame game;
    private Ship playerShip;

    private boolean showDebugHud = true;

    public HUD(AssetManager assetManager, tileShipGame game) {
        this.game = game;
        this.playerShip = game.getPlayerShip();

        HUDScreenLayer = new ExtendViewport(tileShipGame.defaultViewportSizeX,tileShipGame.defaultViewportSizeY);
    }

    /**
     * Called to draw the HUD
     * Has own internal batch draw call for hud to use HUD veiwport
     */
    public void draw(){
        StringBuilder stringBuilder = new StringBuilder();
        if(showDebugHud){
            stringBuilder = buildDebugUI();
        }

        //Secondly draw the Hud
        game.batch.setProjectionMatrix(HUDScreenLayer.getCamera().combined); //set the spriteBatch to draw what our stageViewport sees

        game.batch.begin();

        if(showDebugHud){
            game.font.draw(game.batch, stringBuilder.toString() , 2,
                    (HUDScreenLayer.getWorldHeight() - 2)); // Worldheight gives the extendVeiwport hight
        }

        game.batch.end();
    }

    /**
     * Gets data for debug hud UI stack
     * @return - StringBuilder representing the debug info
     */
    private StringBuilder buildDebugUI() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("FPS " + Gdx.graphics.getFramesPerSecond() + "\n" +
                "ShipTile number : " + playerShip.numberOfShipTiles());
        if(playerShip.destroyedTileCount > 0){
            stringBuilder.append("\nTiles Destroyed : " + playerShip.destroyedTileCount);
        }

        return stringBuilder;
    }

    /**
     * Get if debug HUD should be rendered
     * @return - true if should show debug hud, else should not
     */
    public boolean isShowDebugHud() {
        return showDebugHud;
    }

    /**
     * Call to toggle seeing the debug hud
     * @param showDebugHud - if true will show debug HUD, else will not
     */
    public void setShowDebugHud(boolean showDebugHud) {
        this.showDebugHud = showDebugHud;
    }

    /**
     * Called on screen resize to keep HUD consistent
     * @param width
     * @param height
     */
    public void update(int width, int height) {
        HUDScreenLayer.update(width, height, true);
    }
}
