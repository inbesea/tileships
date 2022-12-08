package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipGame.ncrosby.generalObjects.Ship.Ship;
import com.shipGame.ncrosby.tileShipGame;

public class HUD {
    private Stage stage;
    private FitViewport HUDScreenLayer;
    private tileShipGame game;
    private Ship playerShip;

    private boolean showDebugHud = true;

    public HUD(SpriteBatch spriteBatch, tileShipGame game) {
        this.game = game;
        this.playerShip = game.getPlayerShip();

        HUDScreenLayer = new FitViewport(500,500);
        stage = new Stage(HUDScreenLayer, spriteBatch); //create stage with the stageViewport and the SpriteBatch given in Constructor
    }

    public Stage getStage() { return stage; }

    public void draw(){
        StringBuilder stringBuilder = new StringBuilder();
        if(showDebugHud){
            stringBuilder = buildDebugUI();
        }

        //Secondly draw the Hud
        game.batch.setProjectionMatrix(HUDScreenLayer.getCamera().combined); //set the spriteBatch to draw what our stageViewport sees

        game.batch.begin();

        if(showDebugHud){
            game.font.draw(game.batch, stringBuilder.toString() , 2, (HUDScreenLayer.getScreenHeight()));
        }

        game.batch.end();
    }

    private StringBuilder buildDebugUI() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("FPS " + Gdx.graphics.getFramesPerSecond() + "\n" +
                "ShipTile number : " + playerShip.numberOfShipTiles());
        if(playerShip.destroyedTileCount > 0){
            stringBuilder.append("\nTiles Destroyed : " + playerShip.destroyedTileCount);
        }

        return stringBuilder;
    }

    public boolean isShowDebugHud() {
        return showDebugHud;
    }

    public void setShowDebugHud(boolean showDebugHud) {
        this.showDebugHud = showDebugHud;
    }

    public void dispose(){
        stage.dispose();
    }
}
