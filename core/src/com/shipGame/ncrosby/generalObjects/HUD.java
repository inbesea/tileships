package com.shipGame.ncrosby.generalObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HUD {
    private Stage stage;
    private FitViewport stageViewport;

    public HUD(SpriteBatch spriteBatch) {
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
