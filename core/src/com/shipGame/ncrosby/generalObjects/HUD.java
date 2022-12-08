package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shipGame.ncrosby.tileShipGame;

public class HUD {
    private Stage stage;
    private FitViewport stageViewport;
    private tileShipGame game;

    public HUD(SpriteBatch spriteBatch, tileShipGame game) {
        this.game = game;

        stageViewport = new FitViewport(500,500);
        stage = new Stage(stageViewport, spriteBatch); //create stage with the stageViewport and the SpriteBatch given in Constructor

        Table table = new Table();


//        ...
//        ... //add the Buttons etc.

        stage.addActor(table);
    }

    public Stage getStage() { return stage; }

    public void dispose(){
        stage.dispose();
    }
}
