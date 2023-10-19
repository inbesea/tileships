package org.bitbucket.noahcrosby.shipGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;

import java.util.ArrayList;

public class LevelSelectionScreen extends ScreenAdapter implements Screen {
    private Stage stage;
    private ArrayList<TextButton> levelButtons;
    final TileShipGame game;
    Table table;

    public LevelSelectionScreen(final TileShipGame game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        levelButtons = new ArrayList<>();
    }

    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Each time we show this the table is reasserted to update values
        this.table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        setUpLevelButtons();
//        setButtonBehavior(); come back to this later lol
    }

    private void setUpLevelButtons() {
        stage.clear();

        Gdx.input.setInputProcessor(stage);
        // Create a table that fills the screen. Everything else will go inside this table.
        this.table = new Table();
    }

//    private void setButtonBehavior() {
//        for (TextButton button : levelButtons) {
//            button.addListener((ChangeListener) (event, actor) -> {
//
//            });
//        }
//    }

}
