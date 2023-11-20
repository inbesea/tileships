package org.bitbucket.noahcrosby.shipGame.input;

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
            // TODO : Open the map
            screen.toggleMap();
            System.out.println("Attempting to open Map - setting mapDraw to " + screen.showingMap());
        }
        return false;
    }
}
