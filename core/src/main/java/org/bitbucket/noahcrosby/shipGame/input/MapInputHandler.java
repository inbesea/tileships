package org.bitbucket.noahcrosby.shipGame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import org.bitbucket.noahcrosby.shipGame.screens.GameScreen;

/**
 * Handles player map interactions
 * Like turning on and off the map draw
 */
public class MapInputHandler extends InputAdapter {
    private GameScreen screen;

    public MapInputHandler(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.M) { // Open the map
            screen.toggleMap();
            Gdx.app.log("Map", "Toggling map - setting mapDraw to " + screen.showingMap());
        } else if (keycode == Input.Keys.ESCAPE) {
            if(screen.showingMap()){
                screen.toggleMap();
                Gdx.app.log("Map", "Toggling map - setting mapDraw to " + screen.showingMap());
            }
        }
        return false;
    }
}
